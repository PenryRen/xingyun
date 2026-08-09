import argparse
import asyncio
import json
import os
import threading
import traceback
import logging
from typing import Any, Dict, Iterable, AsyncIterable, AsyncGenerator, Optional
from urllib.parse import urljoin
import cozeloop
import httpx
import uvicorn
import time
from fastapi import FastAPI, HTTPException, Request
from fastapi.responses import StreamingResponse, JSONResponse
from openai import APIConnectionError, AuthenticationError, NotFoundError, RateLimitError
from langchain_core.messages import HumanMessage
from langchain_core.runnables import RunnableConfig
from langgraph.graph import StateGraph, END
from langgraph.graph.state import CompiledStateGraph
from coze_coding_utils.runtime_ctx.context import new_context, Context
from coze_coding_utils.helper import graph_helper
from coze_coding_utils.log.node_log import LOG_FILE
from coze_coding_utils.log.write_log import setup_logging, request_context
from coze_coding_utils.log.config import LOG_LEVEL
from utils.direct_dns import start_model_host_refresher
from coze_coding_utils.error.classifier import ErrorClassifier, classify_error
from coze_coding_utils.helper.stream_runner import AgentStreamRunner, WorkflowStreamRunner,agent_stream_handler,workflow_stream_handler, RunOpt

setup_logging(
    log_file=LOG_FILE,
    max_bytes=100 * 1024 * 1024, # 100MB
    backup_count=5,
    log_level=LOG_LEVEL,
    use_json_format=True,
    console_output=True
)

logger = logging.getLogger(__name__)
from coze_coding_utils.helper.agent_helper import to_stream_input
from coze_coding_utils.openai.handler import OpenAIChatHandler
from coze_coding_utils.log.parser import LangGraphParser
from coze_coding_utils.log.err_trace import extract_core_stack
from coze_coding_utils.log.loop_trace import init_run_config, init_agent_config


# 超时配置常量
TIMEOUT_SECONDS = 900  # 15分钟

