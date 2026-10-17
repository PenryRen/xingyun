"""
API数据模型定义

定义所有API请求和响应的数据结构
"""

from typing import Optional, List, Dict, Any, Literal
from pydantic import BaseModel, Field
from enum import Enum


class AgentType(str, Enum):
    """智能体类型枚举"""
    EXAM_ANALYSIS = "exam_analysis"           # 考试分析
    LEARNING_PROFILE = "learning_profile"     # 学情分析
    LEARNING_ASSESSMENT = "learning_assessment"  # 学情检测
    TEACHING_ASSISTANT = "teaching_assistant"    # 办学助手
    AUTO = "auto"                             # 自动选择


class Message(BaseModel):
    """消息模型"""
    role: Literal["user", "assistant", "system"] = Field(..., description="消息角色")
    content: str = Field(..., description="消息内容")
    timestamp: Optional[str] = Field(None, description="时间戳")


class ChatRequest(BaseModel):
    """
    聊天请求模型
    
    用于与智能体进行对话
    """
    message: str = Field(..., description="用户输入的消息", min_length=1)
    agent_type: AgentType = Field(default=AgentType.AUTO, description="智能体类型")
    session_id: str = Field(default="default", description="会话ID，用于保持对话上下文")
    use_local: bool = Field(default=False, description="是否使用本地模型")
    stream: bool = Field(default=False, description="是否使用流式响应")
    temperature: Optional[float] = Field(default=0.7, ge=0.0, le=2.0, description="温度参数")
    context: Optional[Dict[str, Any]] = Field(default=None, description="额外上下文信息")
    
    class Config:
        json_schema_extra = {
            "example": {
                "message": "帮我分析这次考试的薄弱点",
                "agent_type": "exam_analysis",
                "session_id": "student_001",
                "use_local": False,
                "stream": False,
                "temperature": 0.7
            }
        }


class ChatResponse(BaseModel):
    """
    聊天响应模型
    
    智能体的回复
    """
    success: bool = Field(..., description="是否成功")
    message: str = Field(..., description="响应消息")
    agent_type: AgentType = Field(..., description="实际使用的智能体类型")
    session_id: str = Field(..., description="会话ID")
    timestamp: str = Field(..., description="响应时间戳")
    data: Optional[Dict[str, Any]] = Field(default=None, description="结构化数据（如分析结果、试卷等）")
    usage: Optional[Dict[str, Any]] = Field(default=None, description="Token使用情况")
    
    class Config:
        json_schema_extra = {
            "example": {
                "success": True,
                "message": "根据您的考试结果，我发现您在进程管理部分存在薄弱点...",
                "agent_type": "exam_analysis",
                "session_id": "student_001",
                "timestamp": "2026-03-30T12:00:00Z",
                "data": {
                    "weak_points": ["进程调度", "内存管理"],
                    "error_rate": 0.35
                }
            }
        }


class WorkflowRequest(BaseModel):
    """
    工作流请求模型
    
    用于调用主管架构工作流
    """
    message: str = Field(..., description="用户输入的消息", min_length=1)
    session_id: str = Field(default="default", description="会话ID")
    use_local: bool = Field(default=False, description="是否使用本地模型")
    stream: bool = Field(default=False, description="是否使用流式响应")
    context: Optional[Dict[str, Any]] = Field(default=None, description="额外上下文信息")
    
    class Config:
        json_schema_extra = {
            "example": {
                "message": "帮我生成一份关于进程管理的测试卷",
                "session_id": "student_001",
                "use_local": False,
                "stream": False
            }
        }


class WorkflowResponse(BaseModel):
    """
    工作流响应模型
    
    主管工作流的回复
    """
    success: bool = Field(..., description="是否成功")
    message: str = Field(..., description="响应消息")
    selected_agent: AgentType = Field(..., description="主管选择的智能体")
    session_id: str = Field(..., description="会话ID")
    timestamp: str = Field(..., description="响应时间戳")
    data: Optional[Dict[str, Any]] = Field(default=None, description="结构化数据")
    
    class Config:
        json_schema_extra = {
            "example": {
                "success": True,
                "message": "已为您生成一份进程管理测试卷...",
                "selected_agent": "learning_assessment",
                "session_id": "student_001",
                "timestamp": "2026-03-30T12:00:00Z",
                "data": {
                    "questions": [...],
                    "total_score": 100
                }
            }
        }


class AgentListResponse(BaseModel):
    """智能体列表响应"""
    success: bool = Field(..., description="是否成功")
    agents: List[Dict[str, Any]] = Field(..., description="智能体列表")
    timestamp: str = Field(..., description="响应时间戳")


class HealthResponse(BaseModel):
    """健康检查响应"""
    status: str = Field(..., description="服务状态")
    message: str = Field(..., description="状态信息")
    version: str = Field(default="1.0.0", description="API版本")
    timestamp: str = Field(..., description="响应时间戳")


class ErrorResponse(BaseModel):
    """错误响应模型"""
    success: bool = Field(default=False, description="是否成功")
    error_code: str = Field(..., description="错误代码")
    error_message: str = Field(..., description="错误信息")
    details: Optional[Dict[str, Any]] = Field(default=None, description="详细错误信息")
    timestamp: str = Field(..., description="响应时间戳")
    
    class Config:
        json_schema_extra = {
            "example": {
                "success": False,
                "error_code": "MODEL_INIT_ERROR",
                "error_message": "模型初始化失败",
                "details": {"reason": "API key not configured"},
                "timestamp": "2026-03-30T12:00:00Z"
            }
        }


class StreamChunk(BaseModel):
    """流式响应块"""
    type: Literal["message", "agent_switch", "progress", "error", "end"] = Field(..., description="块类型")
    content: Optional[str] = Field(default=None, description="内容")
    agent_type: Optional[AgentType] = Field(default=None, description="当前智能体类型")
    timestamp: str = Field(..., description="时间戳")
