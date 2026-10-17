"""
AI Agent API Server

提供RESTful API接口供后端系统调用智能体功能
基于FastAPI框架实现
"""

import os
import sys
import json
import asyncio
import logging
from datetime import datetime
from typing import AsyncGenerator, Optional
from contextlib import asynccontextmanager

# 添加src到Python路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))

from fastapi import FastAPI, HTTPException, Request
from fastapi.responses import StreamingResponse, JSONResponse
from fastapi.middleware.cors import CORSMiddleware
import uvicorn
from langchain_core.messages import HumanMessage, AIMessage

# 导入智能体模块
from agents.exam_analysis_agent import build_exam_analysis_agent
from agents.learning_profile_agent import build_learning_profile_agent
from agents.learning_assessment_agent import build_learning_assessment_agent
from agents.teaching_assistant_agent import build_teaching_assistant_agent
from agents.agent import get_agent_by_request
from graphs import create_learning_workflow

# 导入数据模型
from server.models import (
    ChatRequest,
    ChatResponse,
    WorkflowRequest,
    WorkflowResponse,
    AgentType,
    AgentListResponse,
    HealthResponse,
    ErrorResponse,
    StreamChunk
)

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# 全局Agent缓存
_agent_cache = {}
_workflow_cache = {}


def _get_agent(agent_type: AgentType, use_local: bool = False):
    """获取或创建Agent实例（带缓存）"""
    cache_key = f"{agent_type.value}_{use_local}"
    
    if cache_key not in _agent_cache:
        logger.info(f"创建Agent实例: {agent_type.value}, use_local={use_local}")
        
        if agent_type == AgentType.EXAM_ANALYSIS:
            _agent_cache[cache_key] = build_exam_analysis_agent(use_local=use_local)
        elif agent_type == AgentType.LEARNING_PROFILE:
            _agent_cache[cache_key] = build_learning_profile_agent(use_local=use_local)
        elif agent_type == AgentType.LEARNING_ASSESSMENT:
            _agent_cache[cache_key] = build_learning_assessment_agent(use_local=use_local)
        elif agent_type == AgentType.TEACHING_ASSISTANT:
            _agent_cache[cache_key] = build_teaching_assistant_agent(use_local=use_local)
        else:
            raise ValueError(f"未知的Agent类型: {agent_type}")
    
    return _agent_cache[cache_key]


def _get_workflow(use_local: bool = False):
    """获取或创建工作流实例（带缓存）"""
    cache_key = f"workflow_{use_local}"
    
    if cache_key not in _workflow_cache:
        logger.info(f"创建工作流实例: use_local={use_local}")
        _workflow_cache[cache_key] = create_learning_workflow(use_local=use_local)
    
    return _workflow_cache[cache_key]


def _get_timestamp() -> str:
    """获取ISO格式时间戳"""
    return datetime.utcnow().isoformat() + "Z"


# FastAPI应用实例
@asynccontextmanager
async def lifespan(app: FastAPI):
    """应用生命周期管理"""
    logger.info("AI Agent API Server 启动中...")
    yield
    logger.info("AI Agent API Server 关闭中...")
    # 清理缓存
    _agent_cache.clear()
    _workflow_cache.clear()


app = FastAPI(
    title="AI Agent API",
    description="麒麟系统教育平台智能体API接口",
    version="1.0.0",
    lifespan=lifespan
)

# 配置CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.exception_handler(Exception)
async def global_exception_handler(request: Request, exc: Exception):
    """全局异常处理"""
    logger.error(f"未处理的异常: {exc}", exc_info=True)
    return JSONResponse(
        status_code=500,
        content=ErrorResponse(
            error_code="INTERNAL_ERROR",
            error_message=str(exc),
            timestamp=_get_timestamp()
        ).dict()
    )


@app.get("/", response_model=HealthResponse)
async def root():
    """根路径 - 返回服务信息"""
    return HealthResponse(
        status="ok",
        message="AI Agent API Server 运行中",
        version="1.0.0",
        timestamp=_get_timestamp()
    )


@app.get("/health", response_model=HealthResponse)
async def health_check():
    """健康检查接口"""
    return HealthResponse(
        status="ok",
        message="服务正常运行",
        version="1.0.0",
        timestamp=_get_timestamp()
    )


