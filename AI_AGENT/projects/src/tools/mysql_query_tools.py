"""
MySQL 查询工具 - 将 WebBE MySQL 数据库查询包装为 LangChain 工具

这些工具让 Agent 能够直接从生产数据库中查询真实的考试数据，
用于考情分析、学情档案等核心功能。
"""
import json
import logging
from langchain.tools import tool
from langchain.tools import ToolRuntime

from tools.mysql_learning_query import (
    get_student_learning_context,
    get_recent_exam_scores,
    get_student_score_summary,
    get_student_exam_count,
    get_student_exam_status_counts,
    get_student_question_accuracy,
    get_student_knowledge_status,
)

logger = logging.getLogger(__name__)


@tool
def query_student_learning_context(
    user_id: int,
    recent_limit: int = 6,
    runtime: ToolRuntime = None
) -> str:
    """
    查询学生的完整学情数据，包括考试次数、各状态统计、最近成绩趋势、
    平均分、分数波动、题目正确率等核心指标。

    参数:
        user_id: 学生用户ID（正整数，来自系统登录上下文）
        recent_limit: 最近多少场考试用于计算趋势（默认6）

    返回:
        JSON格式的学情上下文，包含:
        - examCount: 考试总数
        - finalizedCount: 已完成考试数
        - pendingReviewCount: 待批改数
        - pendingVerifyCount: 待核验数
        - verifyFailedCount: 核验失败数
        - recentAverageScorePercent: 最近平均分百分比
        - scoreStandardDeviationPercent: 分数标准差
        - questionAccuracyPercent: 题目正确率
        - recentScores: 最近考试成绩列表
    """
    try:
        context = get_student_learning_context(user_id, recent_limit)
        return json.dumps(context, ensure_ascii=False, indent=2)
    except Exception as e:
        logger.error(f"查询学情上下文失败 (user_id={user_id}): {e}")
        return json.dumps({
            "error": str(e),
            "message": "无法从数据库查询学情数据，请检查MySQL连接配置"
        }, ensure_ascii=False)


@tool
def query_recent_exam_scores(
    user_id: int,
    limit: int = 6,
    runtime: ToolRuntime = None
) -> str:
    """
    查询学生最近的考试成绩列表。

    参数:
        user_id: 学生用户ID（正整数）
        limit: 返回最近多少场考试（默认6）

    返回:
        JSON格式的最近成绩列表，每项包含考试日期和分数百分比
    """
    try:
        scores = get_recent_exam_scores(user_id, limit)
        return json.dumps({
            "user_id": user_id,
            "recent_scores": scores,
            "count": len(scores)
        }, ensure_ascii=False, indent=2)
    except Exception as e:
        logger.error(f"查询最近成绩失败 (user_id={user_id}): {e}")
        return json.dumps({"error": str(e)}, ensure_ascii=False)


@tool
def query_score_summary(
    user_id: int,
    recent_limit: int = 6,
    runtime: ToolRuntime = None
) -> str:
    """
    查询学生的成绩汇总统计，包括平均分和分数波动。

    参数:
        user_id: 学生用户ID（正整数）
        recent_limit: 统计最近多少场考试（默认6）

    返回:
        JSON格式的成绩汇总，包含平均分、标准差和最近成绩列表
    """
    try:
        summary = get_student_score_summary(user_id, recent_limit)
        return json.dumps(summary, ensure_ascii=False, indent=2)
    except Exception as e:
        logger.error(f"查询成绩汇总失败 (user_id={user_id}): {e}")
        return json.dumps({"error": str(e)}, ensure_ascii=False)


@tool
def query_exam_count(user_id: int, runtime: ToolRuntime = None) -> str:
    """
    查询学生参加过的考试总次数。

    参数:
        user_id: 学生用户ID（正整数）

    返回:
        JSON格式的考试次数
    """
    try:
        count = get_student_exam_count(user_id)
        return json.dumps({
            "user_id": user_id,
            "exam_count": count
        }, ensure_ascii=False, indent=2)
    except Exception as e:
        logger.error(f"查询考试次数失败 (user_id={user_id}): {e}")
        return json.dumps({"error": str(e)}, ensure_ascii=False)


@tool
def query_exam_status_breakdown(user_id: int, runtime: ToolRuntime = None) -> str:
    """
    查询学生考试状态的分类统计（已完成、待批改、待核验、核验失败）。

    参数:
        user_id: 学生用户ID（正整数）

    返回:
        JSON格式的各状态考试数量统计
    """
    try:
        counts = get_student_exam_status_counts(user_id)
        return json.dumps({
            "user_id": user_id,
            "status_breakdown": counts
        }, ensure_ascii=False, indent=2)
    except Exception as e:
        logger.error(f"查询考试状态统计失败 (user_id={user_id}): {e}")
        return json.dumps({"error": str(e)}, ensure_ascii=False)


@tool
def query_question_accuracy(user_id: int, runtime: ToolRuntime = None) -> str:
    """
    查询学生的题目正确率（所有已完成考试中正确题目数/总题目数）。

    参数:
        user_id: 学生用户ID（正整数）

    返回:
        JSON格式的题目正确率百分比
    """
    try:
        accuracy = get_student_question_accuracy(user_id)
        return json.dumps({
            "user_id": user_id,
            "question_accuracy_percent": accuracy
        }, ensure_ascii=False, indent=2)
    except Exception as e:
        logger.error(f"查询题目正确率失败 (user_id={user_id}): {e}")
        return json.dumps({"error": str(e)}, ensure_ascii=False)


@tool
def query_knowledge_status(user_id: int, runtime: ToolRuntime = None) -> str:
    """
    查询学生的知识点掌握状态（当前仅返回可用性状态）。

    参数:
        user_id: 学生用户ID（正整数）

    返回:
        JSON格式的知识点状态信息
    """
    try:
        status = get_student_knowledge_status(user_id)
        return json.dumps(status, ensure_ascii=False, indent=2)
    except Exception as e:
        logger.error(f"查询知识点状态失败 (user_id={user_id}): {e}")
        return json.dumps({"error": str(e)}, ensure_ascii=False)


__all__ = [
    "query_student_learning_context",
    "query_recent_exam_scores",
    "query_score_summary",
    "query_exam_count",
    "query_exam_status_breakdown",
    "query_question_accuracy",
    "query_knowledge_status",
]