class GraphService:
    def __init__(self):
        # 用于跟踪正在运行的任务（使用asyncio.Task）
        self.running_tasks: Dict[str, asyncio.Task] = {}
        # 错误分类器
        self.error_classifier = ErrorClassifier()
        # stream runner
        self._agent_stream_runner = AgentStreamRunner()
        self._workflow_stream_runner = WorkflowStreamRunner()
        self._graph = None
        self._graph_lock = threading.Lock()

    def _get_graph(self, ctx=Context):
        if graph_helper.is_agent_proj():
            return graph_helper.get_agent_instance("agents.agent", ctx)

        if self._graph is not None:
            return self._graph
        with self._graph_lock:
            if self._graph is not None:
                return self._graph
            self._graph = graph_helper.get_graph_instance("graphs.graph")
            return self._graph

    @staticmethod
    def _sse_event(data: Any, event_id: Any = None) -> str:
        id_line = f"id: {event_id}\n" if event_id else ""
        return f"{id_line}event: message\ndata: {json.dumps(data, ensure_ascii=False, default=str)}\n\n"

    def _get_stream_runner(self):
        if graph_helper.is_agent_proj():
            return self._agent_stream_runner
        else:
            return self._workflow_stream_runner

    # 流式运行（原始迭代器）：本地调用使用
    def stream(self, payload: Dict[str, Any], run_config: RunnableConfig, ctx=Context) -> Iterable[Any]:
        graph = self._get_graph(ctx)
        stream_runner = self._get_stream_runner()
        for chunk in stream_runner.stream(payload, graph, run_config, ctx):
            yield chunk

    # 同步运行：本地/HTTP 通用
    async def run(self, payload: Dict[str, Any], ctx=None) -> Dict[str, Any]:
        if ctx is None:
            ctx = new_context("run")

        run_id = ctx.run_id
        logger.info(f"Starting run with run_id: {run_id}")

        try:
            graph = self._get_graph(ctx)
            # custom tracer
            run_config = init_run_config(graph, ctx)
            run_config["configurable"] = {"thread_id": ctx.run_id}

            # 直接调用，LangGraph会在当前任务上下文中执行
            # 如果当前任务被取消，LangGraph的执行也会被取消
            return await graph.ainvoke(payload, config=run_config, context=ctx)

        except asyncio.CancelledError:
            logger.info(f"Run {run_id} was cancelled")
            return {"status": "cancelled", "run_id": run_id, "message": "Execution was cancelled"}
        except Exception as e:
            # 使用错误分类器分类错误
            err = self.error_classifier.classify(e, {"node_name": "run", "run_id": run_id})
            # 记录详细的错误信息和堆栈跟踪
            logger.error(
                f"Error in GraphService.run: [{err.code}] {err.message}\n"
                f"Category: {err.category.name}\n"
                f"Traceback:\n{extract_core_stack()}"
            )
            # 保留原始异常堆栈，便于上层返回真正的报错位置
            raise
        finally:
            # 清理任务记录
            self.running_tasks.pop(run_id, None)

    # 流式运行（SSE 格式化）：HTTP 路由使用
    async def stream_sse(self, payload: Dict[str, Any], ctx=None, run_opt: Optional[RunOpt] = None) -> AsyncGenerator[str, None]:
        if ctx is None:
            ctx = new_context(method="stream_sse")
        if run_opt is None:
            run_opt = RunOpt()

        run_id = ctx.run_id
        logger.info(f"Starting stream with run_id: {run_id}")
        graph = self._get_graph(ctx)
        if graph_helper.is_agent_proj():
            run_config = init_agent_config(graph, ctx)
        else:
            run_config = init_run_config(graph, ctx)  # vibeflow

        is_workflow = not graph_helper.is_agent_proj()

        try:
            async for chunk in self.astream(payload, graph, run_config=run_config, ctx=ctx, run_opt=run_opt):
                if is_workflow and isinstance(chunk, tuple):
                    event_id, data = chunk
                    yield self._sse_event(data, event_id)
                else:
                    yield self._sse_event(chunk)
        finally:
            # 清理任务记录
            self.running_tasks.pop(run_id, None)
            cozeloop.flush()

    # 取消执行 - 使用asyncio的标准方式
    def cancel_run(self, run_id: str, ctx: Optional[Context] = None) -> Dict[str, Any]:
        """
        取消指定run_id的执行

        使用asyncio.Task.cancel()来取消任务,这是标准的Python异步取消机制。
        LangGraph会在节点之间检查CancelledError,实现优雅的取消。
        """
        logger.info(f"Attempting to cancel run_id: {run_id}")

        # 查找对应的任务
        if run_id in self.running_tasks:
            task = self.running_tasks[run_id]
            if not task.done():
                # 使用asyncio的标准取消机制
                # 这会在下一个await点抛出CancelledError
                task.cancel()
                logger.info(f"Cancellation requested for run_id: {run_id}")
                return {
                    "status": "success",
                    "run_id": run_id,
                    "message": "Cancellation signal sent, task will be cancelled at next await point"
                }
            else:
                logger.info(f"Task already completed for run_id: {run_id}")
                return {
                    "status": "already_completed",
                    "run_id": run_id,
                    "message": "Task has already completed"
                }
        else:
            logger.warning(f"No active task found for run_id: {run_id}")
            return {
                "status": "not_found",
                "run_id": run_id,
                "message": "No active task found with this run_id. Task may have already completed or run_id is invalid."
            }

    # 运行指定节点：本地/HTTP 通用
    async def run_node(self, node_id: str, payload: Dict[str, Any], ctx=None) -> Any:
        if ctx is None or Context.run_id == "":
            ctx = new_context(method="node_run")

        _graph = self._get_graph()
        node_func, input_cls, output_cls = graph_helper.get_graph_node_func_with_inout(_graph.get_graph(), node_id)
        if node_func is None or input_cls is None:
            raise KeyError(f"node_id '{node_id}' not found")

        parser = LangGraphParser(_graph)
        metadata = parser.get_node_metadata(node_id) or {}

        _g = StateGraph(input_cls, input_schema=input_cls, output_schema=output_cls)
        _g.add_node("sn", node_func, metadata=metadata)
        _g.set_entry_point("sn")
        _g.add_edge("sn", END)
        _graph = _g.compile()

        run_config = init_run_config(_graph, ctx)
        return await _graph.ainvoke(payload, config=run_config)

    def graph_inout_schema(self) -> Any:
        if graph_helper.is_agent_proj():
            return {"input_schema": {}, "output_schema": {}}
        builder = getattr(self._get_graph(), 'builder', None)
        if builder is not None:
            input_cls = getattr(builder, 'input_schema', None) or self.graph.get_input_schema()
            output_cls = getattr(builder, 'output_schema', None) or self.graph.get_output_schema()
        else:
            logger.warning(f"No builder input schema found for graph_inout_schema, using graph input schema instead")
            input_cls = self.graph.get_input_schema()
            output_cls = self.graph.get_output_schema()

        return {
            "input_schema": input_cls.model_json_schema(), 
            "output_schema": output_cls.model_json_schema(),
            "code":0,
            "msg":""
        }

    async def astream(self, payload: Dict[str, Any], graph: CompiledStateGraph, run_config: RunnableConfig, ctx=Context, run_opt: Optional[RunOpt] = None) -> AsyncIterable[Any]:
        stream_runner = self._get_stream_runner()
        async for chunk in stream_runner.astream(payload, graph, run_config, ctx, run_opt):
            yield chunk