@app.get("/api/v1/agents", response_model=AgentListResponse)
async def list_agents():
    """
    获取可用的智能体列表
    
    返回所有可用的智能体类型及其描述
    """
    agents = [
        {
            "type": AgentType.EXAM_ANALYSIS.value,
            "name": "考试分析",
            "description": "分析试卷、识别薄弱点、统计错误率",
            "capabilities": ["试卷分析", "薄弱点识别", "错误率统计", "生成分析报告"]
        },
        {
            "type": AgentType.LEARNING_PROFILE.value,
            "name": "学情分析",
            "description": "维护学生学情档案、跟踪学习进度",
            "capabilities": ["创建档案", "更新学情", "查看进度", "知识点掌握分析"]
        },
        {
            "type": AgentType.LEARNING_ASSESSMENT.value,
            "name": "学情检测",
            "description": "智能组卷、生成测试卷、强化训练",
            "capabilities": ["智能组卷", "强化测试", "难度适配", "题库管理"]
        },
        {
            "type": AgentType.TEACHING_ASSISTANT.value,
            "name": "办学助手",
            "description": "解答学习问题、解释概念、提供学习建议",
            "capabilities": ["问题解答", "概念解释", "实践指导", "学习建议"]
        },
        {
            "type": AgentType.AUTO.value,
            "name": "自动选择",
            "description": "根据用户输入自动选择合适的智能体",
            "capabilities": ["意图识别", "智能路由", "多轮对话"]
        }
    ]
    
    return AgentListResponse(
        success=True,
        agents=agents,
        timestamp=_get_timestamp()
    )


@app.post("/api/v1/chat", response_model=ChatResponse)
async def chat(request: ChatRequest):
    """
    与智能体对话（非流式）
    
    根据指定的agent_type调用对应的智能体，或自动选择合适的智能体
    """
    try:
        logger.info(f"收到聊天请求: session_id={request.session_id}, agent_type={request.agent_type}")
        
        # 确定使用哪个Agent
        if request.agent_type == AgentType.AUTO:
            # 自动选择Agent
            agent = get_agent_by_request(request.message, use_local=request.use_local)
            actual_agent_type = AgentType.TEACHING_ASSISTANT  # 默认类型
            
            # 根据返回的agent判断类型
            agent_str = str(type(agent))
            if "exam_analysis" in agent_str:
                actual_agent_type = AgentType.EXAM_ANALYSIS
            elif "learning_profile" in agent_str:
                actual_agent_type = AgentType.LEARNING_PROFILE
            elif "learning_assessment" in agent_str:
                actual_agent_type = AgentType.LEARNING_ASSESSMENT
            else:
                actual_agent_type = AgentType.TEACHING_ASSISTANT
        else:
            # 使用指定类型的Agent
            agent = _get_agent(request.agent_type, request.use_local)
            actual_agent_type = request.agent_type
        
        # 调用Agent
        result = agent.invoke({
            "messages": [HumanMessage(content=request.message)]
        })
        
        # 提取响应消息
        messages = result.get("messages", [])
        if messages:
            last_message = messages[-1]
            response_content = last_message.content if hasattr(last_message, 'content') else str(last_message)
        else:
            response_content = "处理完成"
        
        # 提取结构化数据（如果有）
        data = None
        if isinstance(response_content, str):
            try:
                # 尝试解析JSON
                if response_content.strip().startswith('{') or response_content.strip().startswith('['):
                    data = json.loads(response_content)
            except:
                pass
        
        return ChatResponse(
            success=True,
            message=response_content,
            agent_type=actual_agent_type,
            session_id=request.session_id,
            timestamp=_get_timestamp(),
            data=data
        )
        
    except Exception as e:
        logger.error(f"聊天请求处理失败: {e}", exc_info=True)
        raise HTTPException(
            status_code=500,
            detail=ErrorResponse(
                error_code="CHAT_ERROR",
                error_message=f"处理请求时发生错误: {str(e)}",
                timestamp=_get_timestamp()
            ).dict()
        )


@app.post("/api/v1/chat/stream")
async def chat_stream(request: ChatRequest):
    """
    与智能体对话（流式）
    
    使用SSE（Server-Sent Events）返回流式响应
    """
    async def generate_stream() -> AsyncGenerator[str, None]:
        try:
            # 确定使用哪个Agent
            if request.agent_type == AgentType.AUTO:
                agent = get_agent_by_request(request.message, use_local=request.use_local)
                actual_agent_type = AgentType.TEACHING_ASSISTANT
            else:
                agent = _get_agent(request.agent_type, request.use_local)
                actual_agent_type = request.agent_type
            
            # 发送开始事件
            start_chunk = StreamChunk(
                type="message",
                content="",
                agent_type=actual_agent_type,
                timestamp=_get_timestamp()
            )
            yield f"data: {json.dumps(start_chunk.dict(), ensure_ascii=False)}\n\n"
            
            # 调用Agent的流式接口
            full_response = ""
            for chunk in agent.stream({
                "messages": [HumanMessage(content=request.message)]
            }):
                if isinstance(chunk, dict) and "messages" in chunk:
                    messages = chunk["messages"]
                    if messages:
                        last_msg = messages[-1]
                        if hasattr(last_msg, 'content'):
                            content = last_msg.content
                            if content != full_response:
                                new_content = content[len(full_response):]
                                full_response = content
                                
                                stream_chunk = StreamChunk(
                                    type="message",
                                    content=new_content,
                                    agent_type=actual_agent_type,
                                    timestamp=_get_timestamp()
                                )
                                yield f"data: {json.dumps(stream_chunk.dict(), ensure_ascii=False)}\n\n"
            
            # 发送结束事件
            end_chunk = StreamChunk(
                type="end",
                content=full_response,
                agent_type=actual_agent_type,
                timestamp=_get_timestamp()
            )
            yield f"data: {json.dumps(end_chunk.dict(), ensure_ascii=False)}\n\n"
            
        except Exception as e:
            logger.error(f"流式聊天请求处理失败: {e}", exc_info=True)
            error_chunk = StreamChunk(
                type="error",
                content=str(e),
                timestamp=_get_timestamp()
            )
            yield f"data: {json.dumps(error_chunk.dict(), ensure_ascii=False)}\n\n"
    
    return StreamingResponse(
        generate_stream(),
        media_type="text/event-stream",
        headers={
            "Cache-Control": "no-cache",
            "Connection": "keep-alive",
        }
    )


