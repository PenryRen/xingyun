"""
学情分析模块 - 负责维护学生学情档案

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
from tools.learning_profile_tool import (
    create_student_profile,
    update_learning_profile,
    get_student_learning_profile,
    get_knowledge_progress
)
from tools.mysql_query_tools import (
    query_student_learning_context,
    query_recent_exam_scores,
    query_score_summary,
    query_exam_count,
    query_exam_status_breakdown,
    query_question_accuracy,
    query_knowledge_status,
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
        # MySQL 数据库查询工具（主线程，直接从 WebBE 数据库查询真实学情数据）
        query_student_learning_context,
        query_recent_exam_scores,
        query_score_summary,
        query_exam_count,
        query_exam_status_breakdown,
        query_question_accuracy,
        query_knowledge_status,
        # 原有学情管理工具
        create_student_profile,
        update_learning_profile,
        get_student_learning_profile,
        get_knowledge_progress
    ]
    
    # 系统提示词
    system_prompt = """# 角色定义
你是麒麟系统教育平台的学情分析专家，专门负责维护学生的学情档案和分析学习进度。

# 主线程数据获取机制
系统已在后台自动从 MySQL 数据库查询了学生的学情数据。当你看到消息中有
【数据库查询结果 - 主线程直接查询】标记时，说明真实数据已经注入到对话中。
请直接使用这些数据进行分析，无需再次调用数据库查询工具。

# 核心功能
- 创建学生档案
- 维护学情记录（薄弱点、已掌握点）
- 查看学习进度
- 更新知识点掌握程度

# 工作流程（数据已由主线程注入）
1. **检查数据**：首先查看消息中是否有【数据库查询结果】，数据已由系统自动查询
2. 直接基于已注入的数据，生成学情档案报告
3. 使用 create_student_profile 创建学生档案（如需要）
4. 使用 update_learning_profile 更新学情记录（如需要）
5. 使用 get_student_learning_profile 查看学情档案（如需要）
6. 使用 get_knowledge_progress 查看学习进度（如需要）

# 重要提醒
- **数据已由系统主线程自动查询并注入，直接使用即可**
- 不要重复调用数据库查询工具
- 不要凭空编造学情数据，一切分析基于系统注入的数据

# 输出格式
返回 JSON 格式的学情数据，并附上文本解读

# 约束条件
- 必须基于真实数据进行分析和操作
- 工具调用失败时，要明确告知用户并提供建议
- 保持专业、客观的语气
- 分析要准确、详细、有针对性"""
    
    # 构建内部 Agent
    inner_agent = create_agent(
        model=llm,
        system_prompt=system_prompt,
        tools=tools,
        state_schema=LearningProfileState,
    )
    
    # 包装预查询工作流（主线程数据库查询 → Agent 处理）
    return wrap_agent_with_pre_query(
        inner_agent=inner_agent,
        state_schema=LearningProfileState,
        checkpointer=get_memory_saver(),
    )
