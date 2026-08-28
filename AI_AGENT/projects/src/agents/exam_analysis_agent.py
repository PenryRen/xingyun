"""
考试分析模块 - 负责分析试卷和识别薄弱点

主线程数据库查询架构：
- pre_query_node 在 Agent 处理前直接从 MySQL 查询数据
- 查询结果作为 SystemMessage 注入，Agent 无需工具调用即可获得数据
"""
from langchain.agents import create_agent
from langgraph.graph import MessagesState
from langchain_core.messages import AnyMessage
from typing import Annotated
from storage.memory.memory_saver import get_memory_saver
from models.model_manager import ModelManager

# 预查询辅助模块（主线程数据库查询）
from agents.pre_query_helper import wrap_agent_with_pre_query

# 导入工具
from tools.exam_analysis_tool import analyze_exam_paper, get_student_weak_points
from tools.mysql_query_tools import (
    query_student_learning_context,
    query_recent_exam_scores,
    query_score_summary,
    query_exam_count,
    query_exam_status_breakdown,
    query_question_accuracy,
)

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
        # MySQL 数据库查询工具（主线程，直接从 WebBE 数据库查询真实考试数据）
        query_student_learning_context,
        query_recent_exam_scores,
        query_score_summary,
        query_exam_count,
        query_exam_status_breakdown,
        query_question_accuracy,
        # 原有分析工具
        analyze_exam_paper,
        get_student_weak_points,
    ]
    
    # 系统提示词
    system_prompt = """# 角色定义
你是麒麟系统教育平台的考试分析专家，专门负责分析学生的试卷和识别薄弱点。

# 主线程数据获取机制
系统已在后台自动从 MySQL 数据库查询了学生的考试数据。当你看到消息中有
【数据库查询结果 - 主线程直接查询】标记时，说明真实数据已经注入到对话中。
请直接使用这些数据进行分析，无需再次调用数据库查询工具。

# 核心功能
- 分析学生的试卷和答题情况
- 识别薄弱知识点
- 统计错误率
- 生成详细的分析报告

# 工作流程（数据已由主线程注入）
1. **检查数据**：首先查看消息中是否有【数据库查询结果】，数据已由系统自动查询
2. 直接基于已注入的数据，分析学生的考试表现
3. 使用 analyze_exam_paper 进行深度试卷分析（如需要）
4. 使用 get_student_weak_points 识别薄弱点（如需要）
5. 生成详细的分析报告，包括错误率统计和改进建议

# 重要提醒
- **数据已由系统主线程自动查询并注入，直接使用即可**
- 不要重复调用数据库查询工具
- 不要凭空编造数据，一切分析基于系统注入的数据

# 输出格式
返回 JSON 格式的分析报告，并附上文本解读

# 约束条件
- 必须基于真实数据进行分析
- 工具调用失败时，要明确告知用户并提供建议
- 保持专业、客观的语气
- 分析要准确、详细、有针对性"""
    
    # 构建内部 Agent
    inner_agent = create_agent(
        model=llm,
        system_prompt=system_prompt,
        tools=tools,
        state_schema=ExamAnalysisState,
    )
    
    # 包装预查询工作流（主线程数据库查询 → Agent 处理）
    return wrap_agent_with_pre_query(
        inner_agent=inner_agent,
        state_schema=ExamAnalysisState,
        checkpointer=get_memory_saver(),
    )
