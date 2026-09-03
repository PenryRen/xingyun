"""
学情检测模块 - 负责智能组卷和强化测试
"""
from langchain.agents import create_agent
from langgraph.graph import MessagesState
from langchain_core.messages import AnyMessage
from typing import Annotated
from storage.memory.memory_saver import get_memory_saver
from models.model_manager import ModelManager

# 导入工具
from tools.paper_generator_tool import (
    generate_intelligent_paper,
    generate_enhanced_paper_by_weak_points
)
from tools.exam_recorder_tool import get_question_bank

# 默认保留最近 20 轮对话 (40 条消息)
MAX_MESSAGES = 40

def _windowed_messages(old, new):
    """滑动窗口: 只保留最近 MAX_MESSAGES 条消息"""
    from langgraph.graph.message import add_messages
    return add_messages(old, new)[-MAX_MESSAGES:]  # type: ignore

class LearningAssessmentState(MessagesState):
    messages: Annotated[list[AnyMessage], _windowed_messages]

def build_learning_assessment_agent(use_local: bool = False):
    """
    构建学情检测Agent
    
    Args:
        use_local: 是否使用本地模型
    """
    # 初始化模型
    llm = ModelManager.get_llm(
        use_local=use_local,
        temperature=0.5,  # 组卷任务需要一定的创造性
        streaming=True
    )
    
    # 定义工具
    tools = [
        generate_intelligent_paper,
        generate_enhanced_paper_by_weak_points,
        get_question_bank
    ]
    
    # 系统提示词
    system_prompt = """# 角色定义
你是麒麟系统教育平台的学情检测专家，专门负责智能组卷和强化测试。

# 核心功能
- 智能组卷（根据课程内容）
- 强化测试（针对薄弱点）
- 难度适配

# 工作流程
1. 接收用户的组卷或测试请求
2. 根据请求类型选择合适的工具：
   - 智能组卷：使用 generate_intelligent_paper
   - 强化测试：使用 generate_enhanced_paper
   - 查看题库：使用 get_question_bank
3. 基于工具执行结果，生成完整的试卷

# 输出格式
返回 JSON 格式的试卷，包含题目和答案

# 约束条件
- 必须基于真实数据进行组卷
- 工具调用失败时，要明确告知用户并提供建议
- 保持专业、客观的语气
- 组卷要合理、有针对性"""
    
    return create_agent(
        model=llm,
        system_prompt=system_prompt,
        tools=tools,
        checkpointer=get_memory_saver(),
        state_schema=LearningAssessmentState,
    )
