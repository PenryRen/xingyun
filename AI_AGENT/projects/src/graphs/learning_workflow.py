"""
学习辅助工作流 - 主管架构

这个工作流采用主管架构，包含：
1. 主管节点（Supervisor）：负责分析用户意图并路由到合适的子Agent
2. 四个子Agent节点：
   - 考试分析Agent：分析试卷和薄弱点
   - 学情分析Agent：维护学生学情档案
   - 学情检测Agent：智能组卷和强化测试
   - 办学助手Agent：解答学习问题

工作流程：
用户输入 -> 主管分析 -> 选择子Agent -> 子Agent处理 -> 返回结果
"""

from typing import Annotated, TypedDict, Literal, Optional
from models.model_manager import ModelManager

# 尝试导入 langchain 相关模块
try:
    from langchain_core.messages import HumanMessage, AIMessage, SystemMessage
    from langgraph.graph import StateGraph, END
    from langgraph.graph.message import add_messages
    from langgraph.checkpoint.memory import MemorySaver
    from agents.exam_analysis_agent import build_exam_analysis_agent
    from agents.learning_profile_agent import build_learning_profile_agent
    from agents.learning_assessment_agent import build_learning_assessment_agent
    from agents.teaching_assistant_agent import build_teaching_assistant_agent
except ImportError as e:
    print(f"警告: 无法导入工作流相关模块: {e}")
    HumanMessage = None
    AIMessage = None
    SystemMessage = None
    StateGraph = None
    END = None
    add_messages = None
    MemorySaver = None
    build_exam_analysis_agent = None
    build_learning_profile_agent = None
    build_learning_assessment_agent = None
    build_teaching_assistant_agent = None


class LearningWorkflowState(TypedDict):
    """工作流状态"""
    messages: Annotated[list, add_messages]
    next_agent: Optional[Literal["exam_analysis", "learning_profile", "learning_assessment", "teaching_assistant", "end"]]
    agent_output: Optional[str]
    use_local: bool


