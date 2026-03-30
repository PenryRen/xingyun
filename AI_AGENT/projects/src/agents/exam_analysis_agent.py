"""
考试分析模块 - 负责分析试卷和识别薄弱点
"""
from langchain.agents import create_agent
from langgraph.graph import MessagesState
from langchain_core.messages import AnyMessage
from typing import Annotated
from storage.memory.memory_saver import get_memory_saver
from models.model_manager import ModelManager

# 导入工具
from tools.exam_analysis_tool import analyze_exam_paper, get_student_weak_points

# 默认保留最近 20 轮对话 (40 条消息)
MAX_MESSAGES = 40

def _windowed_messages(old, new):
    """滑动窗口: 只保留最近 MAX_MESSAGES 条消息"""
    from langgraph.graph.message import add_messages
    return add_messages(old, new)[-MAX_MESSAGES:]  # type: ignore

class ExamAnalysisState(MessagesState):
    messages: Annotated[list[AnyMessage], _windowed_messages]

def build_exam_analysis_agent(use_local: bool = False):
    """
    构建考试分析Agent
    
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
        analyze_exam_paper,
        get_student_weak_points
    ]
    
    # 系统提示词
    system_prompt = """# 角色定义
你是麒麟系统教育平台的考试分析专家，专门负责分析学生的试卷和识别薄弱点。

# 核心功能
- 分析学生的试卷和答题情况
- 识别薄弱知识点
- 统计错误率
- 生成详细的分析报告

# 工作流程
1. 接收用户提供的试卷或考试结果
2. 使用 analyze_exam_paper 工具分析试卷
3. 使用 get_student_weak_points 工具识别薄弱点
4. 生成详细的分析报告，包括错误率统计和改进建议

# 输出格式
返回 JSON 格式的分析报告，并附上文本解读

# 约束条件
- 必须基于真实数据进行分析
- 工具调用失败时，要明确告知用户并提供建议
- 保持专业、客观的语气
- 分析要准确、详细、有针对性"""
    
    return create_agent(
        model=llm,
        system_prompt=system_prompt,
        tools=tools,
        checkpointer=get_memory_saver(),
        state_schema=ExamAnalysisState,
    )