service = GraphService()
app = FastAPI()
start_model_host_refresher()

_model_probe_cache: Dict[str, Any] = {
    "checked_at": 0.0,
    "available": False,
    "code": "NOT_CHECKED",
    "message": "尚未检测模型连接",
}
_model_probe_lock = asyncio.Lock()


def _model_configured() -> bool:
    return bool(
        os.getenv("COZE_WORKLOAD_IDENTITY_API_KEY")
        and os.getenv("COZE_INTEGRATION_MODEL_BASE_URL")
    )


def _configured_model_name() -> str:
    workspace_path = os.getenv("COZE_WORKSPACE_PATH", "/workspace/projects")
    config_path = os.path.join(workspace_path, "config", "teaching_assistant_config.json")
    try:
        with open(config_path, "r", encoding="utf-8") as config_file:
            return str(json.load(config_file).get("config", {}).get("model") or "").strip()
    except Exception:
        return ""


def _cache_model_probe(available: bool, code: str, message: str) -> Dict[str, Any]:
    _model_probe_cache.update({
        "checked_at": time.monotonic(),
        "available": available,
        "code": code,
        "message": message,
    })
    return dict(_model_probe_cache)


async def _probe_model_availability(force: bool = False) -> Dict[str, Any]:
    if not _model_configured():
        return _cache_model_probe(False, "MODEL_NOT_CONFIGURED", "模型 API 尚未配置")
    if not force and time.monotonic() - float(_model_probe_cache["checked_at"]) < 30:
        return dict(_model_probe_cache)

    async with _model_probe_lock:
        if not force and time.monotonic() - float(_model_probe_cache["checked_at"]) < 30:
            return dict(_model_probe_cache)
        base_url = str(os.getenv("COZE_INTEGRATION_MODEL_BASE_URL") or "").rstrip("/") + "/"
        models_url = urljoin(base_url, "models")
        expected_model = _configured_model_name()
        headers = {"Authorization": f"Bearer {os.getenv('COZE_WORKLOAD_IDENTITY_API_KEY')}"}
        try:
            async with httpx.AsyncClient(timeout=10, trust_env=False) as client:
                response = await client.get(models_url, headers=headers)
            if response.status_code in {401, 403}:
                return _cache_model_probe(False, "MODEL_AUTHENTICATION_FAILED", "模型鉴权失败，请检查本地 API 配置")
            response.raise_for_status()
            payload = response.json()
            model_ids = {str(item.get("id")) for item in payload.get("data", []) if isinstance(item, dict)}
            if expected_model and expected_model not in model_ids:
                return _cache_model_probe(False, "MODEL_NOT_FOUND", "已配置的模型不可用，请检查模型名称")
            return _cache_model_probe(True, "OK", "模型调用可用")
        except httpx.RequestError:
            return _cache_model_probe(False, "MODEL_NETWORK_UNAVAILABLE", "Agent 在线，但模型网络不可用，请检查本地网络代理后重试")
        except (ValueError, httpx.HTTPStatusError):
            return _cache_model_probe(False, "MODEL_PROBE_FAILED", "模型连接检测失败，请稍后重试")

# OpenAI 兼容接口处理器
openai_handler = OpenAIChatHandler(service)


async def _invoke_education_agent(message: str, session_id: str, headers, method: str) -> str:
    """直接调用教育 Agent，避免旧运行时对 agent/graph 项目类型的误判。"""
    ctx = new_context(method=method, headers=headers)
    request_context.set(ctx)
    agent = graph_helper.get_agent_instance("agents.agent", ctx)
    run_config = init_agent_config(agent, ctx)
    run_config["configurable"] = {"thread_id": session_id}
    result = await agent.ainvoke(
        {"messages": [HumanMessage(content=message)]},
        config=run_config,
        context=ctx,
    )
    messages = result.get("messages", []) if isinstance(result, dict) else []
    return messages[-1].content if messages else "处理完成"


