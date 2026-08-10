"""星云 AI 助教。

固定考试查询与统计由 WebBE/MySQL 完成；本 Agent 不注册数据库或组卷
工具，只解释 WebBE 提供的匿名聚合证据并回答学习问题。
"""
import os
import json
from typing import Annotated, Optional
from langchain.agents import create_agent
from langchain_openai import ChatOpenAI
from langgraph.graph import MessagesState
from langgraph.graph.message import add_messages
from langchain_core.messages import AnyMessage
from coze_coding_utils.runtime_ctx.context import default_headers, new_context
from storage.memory.memory_saver import get_memory_saver

# 默认保留最近 20 轮对话 (40 条消息)
MAX_MESSAGES = 40

def _windowed_messages(old, new):
    """滑动窗口: 只保留最近 MAX_MESSAGES 条消息"""
    return add_messages(old, new)[-MAX_MESSAGES:]  # type: ignore

class AgentState(MessagesState):
    messages: Annotated[list[AnyMessage], _windowed_messages]

def build_agent(ctx=None):
    """
    构建不接触数据库的单模型 AI 助教。
    """
    workspace_path = os.getenv("COZE_WORKSPACE_PATH", "/workspace/projects")
    
    # 使用默认配置（办学助手作为默认模式）
    config_path = os.path.join(workspace_path, "config/teaching_assistant_config.json")
    
    with open(config_path, 'r', encoding='utf-8') as f:
        cfg = json.load(f)
    
    api_key = os.getenv("COZE_WORKLOAD_IDENTITY_API_KEY")
    base_url = os.getenv("COZE_INTEGRATION_MODEL_BASE_URL")
    
    llm = ChatOpenAI(
        model=cfg['config'].get("model"),
        api_key=api_key,
        base_url=base_url,
        temperature=cfg['config'].get('temperature', 0.7),
        streaming=True,
        timeout=cfg['config'].get('timeout', 600),
        extra_body={
            "thinking": {
                "type": cfg['config'].get('thinking', 'disabled')
            }
        },
        default_headers=default_headers(ctx) if ctx else {}
    )
    
    # 固定查询和统计由 WebBE/MySQL 代码直接完成。
    # 主模型不再调度数据库工具，也不会接触 SQL、Cookie、Token 或数据库凭据。
    tools = []
    
    # 模型只解释调用方提供的脱敏统计摘要。
    system_prompt = """# 角色定义
你是星云教育平台的 AI 助教。

# 工作边界
- 考试查询、统计和学情档案由 WebBE 代码直接访问本地 MySQL 完成。
- 你不调用数据库工具、不生成 SQL、不猜测任何未提供的数据。
- 请求中若包含“脱敏学习数据摘要”，只能依据该摘要解释趋势和给出建议。
- 每轮请求中的最新摘要优先于历史轮次中的旧摘要。
- knowledgePoints.available=false 时，必须明确说明暂无知识点级数据。
- 没有学习数据摘要时，只回答通用知识，不能声称知道学生个人成绩。

# 可解释的数据
- counts：考试总数、已定稿数以及待批改、待核验、核验失败等状态数量。
- summary：最近六场平均分、首末场变化、成绩波动、题目正确率和定稿率。
- trend：按时间先后排列的最多六场数值得分与题目汇总，不包含试卷名称和答卷。
- knowledgePoints：只表示知识点数据是否可用；不可用时不能推断具体薄弱知识点。

# 统计口径
- summary.questionAccuracyPercent 是全部有效已定稿考试的题目汇总正确率，不只属于最近六场。
- summary.accuracyEvidenceExamCount 是参与正确率计算的考试场次数，不是题目数；不得用它推算答对或答错题数。
- trend 中的 questionCorrect/questionCount 只属于对应的最近六场；不得与全量正确率混成同一个样本。
- 分数字段和完全答对题数可能因题目权重不同而比例不一致，这是正常现象。

# 证据纪律
- 摘要不包含题目难度、考试名称、具体知识点、答题用时、学习投入或心理状态，不得把这些内容写成事实。
- 低分和波动只能证明结果差异；原因必须明确写成“需要结合答卷或学生反馈验证”的假设。
- 平均得分率与题目正确率是不同指标，禁止直接比较高低、差值或据此判断学习状态。
- 这是硬性禁止：不得出现“题目正确率高于/低于平均得分率”或“平均得分率高于/低于题目正确率”之类句子，即使后面补充“口径不同”也不允许。两项指标必须分开陈述，并分别说明口径。
- 不得把最近六场的逐场题目汇总描述为“正确率上升/下降”，除非请求中明确提供了已计算的趋势指标。
- 对成绩波动的可能原因只能并列列出待验证假设，不得使用“更可能”“主要因为”等证据不足的排序或因果表述。
- 不得根据单场题目数量判断题量是否造成成绩变化，也不得把题量与得分变化写成因果关系。
- 即使作为待验证假设，也不得把题量列为成绩变化的可能原因；题目数量只能用于说明逐场正确题数的统计口径。
- 没有明确基准时，不得把平均分、正确率或波动描述为“偏高”“偏低”“优秀”“较差”；只能陈述数值和变化。
- 待批改和待核验属于考试流程状态，不得要求学生本人完成批改或核验；可建议等待教师处理。
- 核验失败可建议学生查看考试记录或联系教师/管理员，但不能声称知道失败原因。

# 输出要求
- 使用适合页面直接展示的简洁纯文本，不使用 Markdown 表格或标题符号。
- 先说结论，再列出数据依据和下一步建议。
- 明确区分事实、计算结果和建议。
- 不得要求学生向 AI 补充姓名、工号、完整试卷或完整答卷；只能建议学生自行查看考试记录、在本地复盘答卷或咨询教师，并继续使用允许的匿名聚合指标交流。
- 不向学生展示 source、reasonCode 或字段名等内部实现细节，改用自然语言解释。"""

    # 将配置中的通用办学助手说明与当前安全边界结合
    if cfg.get("sp"):
        system_prompt = cfg.get("sp") + "\n\n" + system_prompt
    
    return create_agent(
        model=llm,
        system_prompt=system_prompt,
        tools=tools,
        checkpointer=get_memory_saver(),
        state_schema=AgentState,
    )
