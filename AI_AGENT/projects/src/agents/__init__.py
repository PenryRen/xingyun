"""
教育平台多智能体系统 - 模块导出
"""
from .agent import build_agent, get_agent_by_request
from .exam_analysis_agent import build_exam_analysis_agent
from .learning_profile_agent import build_learning_profile_agent
from .learning_assessment_agent import build_learning_assessment_agent
from .teaching_assistant_agent import build_teaching_assistant_agent

__all__ = [
    "build_agent",
    "get_agent_by_request",
    "build_exam_analysis_agent",
    "build_learning_profile_agent",
    "build_learning_assessment_agent",
    "build_teaching_assistant_agent"
]