@app.post("/run")
async def http_run(request: Request) -> Dict[str, Any]:
    global result
    raw_body = await request.body()
    try:
        body_text = raw_body.decode("utf-8")
    except Exception as e:
        body_text = str(raw_body)
        raise HTTPException(status_code=400,
                            detail=f"Invalid JSON format: {body_text}, traceback: {traceback.format_exc()}, error: {e}")

    ctx = new_context(method="run", headers=request.headers)
    run_id = ctx.run_id
    request_context.set(ctx)

    logger.info(
        f"Received request for /run: "
        f"run_id={run_id}, "
        f"query={dict(request.query_params)}, "
        f"body={body_text}"
    )

    try:
        payload = await request.json()

        # 创建任务并记录 - 这是关键，让我们可以通过run_id取消任务
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
        logger.error(f"JSON decode error in http_run: {e}, traceback: {traceback.format_exc()}")
        raise HTTPException(status_code=400, detail=f"Invalid JSON format, {extract_core_stack()}")

    except asyncio.CancelledError:
        logger.info(f"Request cancelled for run_id: {run_id}")
        result = {"status": "cancelled", "run_id": run_id, "message": "Execution was cancelled"}
        return result

    except Exception as e:
        # 使用错误分类器获取错误信息
        error_response = service.error_classifier.get_error_response(e, {"node_name": "http_run", "run_id": run_id})
        logger.error(
            f"Unexpected error in http_run: [{error_response['error_code']}] {error_response['error_message']}, "
            f"traceback: {traceback.format_exc()}", exc_info=True
        )
        raise HTTPException(
            status_code=500,
            detail={
                "error_code": error_response["error_code"],
                "error_message": error_response["error_message"],
                "stack_trace": extract_core_stack(),
            }
        )
    finally:
        cozeloop.flush()


HEADER_X_WORKFLOW_STREAM_MODE = "x-workflow-stream-mode"


def _register_task(run_id: str, task: asyncio.Task):
    service.running_tasks[run_id] = task


@app.post("/stream_run")
async def http_stream_run(request: Request):
    ctx = new_context(method="stream_run", headers=request.headers)
    workflow_stream_mode = request.headers.get(HEADER_X_WORKFLOW_STREAM_MODE, "").lower()
    workflow_debug = workflow_stream_mode == "debug"
    request_context.set(ctx)
    raw_body = await request.body()
    try:
        body_text = raw_body.decode("utf-8")
    except Exception as e:
        body_text = str(raw_body)
        raise HTTPException(status_code=400,
                            detail=f"Invalid JSON format: {body_text}, traceback: {extract_core_stack()}, error: {e}")
    run_id = ctx.run_id
    is_agent = graph_helper.is_agent_proj()
    logger.info(
        f"Received request for /stream_run: "
        f"run_id={run_id}, "
        f"is_agent_project={is_agent}, "
        f"query={dict(request.query_params)}, "
        f"body={body_text}"
    )
    try:
        payload = await request.json()
    except json.JSONDecodeError as e:
        logger.error(f"JSON decode error in http_stream_run: {e}, traceback: {traceback.format_exc()}")
        raise HTTPException(status_code=400, detail=f"Invalid JSON format:{extract_core_stack()}")

    if is_agent:
        stream_generator = agent_stream_handler(
            payload=payload,
            ctx=ctx,
            run_id=run_id,
            stream_sse_func=service.stream_sse,
            sse_event_func=service._sse_event,
            error_classifier=service.error_classifier,
            register_task_func=_register_task,
        )
    else:
        stream_generator = workflow_stream_handler(
            payload=payload,
            ctx=ctx,
            run_id=run_id,
            stream_sse_func=service.stream_sse,
            sse_event_func=service._sse_event,
            error_classifier=service.error_classifier,
            register_task_func=_register_task,
            run_opt=RunOpt(workflow_debug=workflow_debug),
        )

    response = StreamingResponse(stream_generator, media_type="text/event-stream")
    return response

@app.post("/cancel/{run_id}")
async def http_cancel(run_id: str, request: Request):
    """
    取消指定run_id的执行

    使用asyncio.Task.cancel()实现取消,这是Python标准的异步任务取消机制。
    LangGraph会在节点之间的await点检查CancelledError,实现优雅取消。
    """
    ctx = new_context(method="cancel", headers=request.headers)
    request_context.set(ctx)
    logger.info(f"Received cancel request for run_id: {run_id}")
    result = service.cancel_run(run_id, ctx)
    return result


