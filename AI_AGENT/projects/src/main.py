import argparse
import asyncio
import json
import os
import sys
import uuid
import threading
import traceback
import logging
from datetime import datetime
from typing import Any, Dict, Iterable, AsyncIterable, AsyncGenerator, Optional

# 确保 src 目录在 Python 路径中
sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))

# 本地部署：优先加载项目根目录的 .env
try:
    from dotenv import load_dotenv
    load_dotenv(os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", ".env"))
except ImportError:
    pass

import uvicorn
import time
from fastapi import FastAPI, HTTPException, Request
from fastapi.responses import StreamingResponse, JSONResponse
from langchain_core.runnables import RunnableConfig
from langgraph.graph import StateGraph, END
from langgraph.graph.state import CompiledStateGraph

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# 超时配置常量
TIMEOUT_SECONDS = 900  # 15分钟


# ---- 简单上下文 ----
class SimpleContext:
    """简单的请求上下文，替代 Coze Context"""
    def __init__(self, method: str = "", headers: Optional[Dict] = None):
        self.run_id = str(uuid.uuid4())
        self.method = method


def new_context(method: str = "", headers: Optional[Dict] = None) -> SimpleContext:
    return SimpleContext(method=method, headers=headers)


def _extract_core_stack() -> str:
    """提取核心堆栈信息"""
    return traceback.format_exc()


def _is_dev_env() -> bool:
    env = os.getenv("ENV", "dev").lower()
    return env in ("dev", "development", "local")


def _is_agent_proj() -> bool:
    return True  # 本项目是 Agent 项目


def _init_run_config(graph: CompiledStateGraph, ctx: SimpleContext) -> RunnableConfig:
    return RunnableConfig(configurable={"thread_id": ctx.run_id})


def _init_agent_config(graph: CompiledStateGraph, ctx: SimpleContext) -> RunnableConfig:
    return RunnableConfig(configurable={"thread_id": ctx.run_id, "session_id": ctx.run_id})


# ---- 简单错误分类器 ----
class SimpleErrorClassifier:
    def classify(self, e: Exception, context: Dict[str, Any]) -> Any:
        return type("ErrorInfo", (), {"code": "UNKNOWN", "message": str(e), "category": type("Cat", (), {"name": "UNKNOWN"})})()

    def get_error_response(self, e: Exception, context: Dict[str, Any]) -> Dict[str, Any]:
        return {"error_code": "UNKNOWN", "error_message": str(e)}


class GraphService:
    def __init__(self):
        self.running_tasks: Dict[str, asyncio.Task] = {}
        self.error_classifier = SimpleErrorClassifier()
        self._graph = None
        self._graph_lock = threading.Lock()

    def _get_graph(self, ctx=None):
        if _is_agent_proj():
            from agents.agent import get_agent_by_request
            return get_agent_by_request({})

        if self._graph is not None:
            return self._graph
        with self._graph_lock:
            if self._graph is not None:
                return self._graph
            from graphs import create_learning_workflow
            self._graph = create_learning_workflow()
            return self._graph

    @staticmethod
    def _sse_event(data: Any, event_id: Any = None) -> str:
        id_line = f"id: {event_id}\n" if event_id else ""
        return f"{id_line}event: message\ndata: {json.dumps(data, ensure_ascii=False, default=str)}\n\n"

    def stream(self, payload: Dict[str, Any], run_config: RunnableConfig, ctx=None) -> Iterable[Any]:
        graph = self._get_graph(ctx)
        for chunk in graph.stream(payload, config=run_config):
            yield chunk

    async def run(self, payload: Dict[str, Any], ctx=None) -> Dict[str, Any]:
        if ctx is None:
            ctx = new_context("run")

        run_id = ctx.run_id
        logger.info(f"Starting run with run_id: {run_id}")

        try:
            graph = self._get_graph(ctx)
            run_config = _init_run_config(graph, ctx)
            run_config["configurable"] = {"thread_id": ctx.run_id}

            return await graph.ainvoke(payload, config=run_config, context=ctx)

        except asyncio.CancelledError:
            logger.info(f"Run {run_id} was cancelled")
            return {"status": "cancelled", "run_id": run_id, "message": "Execution was cancelled"}
        except Exception as e:
            logger.error(
                f"Error in GraphService.run: {e}\n"
                f"Traceback:\n{_extract_core_stack()}"
            )
            raise
        finally:
            self.running_tasks.pop(run_id, None)

    async def stream_sse(self, payload: Dict[str, Any], ctx=None, run_opt: Optional[Any] = None) -> AsyncGenerator[str, None]:
        if ctx is None:
            ctx = new_context(method="stream_sse")

        run_id = ctx.run_id
        logger.info(f"Starting stream with run_id: {run_id}")
        graph = self._get_graph(ctx)

        if _is_agent_proj():
            run_config = _init_agent_config(graph, ctx)
        else:
            run_config = _init_run_config(graph, ctx)

        is_workflow = not _is_agent_proj()

        try:
            async for chunk in self.astream(payload, graph, run_config=run_config, ctx=ctx):
                if is_workflow and isinstance(chunk, tuple):
                    event_id, data = chunk
                    yield self._sse_event(data, event_id)
                else:
                    yield self._sse_event(chunk)
        finally:
            self.running_tasks.pop(run_id, None)

    def cancel_run(self, run_id: str, ctx: Optional[SimpleContext] = None) -> Dict[str, Any]:
        logger.info(f"Attempting to cancel run_id: {run_id}")

        if run_id in self.running_tasks:
            task = self.running_tasks[run_id]
            if not task.done():
                task.cancel()
                logger.info(f"Cancellation requested for run_id: {run_id}")
                return {
                    "status": "success",
                    "run_id": run_id,
                    "message": "Cancellation signal sent"
                }
            else:
                logger.info(f"Task already completed for run_id: {run_id}")
                return {"status": "already_completed", "run_id": run_id, "message": "Task has already completed"}
        else:
            logger.warning(f"No active task found for run_id: {run_id}")
            return {"status": "not_found", "run_id": run_id, "message": "No active task found"}

    async def run_node(self, node_id: str, payload: Dict[str, Any], ctx=None) -> Any:
        if ctx is None:
            ctx = new_context(method="node_run")

        _graph = self._get_graph()
        node_func = None
        input_cls = None
        output_cls = None

        if hasattr(_graph, 'get_graph'):
            inner = _graph.get_graph()
            nodes = getattr(inner, 'nodes', {})
            if node_id in nodes:
                node_func = nodes[node_id]
                input_cls = _graph.get_input_schema()
                output_cls = _graph.get_output_schema()

        if node_func is None:
            raise KeyError(f"node_id '{node_id}' not found")

        _g = StateGraph(input_cls, input_schema=input_cls, output_schema=output_cls)
        _g.add_node("sn", node_func)
        _g.set_entry_point("sn")
        _g.add_edge("sn", END)
        _graph = _g.compile()

        run_config = _init_run_config(_graph, ctx)
        return await _graph.ainvoke(payload, config=run_config)

    def graph_inout_schema(self) -> Any:
        if _is_agent_proj():
            return {"input_schema": {}, "output_schema": {}}
        graph = self._get_graph()
        input_cls = graph.get_input_schema()
        output_cls = graph.get_output_schema()
        return {
            "input_schema": input_cls.model_json_schema(),
            "output_schema": output_cls.model_json_schema(),
            "code": 0,
            "msg": ""
        }

    async def astream(self, payload: Dict[str, Any], graph: CompiledStateGraph, run_config: RunnableConfig, ctx=None, run_opt: Optional[Any] = None) -> AsyncIterable[Any]:
        async for chunk in graph.astream(payload, config=run_config):
            yield chunk


service = GraphService()
app = FastAPI()


@app.post("/run")
async def http_run(request: Request) -> Dict[str, Any]:
    ctx = new_context(method="run", headers=request.headers)
    run_id = ctx.run_id

    logger.info(
        f"Received request for /run: "
        f"run_id={run_id}, "
        f"query={dict(request.query_params)}"
    )

    try:
        payload = await request.json()
        task = asyncio.create_task(service.run(payload, ctx))
        service.running_tasks[run_id] = task

        try:
            result = await asyncio.wait_for(task, timeout=float(TIMEOUT_SECONDS))
        except asyncio.TimeoutError:
            logger.error(f"Run execution timeout after {TIMEOUT_SECONDS}s for run_id: {run_id}")
            task.cancel()
            try:
                result = await task
            except asyncio.CancelledError:
                return {
                    "status": "timeout",
                    "run_id": run_id,
                    "message": f"Execution timeout: exceeded {TIMEOUT_SECONDS} seconds"
                }

        if not result:
            result = {}
        if isinstance(result, dict):
            result["run_id"] = run_id
        return result

    except json.JSONDecodeError as e:
        logger.error(f"JSON decode error in http_run: {e}")
        raise HTTPException(status_code=400, detail=f"Invalid JSON format, {_extract_core_stack()}")

    except asyncio.CancelledError:
        logger.info(f"Request cancelled for run_id: {run_id}")
        return {"status": "cancelled", "run_id": run_id, "message": "Execution was cancelled"}

    except Exception as e:
        error_response = service.error_classifier.get_error_response(e, {"node_name": "http_run", "run_id": run_id})
        logger.error(
            f"Unexpected error in http_run: [{error_response['error_code']}] {error_response['error_message']}",
            exc_info=True
        )
        raise HTTPException(
            status_code=500,
            detail={
                "error_code": error_response["error_code"],
                "error_message": error_response["error_message"],
                "stack_trace": _extract_core_stack(),
            }
        )


HEADER_X_WORKFLOW_STREAM_MODE = "x-workflow-stream-mode"


def _register_task(run_id: str, task: asyncio.Task):
    service.running_tasks[run_id] = task


@app.post("/stream_run")
async def http_stream_run(request: Request):
    ctx = new_context(method="stream_run", headers=request.headers)
    workflow_stream_mode = request.headers.get(HEADER_X_WORKFLOW_STREAM_MODE, "").lower()
    workflow_debug = workflow_stream_mode == "debug"
    run_id = ctx.run_id
    is_agent = _is_agent_proj()

    logger.info(
        f"Received request for /stream_run: "
        f"run_id={run_id}, "
        f"is_agent_project={is_agent}, "
        f"query={dict(request.query_params)}"
    )

    try:
        payload = await request.json()
    except json.JSONDecodeError as e:
        logger.error(f"JSON decode error in http_stream_run: {e}")
        raise HTTPException(status_code=400, detail=f"Invalid JSON format:{_extract_core_stack()}")

    async def stream_generator():
        try:
            async for chunk in service.stream_sse(payload, ctx):
                yield chunk
        except Exception as e:
            logger.error(f"Stream error: {e}", exc_info=True)
            error_data = json.dumps({
                "error_code": "STREAM_ERROR",
                "error_message": str(e)
            }, ensure_ascii=False)
            yield f"event: error\ndata: {error_data}\n\n"

    response = StreamingResponse(stream_generator(), media_type="text/event-stream")
    return response


@app.post("/cancel/{run_id}")
async def http_cancel(run_id: str, request: Request):
    ctx = new_context(method="cancel", headers=request.headers)
    logger.info(f"Received cancel request for run_id: {run_id}")
    result = service.cancel_run(run_id, ctx)
    return result


@app.post(path="/node_run/{node_id}")
async def http_node_run(node_id: str, request: Request):
    ctx = new_context(method="node_run", headers=request.headers)
    logger.info(
        f"Received request for /node_run/{node_id}: "
        f"query={dict(request.query_params)}",
    )

    try:
        payload = await request.json()
    except json.JSONDecodeError as e:
        logger.error(f"JSON decode error in http_node_run: {e}")
        raise HTTPException(status_code=400, detail=f"Invalid JSON format:{_extract_core_stack()}")

    try:
        return await service.run_node(node_id, payload, ctx)
    except KeyError:
        raise HTTPException(status_code=404,
                            detail=f"node_id '{node_id}' not found, traceback: {_extract_core_stack()}")
    except Exception as e:
        error_response = service.error_classifier.get_error_response(e, {"node_name": node_id})
        logger.error(
            f"Unexpected error in http_node_run: [{error_response['error_code']}] {error_response['error_message']}",
            exc_info=True
        )
        raise HTTPException(
            status_code=500,
            detail={
                "error_code": error_response["error_code"],
                "error_message": error_response["error_message"],
                "stack_trace": _extract_core_stack(),
            }
        )


@app.get("/health")
async def health_check(force: bool = False):
    """健康检查 + 模型可用性探测"""
    try:
        result = {
            "status": "ok",
            "message": "Service is running",
            "service_ready": True,
            "model_configured": False,
            "model_available": False,
            "model_probe_code": "NOT_PROBED",
            "model_message": "",
            "data_source_ready": False,
            "data_source_mode": "none",
            "timestamp": datetime.now().isoformat(),
        }

        api_key = os.getenv("OPENAI_API_KEY")
        base_url = os.getenv("OPENAI_BASE_URL")

        workspace_path = os.getenv("WORKSPACE_PATH") or os.getcwd()
        config_path = os.path.join(workspace_path, "config/teaching_assistant_config.json")
        model_name = None
        if os.path.exists(config_path):
            try:
                with open(config_path, "r", encoding="utf-8") as f:
                    cfg = json.load(f)
                model_name = (cfg.get("config") or {}).get("model")
            except Exception:
                pass
        if not model_name:
            model_name = os.getenv("MODEL_NAME")

        configured = bool(api_key and base_url and model_name)
        result["model_configured"] = configured
        if not configured:
            missing = []
            if not api_key: missing.append("api_key")
            if not base_url: missing.append("base_url")
            if not model_name: missing.append("model_name")
            result["model_probe_code"] = "CONFIG_INCOMPLETE"
            result["model_message"] = "模型配置不完整，缺失: " + ", ".join(missing)
            return result

        cache_key = "model_probe"
        now = time.time()
        cached = _health_cache.get(cache_key)
        if cached and not force and (now - cached.get("ts", 0)) < 60:
            result.update({k: v for k, v in cached.items() if k != "ts"})
            return result

        probe_result = await _probe_model(api_key, base_url, model_name)
        result["model_available"] = probe_result["ok"]
        result["model_probe_code"] = probe_result["code"]
        result["model_message"] = probe_result["message"]

        _health_cache[cache_key] = {
            "model_available": result["model_available"],
            "model_probe_code": result["model_probe_code"],
            "model_message": result["model_message"],
            "ts": now,
        }

        mysql_cfg = all(os.getenv(f"XINGYUN_MYSQL_{k}") for k in ("HOST", "PORT", "DATABASE", "USERNAME", "PASSWORD"))
        if mysql_cfg:
            result["data_source_ready"] = True
            result["data_source_mode"] = "mysql"
        else:
            result["data_source_ready"] = False
            result["data_source_mode"] = "none"

        return result
    except Exception as e:
        logger.exception("health_check error")
        return {
            "status": "error",
            "message": str(e),
            "service_ready": True,
            "model_configured": False,
            "model_available": False,
            "model_probe_code": "HEALTH_ERROR",
            "model_message": str(e),
            "data_source_ready": False,
            "data_source_mode": "none",
        }


_health_cache: Dict[str, Any] = {}


async def _probe_model(api_key: str, base_url: str, model_name: str) -> Dict[str, Any]:
    """真实发送一个最小 ping 请求探测模型可用性"""
    try:
        from langchain_openai import ChatOpenAI
        from langchain_core.messages import HumanMessage
        probe_llm = ChatOpenAI(
            model=model_name,
            api_key=api_key,
            base_url=base_url,
            temperature=0,
            streaming=False,
            timeout=30,
            max_tokens=200,
        )
        resp = await probe_llm.ainvoke([HumanMessage(content="ping")])
        content = getattr(resp, "content", None) if resp else None
        if content:
            preview = content[:40].replace("\n", " ")
            return {"ok": True, "code": "OK", "message": f"模型可用，返回: {preview}"}
        return {"ok": True, "code": "OK_NO_CONTENT", "message": "模型调用成功（无文本内容，可能为 reasoning 模型）"}
    except Exception as e:
        msg = f"{type(e).__name__}: {e}"
        code = "MODEL_ERROR"
        msg_lower = str(e).lower()
        if "401" in msg_lower or "authentication" in msg_lower or "api key" in msg_lower:
            code = "AUTH_FAILED"
        elif "model" in msg_lower and ("not found" in msg_lower or "does not exist" in msg_lower):
            code = "MODEL_NOT_FOUND"
        elif "timeout" in msg_lower or "timed out" in msg_lower:
            code = "TIMEOUT"
        elif "connection" in msg_lower or "connect" in msg_lower:
            code = "NETWORK_ERROR"
        return {"ok": False, "code": code, "message": msg}


@app.get(path="/graph_parameter")
async def http_graph_inout_parameter(request: Request):
    return service.graph_inout_schema()


# ============ AI 学习中心前端适配端点 ============

from pydantic import BaseModel as PydanticBaseModel


class LearningAssistantRequest(PydanticBaseModel):
    message: str


@app.post("/learning/assistant")
async def learning_assistant(req: LearningAssistantRequest):
    """AI 助教对话（适配前端 wdd-user-web）"""
    try:
        payload = {
            "messages": [{"role": "user", "content": req.message}],
            "session_id": "ai-learning-assistant"
        }
        result = await service.run(payload)
        content = ""
        if isinstance(result, dict):
            if "messages" in result:
                msgs = result["messages"]
                for msg in reversed(msgs):
                    if hasattr(msg, 'content'):
                        c = msg.content
                        if hasattr(msg, 'type') and msg.type == "ai" and c:
                            content = c
                            break
                    elif isinstance(msg, dict) and msg.get("type") == "ai":
                        content = msg.get("content", "")
                        if content:
                            break
            if not content and "response" in result:
                content = str(result["response"])
        if not content:
            content = "AI 助教已收到您的消息"
        return {"response": {"content": content}}
    except Exception as e:
        logger.error(f"Learning assistant error: {e}", exc_info=True)
        return {"response": {"content": f"AI 助教暂时不可用：{str(e)}"}}


@app.post("/learning/workspace")
async def learning_workspace(request: Request):
    """AI 学习中心数据查询（适配前端 wdd-user-web）"""
    try:
        from tools.mysql_learning_query import MysqlLearningQueryService
        from storage.database.mysql_client import get_mysql_engine

        user_id = 1
        engine = get_mysql_engine()
        svc = MysqlLearningQueryService(engine=engine)
        ctx = svc.get_learning_context(user_id)

        return {
            "code": 200,
            "message": "success",
            "response": {
                "student": {"userName": "demo", "realName": "演示用户"},
                "counts": {
                    "total": ctx["examCount"], "finalized": ctx["finalizedCount"],
                    "validFinalized": ctx["finalizedCount"], "invalidFinalized": 0,
                    "waitingReview": ctx["pendingReviewCount"],
                    "waitingVerification": ctx["pendingVerifyCount"],
                    "verificationFailed": ctx["verifyFailedCount"], "other": 0
                },
                "summary": {
                    "trendSampleCount": len(ctx["recentScores"]),
                    "recentAveragePercent": ctx["recentAverageScorePercent"],
                    "changeFromFirstPercentPoints": None,
                    "volatilityPercentPoints": ctx["scoreStandardDeviationPercent"],
                    "questionAccuracyPercent": ctx["questionAccuracyPercent"],
                    "accuracySampleCount": 0, "finalizedPercent": None
                },
                "trend": [], "recentExams": [], "insights": [],
                "report": {"summary": "", "evidenceExamCount": 0},
                "knowledgePoints": {"available": False, "reasonCode": "NOT_CONFIGURED", "message": "知识点数据未配置"},
                "meta": {"source": "local_mysql", "scope": "ai_agent", "complete": True, "generatedAt": datetime.now().isoformat()}
            }
        }
    except Exception as e:
        return {
            "code": 200, "message": "success",
            "response": {
                "student": {"userName": "demo", "realName": "演示用户"},
                "counts": {"total": 0, "finalized": 0, "validFinalized": 0, "invalidFinalized": 0, "waitingReview": 0, "waitingVerification": 0, "verificationFailed": 0, "other": 0},
                "summary": {"trendSampleCount": 0, "recentAveragePercent": None, "changeFromFirstPercentPoints": None, "volatilityPercentPoints": None, "questionAccuracyPercent": None, "accuracySampleCount": 0, "finalizedPercent": None},
                "trend": [], "recentExams": [], "insights": [],
                "report": {"summary": "", "evidenceExamCount": 0},
                "knowledgePoints": {"available": False, "reasonCode": "NOT_CONFIGURED", "message": "知识点数据未配置"},
                "meta": {"source": "standalone", "scope": "ai_agent", "complete": True, "generatedAt": datetime.now().isoformat()}
            }
        }


def parse_args():
    parser = argparse.ArgumentParser(description="Start FastAPI server")
    parser.add_argument("-m", type=str, default="http", help="Run mode, support http,flow,node")
    parser.add_argument("-n", type=str, default="", help="Node ID for single node run")
    parser.add_argument("-p", type=int, default=5000, help="HTTP server port")
    parser.add_argument("-i", type=str, default="", help="Input JSON string for flow/node mode")
    return parser.parse_args()


def parse_input(input_str: str) -> Dict[str, Any]:
    if not input_str:
        return {"text": "你好"}
    try:
        return json.loads(input_str)
    except json.JSONDecodeError:
        return {"text": input_str}


def start_http_server(port):
    reload = _is_dev_env()
    logger.info(f"Start HTTP Server, Port: {port}")
    uvicorn.run("main:app", host="0.0.0.0", port=port, reload=reload, workers=1)


if __name__ == "__main__":
    args = parse_args()
    if args.m == "http":
        start_http_server(args.p)
    elif args.m == "flow":
        payload = parse_input(args.i)
        result = asyncio.run(service.run(payload))
        print(json.dumps(result, ensure_ascii=False, indent=2))
    elif args.m == "node" and args.n:
        payload = parse_input(args.i)
        result = asyncio.run(service.run_node(args.n, payload))
        print(json.dumps(result, ensure_ascii=False, indent=2))
    elif args.m == "agent":
        ctx = new_context(method="agent")
        for chunk in service.stream(
                {
                    "type": "query",
                    "session_id": "1",
                    "message": "你好",
                    "content": {
                        "query": {
                            "prompt": [
                                {
                                    "type": "text",
                                    "content": {"text": "现在几点了？请调用工具获取当前时间"},
                                }
                            ]
                        }
                    },
                },
                run_config={"configurable": {"session_id": "1"}},
                ctx=ctx,
        ):
            print(chunk)