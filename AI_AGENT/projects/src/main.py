import argparse
import asyncio
import json
import threading
import traceback
import logging
from typing import Any, Dict, Optional
import uvicorn
from fastapi import FastAPI, HTTPException, Request
from fastapi.responses import StreamingResponse

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler("agent.log", encoding='utf-8'),
        logging.StreamHandler()
    ]
)

logger = logging.getLogger(__name__)


# 超时配置常量
TIMEOUT_SECONDS = 900  # 15 分钟

class GraphService:
    def __init__(self):
        # 用于跟踪正在运行的任务（使用 asyncio.Task）
        self.running_tasks: Dict[str, asyncio.Task] = {}
        # graph 实例缓存
        self._graph = None
        self._graph_lock = threading.Lock()

    def _get_graph(self):
        """获取图实例"""
        if self._graph is not None:
            return self._graph
        with self._graph_lock:
            if self._graph is not None:
                return self._graph
            # 导入并初始化图
            from graphs.learning_workflow import LearningSupervisorWorkflow
            self._graph = LearningSupervisorWorkflow(use_local=True)  # 默认使用本地模型
            return self._graph

    @staticmethod
    def _sse_event(data: Any, event_id: Any = None) -> str:
        """生成 SSE 事件"""
        event_parts = []
        if event_id is not None:
            event_parts.append(f"id: {event_id}")
        event_parts.append(f"data: {json.dumps(data, ensure_ascii=False)}")
        event_parts.append("")  # 空行表示事件结束
        return "\n".join(event_parts)

    # 普通运行：同步返回结果
    async def run(self, payload: Dict[str, Any], thread_id: str = "default") -> Dict[str, Any]:
        """运行工作流"""
        logger.info(f"Starting run with thread_id: {thread_id}")
        graph = self._get_graph()
        
        try:
            # 调用工作流
            result = graph.invoke(
                user_input=payload.get("text", "你好"),
                thread_id=thread_id
            )
            
            if not result:
                result = {}
            if isinstance(result, dict):
                result["thread_id"] = thread_id
            return result
            
        except Exception as e:
            logger.error(
                f"Error in GraphService.run: {str(e)}\n"
                f"Traceback:\n{traceback.format_exc()}"
            )
            raise

    # 流式运行（SSE 格式化）：HTTP 路由使用
    async def stream_sse(self, payload: Dict[str, Any], thread_id: str = "default"):
        """流式输出"""
        logger.info(f"Starting stream with thread_id: {thread_id}")
        graph = self._get_graph()
        
        try:
            # 调用工作流
            result = graph.invoke(
                user_input=payload.get("text", "你好"),
                thread_id=thread_id
            )
            
            # 发送 SSE 事件
            yield self._sse_event({
                "status": "success",
                "output": result
            })
            
        except Exception as e:
            logger.error(f"Error in stream_sse: {str(e)}")
            yield self._sse_event({
                "status": "error",
                "message": str(e)
            })

    # 取消执行 - 使用 asyncio 的标准方式
    def cancel_run(self, thread_id: str) -> Dict[str, Any]:
        """取消指定 thread_id 的执行"""
        logger.info(f"Attempting to cancel thread_id: {thread_id}")

        # 查找对应的任务
        if thread_id in self.running_tasks:
            task = self.running_tasks[thread_id]
            if not task.done():
                task.cancel()
                logger.info(f"Cancellation requested for thread_id: {thread_id}")
                return {
                    "status": "success",
                    "thread_id": thread_id,
                    "message": "Cancellation signal sent"
                }
            else:
                logger.info(f"Task already completed for thread_id: {thread_id}")
                return {
                    "status": "success",
                    "thread_id": thread_id,
                    "message": "Task already completed"
                }
        else:
            logger.warning(f"No running task found for thread_id: {thread_id}")
            return {
                "status": "error",
                "thread_id": thread_id,
                "message": "No running task found"
            }


# 初始化服务
service = GraphService()

# 初始化 FastAPI 应用
app = FastAPI()