@app.post(path="/node_run/{node_id}")
async def http_node_run(node_id: str, request: Request):
    raw_body = await request.body()
    try:
        body_text = raw_body.decode("utf-8")
    except UnicodeDecodeError:
        body_text = str(raw_body)
        raise HTTPException(status_code=400, detail=f"Invalid JSON format: {body_text}")
    ctx = new_context(method="node_run", headers=request.headers)
    request_context.set(ctx)
    logger.info(
        f"Received request for /node_run/{node_id}: "
        f"query={dict(request.query_params)}, "
        f"body={body_text}",
    )

    try:
        payload = await request.json()
    except json.JSONDecodeError as e:
        logger.error(f"JSON decode error in http_node_run: {e}, traceback: {traceback.format_exc()}")
        raise HTTPException(status_code=400, detail=f"Invalid JSON format:{extract_core_stack()}")
    try:
        return await service.run_node(node_id, payload, ctx)
    except KeyError:
        raise HTTPException(status_code=404,
                            detail=f"node_id '{node_id}' not found or input miss required fields, traceback: {extract_core_stack()}")
    except Exception as e:
        # 使用错误分类器获取错误信息
        error_response = service.error_classifier.get_error_response(e, {"node_name": node_id})
        logger.error(
            f"Unexpected error in http_node_run: [{error_response['error_code']}] {error_response['error_message']}, "
            f"traceback: {traceback.format_exc()}", exc_info=True
        )
        raise HTTPException(
            status_code=500,
            detail={
                "error_code": error_response["error_code"],
                "error_message": error_response["error_message"],
                "stack_trace": extract_core_stack(),
            }
        )
    finally:
        cozeloop.flush()


@app.post("/v1/chat/completions")
async def openai_chat_completions(request: Request):
    """OpenAI Chat Completions 兼容接口（当前仅支持非流式文本消息）。"""
    try:
        payload = await request.json()
        if payload.get("stream"):
            raise HTTPException(status_code=400, detail="stream=true is not supported by this endpoint")
        messages = payload.get("messages") or []
        user_messages = [item for item in messages if item.get("role") == "user"]
        if not user_messages:
            raise HTTPException(status_code=400, detail="a user message is required")
        raw_content = user_messages[-1].get("content", "")
        if isinstance(raw_content, list):
            message = "\n".join(str(item.get("text", "")) for item in raw_content if item.get("type") == "text")
        else:
            message = str(raw_content)
        session_id = str(payload.get("session_id") or f"openai-{int(time.time())}")
        content = await _invoke_education_agent(message, session_id, request.headers, "openai_chat")
        return {
            "id": f"chatcmpl-{int(time.time() * 1000)}",
            "object": "chat.completion",
            "created": int(time.time()),
            "model": payload.get("model") or "xingyun-education-agent",
            "choices": [{"index": 0, "message": {"role": "assistant", "content": content}, "finish_reason": "stop"}],
        }
    except json.JSONDecodeError as e:
        logger.error(f"JSON decode error in openai_chat_completions: {e}")
        raise HTTPException(status_code=400, detail="Invalid JSON format")
    finally:
        cozeloop.flush()


@app.get("/api/v1/agents")
async def list_education_agents():
    """返回前端可视化所使用的教育智能体能力清单。"""
    return {
        "success": True,
        "agents": [
            {"type": "exam_analysis", "name": "考试分析", "capabilities": ["成绩趋势", "错题统计", "薄弱项定位"]},
            {"type": "learning_profile", "name": "学情画像", "capabilities": ["学习进度", "能力维度", "行动建议"]},
            {"type": "learning_assessment", "name": "智能组卷", "capabilities": ["强化练习", "难度适配", "针对性训练"]},
            {"type": "teaching_assistant", "name": "办学助手", "capabilities": ["概念解释", "学习答疑", "实践指导"]},
        ],
    }


