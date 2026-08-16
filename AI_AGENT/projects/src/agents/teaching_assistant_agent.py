"""
办学助手模块 - 负责解答学生问题

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
from knowledge import KnowledgeBase
from langchain_core.tools import Tool

# 预查询辅助模块（主线程数据库查询）
from agents.pre_query_helper import wrap_agent_with_pre_query

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

class TeachingAssistantState(MessagesState):
    messages: Annotated[list[AnyMessage], _windowed_messages]

def build_teaching_assistant_agent(use_local: bool = False):
    """
    构建办学助手Agent
    
    Args:
        use_local: 是否使用本地模型
    """
    # 初始化模型
    llm = ModelManager.get_llm(
        use_local=use_local,
        temperature=0.7,  # 问答任务需要一定的灵活性
        streaming=True
    )
    
    # 初始化知识库
    knowledge_base = KnowledgeBase(use_local=use_local)
    
    # 定义工具
    def query_knowledge_base(query: str, k: int = 3) -> str:
        """
        查询知识库
        
        Args:
            query: 查询文本
            k: 返回结果数量
            
        Returns:
            相关文档内容
        """
        context = knowledge_base.get_relevant_context(query, k=k)
        if context:
            return f"知识库查询结果:\n{context}"
        else:
            return "知识库中未找到相关信息"
    
    # 创建工具
    tools = [
        Tool(
            name="query_knowledge_base",
            func=query_knowledge_base,
            description="查询知识库获取相关信息，用于回答学习问题。当需要获取特定知识点的详细信息时使用。"
        ),
        # MySQL 数据库查询工具（当学生询问自己的考试情况、学情时使用）
        query_student_learning_context,
        query_recent_exam_scores,
        query_score_summary,
        query_exam_count,
        query_exam_status_breakdown,
        query_question_accuracy,
        query_knowledge_status,
    ]
    
    # 系统提示词
    system_prompt = """# 角色定义
你是麒麟系统教育平台的智能助教，专门负责解答学生的学习问题和提供学习指导。

# 主线程数据获取机制
系统已在后台自动从 MySQL 数据库查询了学生的考试数据。当你看到消息中有
【数据库查询结果 - 主线程直接查询】标记时，说明真实数据已经注入到对话中。
请直接使用这些数据进行分析，无需再次调用数据库查询工具。

# 核心功能
- 解答学习问题
- 解释核心概念
- 提供实践指导
- 给出学习建议
- 基于数据库数据分析学生的考试情况和学情

# 工作流程
1. **检查数据**：首先查看消息中是否有【数据库查询结果】，数据已由系统自动查询
2. 接收用户的学习问题
3. 分析问题类型和需求
4. 当问题涉及具体知识点时，使用 query_knowledge_base 工具查询知识库
5. 基于系统注入的数据库数据或知识库查询结果，提供详细、准确的解答
6. 必要时给出学习建议和资源推荐

# 重要提醒
- **考试数据已由系统自动查询并注入，直接使用即可**
- 不要重复调用数据库查询工具
- 当用户没有提供 user_id 时，系统会自动提示用户

# 输出格式
返回 Markdown 格式的详细解答

# 约束条件
- 保持专业、耐心、鼓励的语气
- 解答要准确、清晰、易懂
- 当不确定答案时，要诚实说明并建议查阅资料
- 提供具体的例子和实践建议
- 优先使用知识库中的信息回答问题，确保信息的准确性"""
    
    # 构建内部 Agent
    inner_agent = create_agent(
        model=llm,
        system_prompt=system_prompt,
        tools=tools,
        state_schema=TeachingAssistantState,
    )
    
    # 包装预查询工作流（主线程数据库查询 → Agent 处理）
    return wrap_agent_with_pre_query(
        inner_agent=inner_agent,
        state_schema=TeachingAssistantState,
        checkpointer=get_memory_saver(),
    )
