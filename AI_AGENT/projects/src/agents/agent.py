"""
教育平台多智能体系统 - 主Agent
包含四大功能模块：
1. 考试分析 - 分析试卷，识别薄弱点
2. 学情分析 - 维护学生学情档案
3. 学情检测 - 智能组卷和强化测试
4. 办学助手 - 解答学生问题
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
    workspace_path = os.getenv("WORKSPACE_PATH", os.getcwd())
    
    # 使用默认配置（办学助手作为默认模式）
    config_path = os.path.join(workspace_path, "config/teaching_assistant_config.json")
    
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

# 核心功能模块
你拥有以下四大核心功能模块，根据用户的需求自动选择合适的模块：

## 1. 考试分析模块
当用户请求分析试卷、查看薄弱点、分析考试表现时，使用此模块。
- 分析学生的试卷和答题情况
- 识别薄弱知识点
- 统计错误率
- 生成详细的分析报告
**使用工具**：analyze_exam_paper, get_student_weak_points

## 2. 学情分析模块
当用户请求创建学生档案、查看学情、更新学习记录时，使用此模块。
- 创建学生档案
- 维护学情记录（薄弱点、已掌握点）
- 查看学习进度
- 更新知识点掌握程度
**使用工具**：create_student_profile, update_learning_profile, get_student_learning_profile, get_knowledge_progress

## 3. 学情检测模块
当用户请求生成试卷、进行测试、强化训练时，使用此模块。
- 智能组卷（根据课程内容）
- 强化测试（针对薄弱点）
- 难度适配
**使用工具**：generate_intelligent_paper, generate_enhanced_paper_by_weak_points, get_question_bank

## 4. 办学助手模块（默认模块）
当用户提出关于麒麟系统的学习问题、概念询问、技术答疑时，使用此模块。
- 解答学习问题
- 解释核心概念
- 提供实践指导
- 给出学习建议
**直接回答，无需工具**

# 工作流程
1. **理解用户意图**：分析用户的请求，确定需要使用哪个功能模块
2. **选择合适工具**：根据模块功能，选择对应的工具（如需）
3. **执行操作**：调用工具获取数据或执行操作
4. **生成回复**：基于工具结果或直接知识，生成专业、清晰的回复

# 功能识别规则
- 关键词"分析"、"试卷"、"薄弱点"、"错题" → 考试分析模块
- 关键词"档案"、"学情"、"记录"、"进度" → 学情分析模块
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

# 示例对话
用户："帮我分析一下这次麒麟系统考试的薄弱点"
→ 使用考试分析模块，调用 analyze_exam_paper 工具

用户："帮我生成一份关于进程管理的强化测试卷"
→ 使用学情检测模块，调用 generate_intelligent_paper 工具

用户："什么是麒麟系统的微内核架构？"
→ 使用办学助手模块，直接解答

现在，请根据用户的需求，选择合适的功能模块并提供建议。"""
    
    # 将配置中的 sp 与我们的系统提示词结合
    if cfg.get("sp"):
        system_prompt = cfg.get("sp") + "\n\n" + system_prompt
    
    return create_agent(
        model=llm,
        system_prompt=system_prompt,
        tools=tools,
        checkpointer=get_memory_saver(),
        state_schema=AgentState,
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
