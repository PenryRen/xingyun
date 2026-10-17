"""
预查询辅助模块 - 在 Agent 处理请求之前，从 MySQL 数据库直接查询学生数据

这是"主线程"查询：数据在 LLM Agent 启动之前就已经获取完毕，
Agent 直接使用注入的数据进行分析，无需通过工具调用（子线程）方式查询。
"""
import json
import logging
import re
from typing import Optional

from langchain_core.messages import SystemMessage, HumanMessage, BaseMessage

logger = logging.getLogger(__name__)

# 考情分析 / 学情档案相关关键词
EXAM_KEYWORDS = ["考试", "分析", "试卷", "薄弱点", "错题", "成绩", "考情", "测验", "答题"]
PROFILE_KEYWORDS = ["学情", "档案", "记录", "进度", "学习档案", "学情档案", "学习记录"]


def _extract_user_id(content: str) -> Optional[int]:
    """从消息内容中提取 user_id"""
    patterns = [
        r'user[_\-]?id[=:：\s]*(\d+)',
        r'用户[_\-]?id[=:：\s]*(\d+)',
        r'用户[=:：\s]*(\d+)',
        r'学生[_\-]?id[=:：\s]*(\d+)',
        r'id[=:：\s]*(\d+)',
    ]
    for pattern in patterns:
        match = re.search(pattern, content, re.IGNORECASE)
        if match:
            return int(match.group(1))
    return None


def _is_exam_or_profile_related(content: str) -> bool:
    """判断消息是否与考情分析或学情档案相关"""
    all_keywords = EXAM_KEYWORDS + PROFILE_KEYWORDS
    return any(kw in content for kw in all_keywords)


def _get_last_human_message(messages: list) -> Optional[BaseMessage]:
    """获取最新的用户消息"""
    for msg in reversed(messages):
        if isinstance(msg, HumanMessage):
            return msg
        if hasattr(msg, 'type') and getattr(msg, 'type', None) == 'human':
            return msg
    return None


def create_pre_query_node():
    """
    创建预查询节点函数。
    
    该节点在 Agent 处理请求之前执行：
    1. 从用户消息中提取 user_id
    2. 判断是否为考情/学情相关请求
    3. 如果是，直接查询 MySQL 数据库
    4. 将查询结果作为 SystemMessage 注入到消息流中
    """
    
    def pre_query_node(state: dict) -> dict:
        """主线程数据库查询节点"""
        messages = list(state.get("messages", []))
        if not messages:
            logger.debug("pre_query_node: 没有消息，跳过")
            return {}
        
        # 获取最新的用户消息
        last_human = _get_last_human_message(messages)
        if last_human is None:
            logger.debug("pre_query_node: 没有找到用户消息，跳过")
            return {}
        
        content = last_human.content if hasattr(last_human, 'content') else str(last_human)
        logger.info(f"pre_query_node: 用户消息 = {content[:200]}")
        
        # 提取 user_id
        user_id = _extract_user_id(content)
        if user_id is None:
            # 没有 user_id，检查是否为考情/学情相关请求
            if _is_exam_or_profile_related(content):
                logger.info("pre_query_node: 考情/学情相关请求但未提供 user_id，提示用户")
                hint_message = SystemMessage(
                    content="【系统提示】检测到您正在进行考情/学情相关查询，但未提供 user_id。"
                           "请提供您的用户ID（例如：user_id=123），以便系统从数据库查询真实数据进行分析。"
                )
                return {"messages": [hint_message]}
            return {}
        
        # 检查是否为考情/学情相关
        if not _is_exam_or_profile_related(content):
            logger.info(f"pre_query_node: user_id={user_id} 但非考情/学情请求，跳过数据库查询")
            return {}
        
        # ====== 主线程：直接从 MySQL 数据库查询 ======
        logger.info(f"pre_query_node: 启动主线程数据库查询，user_id={user_id}")
        
        # 找到最后一条用户消息的位置（用于后续注入数据）
        insert_idx = len(messages)
        for i in range(len(messages) - 1, -1, -1):
            msg = messages[i]
            if isinstance(msg, HumanMessage) or (hasattr(msg, 'type') and getattr(msg, 'type', None) == 'human'):
                insert_idx = i
                break
        
        try:
            from tools.mysql_learning_query import get_student_learning_context
            db_context = get_student_learning_context(user_id)
            
            context_str = json.dumps(db_context, ensure_ascii=False, indent=2)
            
            # 构建数据注入消息
            data_message = SystemMessage(
                content=(
                    f"【数据库查询结果 - 主线程直接查询】\n"
                    f"user_id = {user_id}\n"
                    f"以下是该学生的真实考试数据（已从 MySQL wdd 数据库直接查询获取）：\n"
                    f"```json\n{context_str}\n```\n\n"
                    f"请基于以上真实数据进行分析，不要凭空编造数据。"
                )
            )
            
            logger.info(
                f"pre_query_node: 数据库查询成功，"
                f"考试总数={db_context.get('examCount', 0)}, "
                f"已完成={db_context.get('finalizedCount', 0)}, "
                f"平均分={db_context.get('recentAverageScorePercent')}"
            )
            
            new_messages = list(messages)
            new_messages.insert(insert_idx, data_message)
            
            return {"messages": new_messages}
            
        except Exception as e:
            logger.error(f"pre_query_node: 数据库查询失败: {e}", exc_info=True)
            error_message = SystemMessage(
                content=(
                    f"【系统提示】数据库查询失败 (user_id={user_id})：{str(e)}\n"
                    f"请检查 MySQL 连接配置（XINGYUN_MYSQL_* 环境变量）是否正确。\n"
                    f"当前将以有限信息进行分析，但数据可能不准确。"
                )
            )
            new_messages = list(messages)
            new_messages.insert(insert_idx, error_message)
            return {"messages": new_messages}
    
    return pre_query_node


def wrap_agent_with_pre_query(inner_agent, state_schema, checkpointer=None):
    """
    将内部 Agent 包装在预查询工作流中。
    
    工作流：
    START → pre_query_node → inner_agent → END
    
    Args:
        inner_agent: 内部 Agent（create_agent 返回的编译图）
        state_schema: 状态模式类
        checkpointer: 可选的检查点保存器
    
    Returns:
        编译后的 LangGraph 工作流
    """
    from langgraph.graph import StateGraph, END, START
    
    workflow = StateGraph(state_schema)
    
    # 预查询节点
    pre_query = create_pre_query_node()
    workflow.add_node("pre_query", pre_query)
    
    # 内部 Agent 节点
    workflow.add_node("agent", inner_agent)
    
    # 工作流：START → pre_query → agent → END
    workflow.add_edge(START, "pre_query")
    workflow.add_edge("pre_query", "agent")
    workflow.add_edge("agent", END)
    
    return workflow.compile(checkpointer=checkpointer)


__all__ = [
    "create_pre_query_node",
    "wrap_agent_with_pre_query",
    "EXAM_KEYWORDS",
    "PROFILE_KEYWORDS",
]