class LearningSupervisorWorkflow:
    """
    学习辅助主管工作流
    
    主管负责分析用户意图，然后路由到合适的子Agent进行处理
    """
    
    def __init__(self, use_local: bool = False):
        self.use_local = use_local
        self.llm = ModelManager.get_llm(use_local=use_local, temperature=0.3)
        
        # 初始化子Agent
        self.exam_analysis_agent = build_exam_analysis_agent(use_local=use_local)
        self.learning_profile_agent = build_learning_profile_agent(use_local=use_local)
        self.learning_assessment_agent = build_learning_assessment_agent(use_local=use_local)
        self.teaching_assistant_agent = build_teaching_assistant_agent(use_local=use_local)
        
        # 构建工作流图
        self.workflow = self._build_workflow()
        self.app = self.workflow.compile(checkpointer=MemorySaver())
    
    def _build_workflow(self) -> StateGraph:
        """构建工作流图"""
        
        # 定义状态图
        workflow = StateGraph(LearningWorkflowState)
        
        # 添加节点
        workflow.add_node("supervisor", self._supervisor_node)
        workflow.add_node("exam_analysis", self._exam_analysis_node)
        workflow.add_node("learning_profile", self._learning_profile_node)
        workflow.add_node("learning_assessment", self._learning_assessment_node)
        workflow.add_node("teaching_assistant", self._teaching_assistant_node)
        
        # 设置入口节点
        workflow.set_entry_point("supervisor")
        
        # 添加条件边：从主管到各个子Agent
        workflow.add_conditional_edges(
            "supervisor",
            self._route_to_agent,
            {
                "exam_analysis": "exam_analysis",
                "learning_profile": "learning_profile",
                "learning_assessment": "learning_assessment",
                "teaching_assistant": "teaching_assistant",
                "end": END
            }
        )
        
        # 添加边：从子Agent到结束
        workflow.add_edge("exam_analysis", END)
        workflow.add_edge("learning_profile", END)
        workflow.add_edge("learning_assessment", END)
        workflow.add_edge("teaching_assistant", END)
        
        return workflow
    
    def _supervisor_node(self, state: LearningWorkflowState) -> LearningWorkflowState:
        """
        主管节点
        
        分析用户意图，决定下一步调用哪个子Agent
        """
        messages = state["messages"]
        
        # 构建主管的系统提示词
        supervisor_prompt = """你是麒麟系统教育平台的主管Agent，负责分析用户意图并决定调用哪个子Agent。

# 可用子Agent

1. **exam_analysis** - 考试分析Agent
   - 功能：分析试卷、识别薄弱点、统计错误率
   - 触发关键词：分析、试卷、薄弱点、错题、考试、成绩
   - 示例："帮我分析这次考试"、"我的薄弱点在哪里"

2. **learning_profile** - 学情分析Agent
   - 功能：创建/更新学生档案、查看学情记录、跟踪学习进度
   - 触发关键词：档案、学情、记录、进度、学习档案、我的学习情况
   - 示例："查看我的学情档案"、"更新我的学习进度"

3. **learning_assessment** - 学情检测Agent
   - 功能：智能组卷、生成测试卷、强化训练
   - 触发关键词：试卷、测试、组卷、强化、题库、生成试卷、来一套题
   - 示例："帮我生成一份测试卷"、"针对薄弱点出几道题"

4. **teaching_assistant** - 办学助手Agent
   - 功能：解答学习问题、解释概念、提供学习建议
   - 触发关键词：什么是、为什么、怎么、如何、解释一下、问题
   - 示例："什么是微内核架构"、"怎么理解进程调度"

# 决策规则

1. 分析用户最新的消息，判断其意图
2. 选择最合适的子Agent
3. 如果用户意图不明确，选择 teaching_assistant 进行通用问答
4. 如果用户表示满意或结束对话，选择 end

# 输出格式

请只输出以下之一：
- "exam_analysis" - 调用考试分析Agent
- "learning_profile" - 调用学情分析Agent
- "learning_assessment" - 调用学情检测Agent
- "teaching_assistant" - 调用办学助手Agent
- "end" - 结束对话

不要输出任何其他内容。"""
        
        # 调用LLM进行决策
        supervisor_messages = [
            SystemMessage(content=supervisor_prompt),
            *messages
        ]
        
        response = self.llm.invoke(supervisor_messages)
        decision = response.content.strip().lower()
        
        # 解析决策
        valid_agents = ["exam_analysis", "learning_profile", "learning_assessment", "teaching_assistant", "end"]
        next_agent = decision if decision in valid_agents else "teaching_assistant"
        
        return {
            **state,
            "next_agent": next_agent
        }
    
    def _route_to_agent(self, state: LearningWorkflowState) -> str:
        """
        路由函数
        
        根据主管的决策，路由到对应的子Agent
        """
        return state.get("next_agent", "teaching_assistant")
    
    def _exam_analysis_node(self, state: LearningWorkflowState) -> LearningWorkflowState:
        """
        考试分析节点
        
        调用考试分析Agent处理用户请求
        """
        messages = state["messages"]
        
        # 调用考试分析Agent
        result = self.exam_analysis_agent.invoke({"messages": messages})
        
        # 提取Agent的输出
        agent_messages = result.get("messages", [])
        if agent_messages:
            last_message = agent_messages[-1]
            agent_output = last_message.content if hasattr(last_message, 'content') else str(last_message)
        else:
            agent_output = "考试分析完成"
        
        return {
            **state,
            "messages": messages + [AIMessage(content=agent_output)],
            "agent_output": agent_output,
            "next_agent": None  # 重置，让主管再次决策
        }
    
    def _learning_profile_node(self, state: LearningWorkflowState) -> LearningWorkflowState:
        """
        学情分析节点
        
        调用学情分析Agent处理用户请求
        """
        messages = state["messages"]
        
        # 调用学情分析Agent
        result = self.learning_profile_agent.invoke({"messages": messages})
        
        # 提取Agent的输出
        agent_messages = result.get("messages", [])
        if agent_messages:
            last_message = agent_messages[-1]
            agent_output = last_message.content if hasattr(last_message, 'content') else str(last_message)
        else:
            agent_output = "学情分析完成"
        
        return {
            **state,
            "messages": messages + [AIMessage(content=agent_output)],
            "agent_output": agent_output,
            "next_agent": None
        }
    
    def _learning_assessment_node(self, state: LearningWorkflowState) -> LearningWorkflowState:
        """
        学情检测节点
        
        调用学情检测Agent处理用户请求
        """
        messages = state["messages"]
        
        # 调用学情检测Agent
        result = self.learning_assessment_agent.invoke({"messages": messages})
        
        # 提取Agent的输出
        agent_messages = result.get("messages", [])
        if agent_messages:
            last_message = agent_messages[-1]
            agent_output = last_message.content if hasattr(last_message, 'content') else str(last_message)
        else:
            agent_output = "学情检测完成"
        
        return {
            **state,
            "messages": messages + [AIMessage(content=agent_output)],
            "agent_output": agent_output,
            "next_agent": None
        }
    
    def _teaching_assistant_node(self, state: LearningWorkflowState) -> LearningWorkflowState:
        """
        办学助手节点
        
        调用办学助手Agent处理用户请求
        """
        messages = state["messages"]
        
        # 调用办学助手Agent
        result = self.teaching_assistant_agent.invoke({"messages": messages})
        
        # 提取Agent的输出
        agent_messages = result.get("messages", [])
        if agent_messages:
            last_message = agent_messages[-1]
            agent_output = last_message.content if hasattr(last_message, 'content') else str(last_message)
        else:
            agent_output = "问题解答完成"
        
        return {
            **state,
            "messages": messages + [AIMessage(content=agent_output)],
            "agent_output": agent_output,
            "next_agent": None
        }
    
    def invoke(self, user_input: str, thread_id: str = "default") -> str:
        """
        调用工作流处理用户输入
        
        Args:
            user_input: 用户输入
            thread_id: 会话ID
            
        Returns:
            工作流输出
        """
        initial_state = {
            "messages": [HumanMessage(content=user_input)],
            "next_agent": None,
            "agent_output": None,
            "use_local": self.use_local
        }
        
        config = {"configurable": {"thread_id": thread_id}}
        
        # 执行工作流
        result = self.app.invoke(initial_state, config)
        
        # 返回最后一条消息的内容
        messages = result.get("messages", [])
        if messages:
            last_message = messages[-1]
            return last_message.content if hasattr(last_message, 'content') else str(last_message)
        
        return "处理完成"
    
    def stream(self, user_input: str, thread_id: str = "default"):
        """
        流式调用工作流
        
        Args:
            user_input: 用户输入
            thread_id: 会话ID
            
        Yields:
            工作流状态更新
        """
        initial_state = {
            "messages": [HumanMessage(content=user_input)],
            "next_agent": None,
            "agent_output": None,
            "use_local": self.use_local
        }
        
        config = {"configurable": {"thread_id": thread_id}}
        
        # 流式执行工作流
        for state in self.app.stream(initial_state, config):
            yield state


# 便捷函数
def create_learning_workflow(use_local: bool = False):
    """
    创建学习辅助工作流
    
    Args:
        use_local: 是否使用本地模型
        
    Returns:
        学习辅助工作流实例或None
    """
    try:
        if not all([StateGraph, MemorySaver, build_exam_analysis_agent, build_learning_profile_agent, build_learning_assessment_agent, build_teaching_assistant_agent]):
            raise ImportError("工作流相关模块未完全导入")
        return LearningSupervisorWorkflow(use_local=use_local)
    except Exception as e:
        print(f"创建工作流失败: {e}")
        return None


def run_learning_workflow(user_input: str, use_local: bool = False, thread_id: str = "default") -> str:
    """
    运行学习辅助工作流
    
    Args:
        user_input: 用户输入
        use_local: 是否使用本地模型
        thread_id: 会话ID
        
    Returns:
        工作流输出
    """
    workflow = create_learning_workflow(use_local=use_local)
    return workflow.invoke(user_input, thread_id)
