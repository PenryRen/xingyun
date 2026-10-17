"""
Graphs模块 - 包含LangGraph工作流定义

这个模块包含使用LangGraph构建的工作流，用于协调多个Agent的协作。
"""

from graphs.learning_workflow import (
    LearningSupervisorWorkflow,
    LearningWorkflowState,
    create_learning_workflow,
    run_learning_workflow
)

__all__ = [
    "LearningSupervisorWorkflow",
    "LearningWorkflowState",
    "create_learning_workflow",
    "run_learning_workflow"
]