@app.post("/run")
async def http_run(request: Request):
    """执行工作流接口"""
    thread_id = request.query_params.get("thread_id", "default")
    
    logger.info(
        f"Received request for /run: "
        f"thread_id={thread_id}, "
        f"query={dict(request.query_params)}"
    )

    try:
        payload = await request.json()

        # 创建任务并记录
        task = asyncio.create_task(service.run(payload, thread_id))
        service.running_tasks[thread_id] = task

        try:
            result = await asyncio.wait_for(task, timeout=float(TIMEOUT_SECONDS))
        except asyncio.TimeoutError:
            logger.error(f"Run execution timeout after {TIMEOUT_SECONDS}s for thread_id: {thread_id}")
            task.cancel()
            try:
                result = await task
            except asyncio.CancelledError:
                return {
                    "status": "timeout",
                    "thread_id": thread_id,
                    "message": f"Execution timeout: exceeded {TIMEOUT_SECONDS} seconds"
                }

        if not result:
            result = {}
        if isinstance(result, dict):
            result["thread_id"] = thread_id
        return result

    except json.JSONDecodeError as e:
        logger.error(f"JSON decode error in http_run: {e}")
        raise HTTPException(status_code=400, detail=f"Invalid JSON format")

    except asyncio.CancelledError:
        logger.info(f"Request cancelled for thread_id: {thread_id}")
        return {"status": "cancelled", "thread_id": thread_id, "message": "Execution was cancelled"}

    except Exception as e:
        logger.error(f"Error in http_run: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))


@app.post("/stream_run")
async def http_stream_run(request: Request):
    """流式执行工作流接口"""
    thread_id = request.query_params.get("thread_id", "default")
    
    logger.info(
        f"Received request for /stream_run: "
        f"thread_id={thread_id}, "
        f"query={dict(request.query_params)}"
    )
    
    try:
        payload = await request.json()
    except json.JSONDecodeError as e:
        logger.error(f"JSON decode error in http_stream_run: {e}")
        raise HTTPException(status_code=400, detail="Invalid JSON format")

    stream_generator = service.stream_sse(payload, thread_id)
    response = StreamingResponse(stream_generator, media_type="text/event-stream")
    return response


@app.post("/cancel/{thread_id}")
async def http_cancel(thread_id: str, request: Request):
    """取消执行接口"""
    logger.info(f"Received cancel request for thread_id: {thread_id}")
    result = service.cancel_run(thread_id)
    return result


@app.get("/health")
async def health_check():
    """健康检查接口"""
    try:
        return {
            "status": "ok",
            "message": "Service is running",
        }
    except Exception as e:
        raise HTTPException(status_code=503, detail=str(e))


def parse_args():
    parser = argparse.ArgumentParser(description="Start FastAPI server")
    parser.add_argument("-m", type=str, default="http", help="Run mode: http,flow")
    parser.add_argument("-p", type=int, default=5000, help="HTTP server port")
    parser.add_argument("-i", type=str, default="", help="Input JSON string for flow mode")
    parser.add_argument("-t", type=str, default="default", help="Thread ID")
    return parser.parse_args()


async def run_flow(input_str, thread_id):
    """运行工作流"""
    try:
        payload = json.loads(input_str)
    except json.JSONDecodeError:
        payload = {"text": input_str}
    
    result = await service.run(payload, thread_id)
    logger.info(f"Flow result: {result}")
    print("\n" + "="*50)
    print("工作流执行结果:")
    print(json.dumps(result, ensure_ascii=False, indent=2))
    print("="*50 + "\n")


if __name__ == "__main__":
    args = parse_args()
    
    if args.m == "flow":
        # 直接运行工作流
        if not args.i:
            print("Error: -i parameter is required for flow mode")
            exit(1)
        
        asyncio.run(run_flow(args.i, args.t))
    else:
        # 启动 HTTP 服务器
        logger.info(f"Start HTTP Server, Port: {args.p}, Workers: 1")
        uvicorn.run(
            "main:app",
            host="0.0.0.0",
            port=args.p,
            workers=1,
            reload=False
        )
