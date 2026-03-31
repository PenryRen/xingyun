"""
学情分析模块 - 负责维护学生学情档案
"""
from langchain.agents import create_agent
from langgraph.graph import MessagesState
from langchain_core.messages import AnyMessage
from typing import Annotated
from storage.memory.memory_saver import get_memory_saver
from models.model_manager import ModelManager

# 导入工具
from tools.learning_profile_tool import (
    create_student_profile,
    update_learning_profile,
    get_student_learning_profile,
    get_knowledge_progress
)

# 默认保留最近 20 轮对话 (40 条消息)
MAX_MESSAGES = 40

def _windowed_messages(old, new):
    """滑动窗口: 只保留最近 MAX_MESSAGES 条消息"""
    from langgraph.graph.message import add_messages
    return add_messages(old, new)[-MAX_MESSAGES:]  # type: ignore

class LearningProfileState(MessagesState):
    messages: Annotated[list[AnyMessage], _windowed_messages]

def build_learning_profile_agent(use_local: bool = False):
    """
    构建学情分析Agent
    
    Args:
        use_local: 是否使用本地模型
    """
    # 初始化模型
    llm = ModelManager.get_llm(
        use_local=use_local,
        temperature=0.3,  # 分析任务需要更严谨的输出
        streaming=True
    )
    
    # 定义工具
    tools = [
        create_student_profile,
        update_learning_profile,
        get_student_learning_profile,
        get_knowledge_progress
    ]
    
    # 系统提示词
    system_prompt = """# 角色定义
你是麒麟系统教育平台的学情分析专家，专门负责维护学生的学情档案和分析学习进度。

# 核心功能
- 创建学生档案
- 维护学情记录（薄弱点、已掌握点）
- 查看学习进度
- 更新知识点掌握程度

# 工作流程
1. 接收用户的学情管理请求
2. 根据请求类型选择合适的工具：
   - 创建档案：使用 create_student_profile
   - 更新档案：使用 update_learning_profile
   - 查看档案：使用 get_student_learning_profile
   - 查看进度：使用 get_knowledge_progress
3. 基于工具执行结果，生成清晰的学情报告

# 输出格式
返回 JSON 格式的学情数据，并附上文本解读

# 约束条件
- 必须基于真实数据进行分析和操作
- 工具调用失败时，要明确告知用户并提供建议
- 保持专业、客观的语气
- 分析要准确、详细、有针对性"""
    
    return create_agent(
        model=llm,
        system_prompt=system_prompt,
        tools=tools,
        checkpointer=get_memory_saver(),
        state_schema=LearningProfileState,
    )