@app.post("/api/v1/chat")
async def education_chat(request: Request):
    """供星云学生端调用的简洁聊天接口，绕过旧版 OpenAI handler 的项目类型误判。"""
    body = await request.json()
    message = str(body.get("message") or "").strip()
    session_id = str(body.get("session_id") or "default")
    if not message:
        raise HTTPException(status_code=400, detail="message is required")

    try:
        content = await _invoke_education_agent(message, session_id, request.headers, "education_chat")
        _cache_model_probe(True, "OK", "模型调用可用")
        return {"success": True, "message": content, "session_id": session_id}
    except APIConnectionError:
        _cache_model_probe(False, "MODEL_NETWORK_UNAVAILABLE", "Agent 在线，但模型网络不可用，请检查本地网络代理后重试")
        logger.warning("Education chat failed: MODEL_NETWORK_UNAVAILABLE", exc_info=True)
        raise HTTPException(status_code=503, detail={
            "code": "MODEL_NETWORK_UNAVAILABLE",
            "message": "Agent 在线，但模型网络不可用，请检查本地网络代理后重试",
        })
    except AuthenticationError:
        _cache_model_probe(False, "MODEL_AUTHENTICATION_FAILED", "模型鉴权失败，请检查本地 API 配置")
        logger.warning("Education chat failed: MODEL_AUTHENTICATION_FAILED")
        raise HTTPException(status_code=401, detail={
            "code": "MODEL_AUTHENTICATION_FAILED",
            "message": "模型鉴权失败，请检查本地 API 配置",
        })
    except RateLimitError:
        logger.warning("Education chat failed: MODEL_RATE_LIMITED")
        raise HTTPException(status_code=429, detail={
            "code": "MODEL_RATE_LIMITED",
            "message": "模型请求过于频繁，请稍后重试",
        })
    except NotFoundError:
        _cache_model_probe(False, "MODEL_NOT_FOUND", "已配置的模型不可用，请检查模型名称")
        logger.warning("Education chat failed: MODEL_NOT_FOUND")
        raise HTTPException(status_code=502, detail={
            "code": "MODEL_NOT_FOUND",
            "message": "已配置的模型不可用，请检查模型名称",
        })
    except Exception:
        logger.error("Education chat failed: MODEL_CALL_FAILED", exc_info=True)
        raise HTTPException(status_code=500, detail={
            "code": "MODEL_CALL_FAILED",
            "message": "模型调用失败，请稍后重试",
        })
    finally:
        cozeloop.flush()


@app.get("/health")
async def health_check(force: bool = False):
    try:
        model_configured = _model_configured()
        probe = await _probe_model_availability(force=force)
        data_source_ready = bool(
            os.getenv("COZE_SUPABASE_URL")
            and os.getenv("COZE_SUPABASE_ANON_KEY")
        )
        return {
            "status": "ok" if probe["available"] else "degraded",
            "message": "Agent 服务已启动，模型调用可用" if probe["available"] else "Agent 服务已启动，但模型调用不可用",
            "service_ready": True,
            "model_configured": model_configured,
            "model_available": bool(probe["available"]),
            "model_ready": bool(probe["available"]),
            "model_probe_code": probe["code"],
            "model_message": probe["message"],
            "data_source_ready": data_source_ready,
        }
    except Exception:
        logger.error("Health model probe failed", exc_info=True)
        raise HTTPException(status_code=503, detail={"code": "MODEL_PROBE_FAILED", "message": "模型连接检测失败"})


@app.get("/live")
async def liveness_check():
    return {"status": "ok", "service_ready": True}


@app.get("/ready")
async def readiness_check():
    probe = await _probe_model_availability(force=False)
    if not probe["available"]:
        raise HTTPException(status_code=503, detail={"code": probe["code"], "message": probe["message"]})
    return {"status": "ok", "model_ready": True, "model_available": True}


@app.get(path="/graph_parameter")
async def http_graph_inout_parameter(request: Request):
    return service.graph_inout_schema()

def parse_args():
    parser = argparse.ArgumentParser(description="Start FastAPI server")
    parser.add_argument("-m", type=str, default="http", help="Run mode, support http,flow,node")
    parser.add_argument("-n", type=str, default="", help="Node ID for single node run")
    parser.add_argument("-p", type=int, default=5000, help="HTTP server port")
    parser.add_argument("-i", type=str, default="", help="Input JSON string for flow/node mode")
    return parser.parse_args()


def parse_input(input_str: str) -> Dict[str, Any]:
    """Parse input string, support both JSON string and plain text"""
    if not input_str:
        return {"text": "你好"}

    # Try to parse as JSON first
    try:
        return json.loads(input_str)
    except json.JSONDecodeError:
        # If not valid JSON, treat as plain text
        return {"text": input_str}

def start_http_server(port):
    workers = 1
    reload = False
    if graph_helper.is_dev_env():
        reload = True

    logger.info(f"Start HTTP Server, Port: {port}, Workers: {workers}")
    uvicorn.run("main:app", host="0.0.0.0", port=port, reload=reload, workers=workers)

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
        agent_ctx = new_context(method="agent")
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
                ctx=agent_ctx,
        ):
            print(chunk)