@app.post("/api/v1/workflow", response_model=WorkflowResponse)
async def workflow(request: WorkflowRequest):
    """
    调用主管架构工作流（非流式）
    
    主管会自动分析用户意图并路由到合适的子Agent
    """
    try:
        logger.info(f"收到工作流请求: session_id={request.session_id}")
        
        # 获取工作流实例
        workflow = _get_workflow(request.use_local)
        
        # 调用工作流
        result = workflow.invoke(request.message, thread_id=request.session_id)
        
        # 尝试解析结果
        data = None
        if isinstance(result, str):
            try:
                if result.strip().startswith('{') or result.strip().startswith('['):
                    data = json.loads(result)
            except:
                pass
        
        return WorkflowResponse(
            success=True,
            message=result if isinstance(result, str) else str(result),
            selected_agent=AgentType.AUTO,  # 工作流内部决定
            session_id=request.session_id,
            timestamp=_get_timestamp(),
            data=data
        )
        
    except Exception as e:
        logger.error(f"工作流请求处理失败: {e}", exc_info=True)
        raise HTTPException(
            status_code=500,
            detail=ErrorResponse(
                error_code="WORKFLOW_ERROR",
                error_message=f"处理请求时发生错误: {str(e)}",
                timestamp=_get_timestamp()
            ).dict()
        )


@app.post("/api/v1/workflow/stream")
async def workflow_stream(request: WorkflowRequest):
    """
    调用主管架构工作流（流式）
    
    使用SSE返回流式响应，包含主管决策过程和子Agent处理结果
    """
    async def generate_stream() -> AsyncGenerator[str, None]:
        try:
            workflow = _get_workflow(request.use_local)
            
            # 发送开始事件
            start_chunk = StreamChunk(
                type="progress",
                content="主管正在分析您的请求...",
                timestamp=_get_timestamp()
            )
            yield f"data: {json.dumps(start_chunk.dict(), ensure_ascii=False)}\n\n"
            
            # 流式执行工作流
            full_response = ""
            for state in workflow.stream(request.message, thread_id=request.session_id):
                if "agent_output" in state and state["agent_output"]:
                    output = state["agent_output"]
                    if output != full_response:
                        new_content = output[len(full_response):] if full_response else output
                        full_response = output
                        
                        stream_chunk = StreamChunk(
                            type="message",
                            content=new_content,
                            timestamp=_get_timestamp()
                        )
                        yield f"data: {json.dumps(stream_chunk.dict(), ensure_ascii=False)}\n\n"
                
                if "next_agent" in state and state["next_agent"]:
                    agent_switch_chunk = StreamChunk(
                        type="agent_switch",
                        content=f"切换到 {state['next_agent']} 处理...",
                        agent_type=AgentType(state["next_agent"]) if state["next_agent"] != "end" else None,
                        timestamp=_get_timestamp()
                    )
                    yield f"data: {json.dumps(agent_switch_chunk.dict(), ensure_ascii=False)}\n\n"
            
            # 发送结束事件
            end_chunk = StreamChunk(
                type="end",
                content=full_response,
                timestamp=_get_timestamp()
            )
            yield f"data: {json.dumps(end_chunk.dict(), ensure_ascii=False)}\n\n"
            
        except Exception as e:
            logger.error(f"流式工作流请求处理失败: {e}", exc_info=True)
            error_chunk = StreamChunk(
                type="error",
                content=str(e),
                timestamp=_get_timestamp()
            )
            yield f"data: {json.dumps(error_chunk.dict(), ensure_ascii=False)}\n\n"
    
    return StreamingResponse(
        generate_stream(),
        media_type="text/event-stream",
        headers={
            "Cache-Control": "no-cache",
            "Connection": "keep-alive",
        }
    )


def start_server(host: str = "0.0.0.0", port: int = 8000, reload: bool = False):
    """
    启动API服务器
    
    Args:
        host: 监听地址
        port: 监听端口
        reload: 是否启用热重载
    """
    logger.info(f"启动AI Agent API Server: {host}:{port}")
    uvicorn.run(
        "server.api:app",
        host=host,
        port=port,
        reload=reload,
        log_level="info"
    )


if __name__ == "__main__":
    start_server()
