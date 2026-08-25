"""
教育平台多智能体系统 - 主Agent
包含四大功能模块：
1. 考试分析 - 分析试卷，识别薄弱点
2. 学情分析 - 维护学生学情档案
3. 学情检测 - 智能组卷和强化测试
4. 办学助手 - 解答学生问题

主线程数据库查询架构：
- pre_query_node 在 Agent 处理前直接从 MySQL 查询数据
- 查询结果作为 SystemMessage 注入，Agent 无需工具调用即可获得数据
"""
import os
import json
from typing import Annotated, Optional
from langchain.agents import create_agent
from langgraph.graph import MessagesState
from langgraph.graph.message import add_messages
from langchain_core.messages import AnyMessage
from storage.memory.memory_saver import get_memory_saver
from models.model_manager import ModelManager

# 预查询辅助模块（主线程数据库查询）
from agents.pre_query_helper import wrap_agent_with_pre_query

# 导入工具
from tools.exam_analysis_tool import analyze_exam_paper, get_student_weak_points
from tools.learning_profile_tool import (
    create_student_profile,
    update_learning_profile,
    get_student_learning_profile,
    get_knowledge_progress
)
from tools.paper_generator_tool import (
    generate_intelligent_paper,
    generate_enhanced_paper_by_weak_points
)
from tools.exam_recorder_tool import (
    add_questions_to_database,
    record_exam_result,
    get_question_bank
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
    return add_messages(old, new)[-MAX_MESSAGES:]  # type: ignore

class AgentState(MessagesState):
    messages: Annotated[list[AnyMessage], _windowed_messages]

def build_agent(use_local: bool = False, ctx=None):
    """
    构建教育平台多智能体系统
    
    这个Agent集成了四大功能模块，通过System Prompt和工具调用
    来实现不同的教育功能。
    
    Args:
        use_local: 是否使用本地模型
        ctx: 上下文信息（可选）
    """
    # 优先使用 PROJECT_ROOT（由 main.py 注入），不依赖 WORKSPACE_PATH（可能为相对路径）
    project_root = os.getenv("PROJECT_ROOT") or os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
    
    # 使用默认配置（办学助手作为默认模式）
    config_path = os.path.join(project_root, "config", "teaching_assistant_config.json")
    
    if os.path.exists(config_path):
        with open(config_path, 'r', encoding='utf-8') as f:
            cfg = json.load(f)
        model_name = cfg['config'].get("model")
        temperature = cfg['config'].get('temperature', 0.7)
        timeout = cfg['config'].get('timeout', 600)
    else:
        # 配置文件不存在时使用默认值
        model_name = None
        temperature = 0.7
        timeout = 600

    # 环境变量 MODEL_NAME 优先级高于配置文件
    env_model_name = os.getenv("MODEL_NAME")
    if env_model_name:
        model_name = env_model_name
    
    # 使用模型管理器初始化模型
    llm = ModelManager.get_llm(
        use_local=use_local,
        model_name=model_name,
        temperature=temperature,
        streaming=True,
        timeout=timeout
    )
    
    # 导入各个模块的Agent构建函数
    from .exam_analysis_agent import build_exam_analysis_agent
    from .learning_profile_agent import build_learning_profile_agent
    from .learning_assessment_agent import build_learning_assessment_agent
    from .teaching_assistant_agent import build_teaching_assistant_agent
    
    # 定义所有可用工具
    tools = [
        # MySQL 数据库查询工具（主线程，直接从 WebBE 数据库查询真实考试数据）
        query_student_learning_context,
        query_recent_exam_scores,
        query_score_summary,
        query_exam_count,
        query_exam_status_breakdown,
        query_question_accuracy,
        query_knowledge_status,
        # 考试分析工具
        analyze_exam_paper,
        get_student_weak_points,
        # 学情管理工具
        create_student_profile,
        update_learning_profile,
        get_student_learning_profile,
        get_knowledge_progress,
        # 组卷工具
        generate_intelligent_paper,
        generate_enhanced_paper_by_weak_points,
        # 试卷入库工具
        add_questions_to_database,
        record_exam_result,
        get_question_bank
    ]
    
    # 系统提示词 - 定义Agent的行为和功能路由
    system_prompt = """# 角色定义
你是麒麟系统教育平台的智能助教，是一个集成多功能的AI助手，能够为学生提供全面的学习支持。

# 主线程数据获取机制
系统已在后台自动从 MySQL 数据库查询了学生的考试数据。当你看到消息中有
【数据库查询结果 - 主线程直接查询】标记时，说明真实数据已经注入到对话中。
请直接使用这些数据进行分析，无需再次调用数据库查询工具。

# 核心功能模块
你拥有以下四大核心功能模块，根据用户的需求自动选择合适的模块：

## 1. 考试分析模块
当用户请求分析试卷、查看薄弱点、分析考试表现时，使用此模块。
- **数据已在对话中提供**，直接使用【数据库查询结果】中的数据进行分析
- 如需创建用户档案，使用 create_student_profile
- 如需更新学情记录，使用 update_learning_profile
- 如需深度分析，使用 analyze_exam_paper 和 get_student_weak_points

## 2. 学情分析模块
当用户请求创建学生档案、查看学情、更新学习记录时，使用此模块。
- **数据已在对话中提供**，直接使用【数据库查询结果】中的数据
- 使用 create_student_profile、update_learning_profile 管理档案
- 使用 get_student_learning_profile、get_knowledge_progress 查看进度

## 3. 学情检测模块
当用户请求生成试卷、进行测试、强化训练时，使用此模块。
- 使用 generate_intelligent_paper 智能组卷
- 使用 generate_enhanced_paper_by_weak_points 强化测试
- 使用 get_question_bank 查看题库

## 4. 办学助手模块（默认模块）
当用户提出学习问题、概念询问、技术答疑时，使用此模块。
- 直接回答，无需工具

# 工作流程
1. **检查数据**：首先查看消息中是否有【数据库查询结果 - 主线程直接查询】，这些是系统已为你查询好的真实数据
2. **理解用户意图**：分析用户的请求，确定需要使用哪个功能模块
3. **使用数据**：直接基于已注入的数据进行分析，不要凭空编造
4. **选择工具**：仅在需要额外操作（如创建档案、生成试卷）时调用工具
5. **生成回复**：基于真实数据，生成专业、清晰的回复

# 重要规则
- **考试分析、学情分析的数据已由系统主线程自动查询并注入，直接使用即可**
- 不要重复调用数据库查询工具（数据已在对话中）
- 当用户没有提供 user_id 时，系统会自动提示用户
- 分析必须基于真实数据，不能凭空编造

# 功能识别规则
- 关键词"分析"、"试卷"、"薄弱点"、"错题"、"考试" → 考试分析模块
- 关键词"档案"、"学情"、"记录"、"进度"、"学习档案" → 学情分析模块
- 关键词"试卷"、"测试"、"组卷"、"强化" → 学情检测模块
- 其他学习问题 → 办学助手模块

# 输出格式要求
- 考试分析：返回 JSON 格式的分析报告，并附上文本解读
- 学情报告：返回 JSON 格式的学情数据，并附上文本解读
- 试卷生成：返回 JSON 格式的试卷，包含题目和答案
- 问题解答：返回 Markdown 格式的详细解答

# 约束条件
- 必须基于真实数据进行分析和操作
- 工具调用失败时，要明确告知用户并提供建议
- 保持专业、耐心、鼓励的语气
- 解答要准确、清晰、易懂
- 当不确定答案时，要诚实说明并建议查阅资料

现在，请根据用户的需求，选择合适的功能模块并提供建议。"""
    
    # 将配置中的 sp 与我们的系统提示词结合
    if cfg.get("sp"):
        system_prompt = cfg.get("sp") + "\n\n" + system_prompt
    
    # 构建内部 Agent
    inner_agent = create_agent(
        model=llm,
        system_prompt=system_prompt,
        tools=tools,
        state_schema=AgentState,
    )
    
    # 包装预查询工作流（主线程数据库查询 → Agent 处理）
    return wrap_agent_with_pre_query(
        inner_agent=inner_agent,
        state_schema=AgentState,
        checkpointer=get_memory_saver(),
    )

def get_agent_by_request(request: str, use_local: bool = False):
    """
    根据用户请求选择合适的Agent模块
    
    Args:
        request: 用户的请求内容
        use_local: 是否使用本地模型
    
    Returns:
        对应的Agent实例
    """
    # 导入各个模块的Agent构建函数
    from .exam_analysis_agent import build_exam_analysis_agent
    from .learning_profile_agent import build_learning_profile_agent
    from .learning_assessment_agent import build_learning_assessment_agent
    from .teaching_assistant_agent import build_teaching_assistant_agent
    
    # 关键词匹配
    request_lower = request.lower()
    
    # 考试分析模块关键词
    if any(keyword in request_lower for keyword in ["分析", "试卷", "薄弱点", "错题", "考试"]):
        return build_exam_analysis_agent(use_local=use_local)
    
    # 学情分析模块关键词
    elif any(keyword in request_lower for keyword in ["档案", "学情", "记录", "进度", "学习档案"]):
        return build_learning_profile_agent(use_local=use_local)
    
    # 学情检测模块关键词
    elif any(keyword in request_lower for keyword in ["试卷", "测试", "组卷", "强化", "题库"]):
        return build_learning_assessment_agent(use_local=use_local)
    
    # 办学助手模块（默认）
    else:
        return build_teaching_assistant_agent(use_local=use_local)
