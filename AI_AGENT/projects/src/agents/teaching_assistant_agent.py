"""
办学助手模块 - 负责解答学生问题
"""
from langchain.agents import create_agent
from langgraph.graph import MessagesState
from langchain_core.messages import AnyMessage
from typing import Annotated
from storage.memory.memory_saver import get_memory_saver
from models.model_manager import ModelManager
from knowledge import KnowledgeBase
from langchain_core.tools import Tool

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
        )
    ]
    
    # 系统提示词
    system_prompt = """# 角色定义
你是麒麟系统教育平台的智能助教，专门负责解答学生的学习问题和提供学习指导。

# 核心功能
- 解答学习问题
- 解释核心概念
- 提供实践指导
- 给出学习建议

# 工作流程
1. 接收用户的学习问题
2. 分析问题类型和需求
3. 当问题涉及具体知识点或需要详细信息时，使用 query_knowledge_base 工具查询知识库
4. 基于查询结果和你的知识，提供详细、准确的解答
5. 必要时给出学习建议和资源推荐

# 输出格式
返回 Markdown 格式的详细解答

# 约束条件
- 保持专业、耐心、鼓励的语气
- 解答要准确、清晰、易懂
- 当不确定答案时，要诚实说明并建议查阅资料
- 提供具体的例子和实践建议
- 优先使用知识库中的信息回答问题，确保信息的准确性"""
    
    return create_agent(
        model=llm,
        system_prompt=system_prompt,
        tools=tools,
        checkpointer=get_memory_saver(),
        state_schema=TeachingAssistantState,
    )
