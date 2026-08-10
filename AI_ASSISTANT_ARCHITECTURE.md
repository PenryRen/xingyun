# 星云 AI 助教架构改造说明

## 1. 改造目标

本次改造将 AI 助教从“可能自行查询数据库的通用问答工具”调整为“基于当前页面脱敏学情摘要的解释与建议服务”。

考试分析、学情档案和总览指标由 WebBE 直接访问本地 MySQL 并使用 Java 代码计算；AI Agent 和 DeepSeek 只负责解释统计结果、回答学生问题和生成学习建议。

智能组卷本轮暂不改造，继续保持禁用状态，作为后续独立任务。

## 2. 最终数据流

```text
学生端页面
    │ 只发送学生问题
    ▼
WebBE（登录态校验）
    │ 查询当前登录学生的数据
    ▼
Java 学情服务
    │ 计算指标并生成脱敏摘要
    ▼
内部 Agent 服务
    │ 只解释白名单摘要
    ▼
DeepSeek
    │ 返回解释、建议和复习计划
    ▼
学生端展示
```

## 3. WebBE 实现

主要文件：

- `WebBE/wdd-user-web/src/main/java/com/mindskip/wdd/controller/AiLearningController.java`
- `WebBE/wdd-user-web/src/main/java/com/mindskip/wdd/service/AiLearningService.java`
- `WebBE/wdd-user-web/src/main/java/com/mindskip/wdd/service/impl/AiLearningServiceImpl.java`
- `WebBE/wdd-user-web/src/main/java/com/mindskip/wdd/service/AiAssistantService.java`
- `WebBE/wdd-user-web/src/main/java/com/mindskip/wdd/service/impl/AiAssistantServiceImpl.java`
- `WebBE/wdd-user-web/src/main/java/com/mindskip/wdd/viewmodel/ai/AiLearningWorkspaceVM.java`

### 3.1 学情聚合接口

```http
POST /api/ai/learning/workspace
```

接口从当前登录会话获取用户，不接收前端传入的 `userId`。Java 服务查询 `t_exam_paper_answer`，并计算：

- 考试总数、已定稿数、有效定稿数、异常定稿数
- 待批改、待核验、核验失败和其他状态数量
- 最近最多六场考试的得分率趋势
- 最近六场平均得分率
- 首场到末场的变化百分点
- 成绩波动幅度
- 题目汇总正确率及其考试场次数
- 定稿率
- 知识点数据是否可用

缺失分数使用 `null` 表示，真实的 0 分保留为 `0`，不能混淆。

### 3.2 AI 助教接口

```http
POST /api/ai/learning/assistant
POST /api/ai/learning/assistant/session/reset
```

前端只提交：

```json
{
  "message": "结合我的成绩趋势，告诉我为什么不稳定"
}
```

WebBE 负责查询当前用户数据、生成脱敏摘要，并调用内部 Agent。会话 ID 由 WebBE 为当前用户生成随机 UUID，浏览器不能自行指定或复用会话 ID。

## 4. 发送给模型的数据

允许发送的字段只有：

```json
{
  "source": "LOCAL_MYSQL",
  "counts": {
    "total": 0,
    "finalized": 0,
    "validFinalized": 0,
    "invalidFinalized": 0,
    "waitingReview": 0,
    "waitingVerification": 0,
    "verificationFailed": 0,
    "other": 0
  },
  "summary": {
    "trendSampleCount": 0,
    "recentAveragePercent": null,
    "changeFromFirstPercentPoints": null,
    "volatilityPercentPoints": null,
    "questionAccuracyPercent": null,
    "accuracySampleCount": 0,
    "finalizedPercent": null
  },
  "trend": [],
  "knowledgePoints": {
    "available": false
  }
}
```

模型不会收到：

- 姓名、工号、用户名和个人身份资料
- Cookie、浏览器 Token、API Key
- 数据库账号、密码和 SQL 查询权限
- 完整试卷、完整答卷、试卷名称
- 未经计算的原始数据库记录

平均得分率和题目正确率是不同统计口径，Agent 被明确禁止直接比较二者高低，也不能把逐场题目数据擅自描述为正确率上升或下降。

## 5. Agent 实现

主要文件：

- `AI_AGENT/projects/src/agents/agent.py`
- `AI_AGENT/projects/src/main.py`

当前 Agent 配置为：

```python
tools = []
```

Agent 不再导入或注册 Supabase、考试分析、学情查询和组卷工具。`main.py` 会对摘要执行来源、数量、范围和统计不变量校验，拒绝伪造或越界数据。

Agent 只负责：

- 解释成绩趋势
- 解释当前考情分析页面
- 根据平均分、正确率和波动给出复习计划
- 结合连续会话回答后续问题
- 明确区分事实、计算结果、假设和建议

Agent 不得把知识点、难度、答题用时、心理状态等未提供的信息写成事实，也不得要求学生向 AI 上传完整试卷或完整答卷。

旧的 `src/tools/exam_analysis_tool.py` 等文件暂时作为 legacy 保留，但当前运行路径不可达，后续可单独清理或改造成明确的兼容适配层。

## 6. 前端实现

主要文件：

- `WebFE/wdd-user-web/src/views/ai/workspace.vue`
- `WebFE/wdd-user-web/src/views/ai/tabs/AiAssistantTab.vue`
- `WebFE/wdd-user-web/src/views/ai/tabs/AiOverviewTab.vue`
- `WebFE/wdd-user-web/src/views/ai/tabs/ExamAnalysisTab.vue`
- `WebFE/wdd-user-web/src/views/ai/tabs/LearningProfileTab.vue`
- `WebFE/wdd-user-web/src/api/aiLearning.ts`
- `WebFE/wdd-user-web/src/api/aiAgent.ts`

已完成：

- 总览、考情分析、学情档案使用一次聚合接口
- AI 助教保留连续对话
- 支持“新对话”并重置服务端会话
- 趋势图、考情报告和学情档案旁增加“询问 AI”入口
- AI 数据边界在页面上明确展示
- 前三个页面不再把统计工作交给模型

## 7. 外部访问边界

Nginx 对浏览器只开放 Agent 健康检查；聊天统一经过已登录的 WebBE。Agent 的内部聊天端点不对外暴露，避免调用方绕过 WebBE 伪造学情摘要。

## 8. 验证结果

- WebBE Java 编译通过
- 学生端 `vue-tsc --noEmit && vite build` 通过
- Agent 镜像构建通过
- Agent、WebBE、学生端和 Nginx 容器均正常运行
- 学生端真实数据已显示：32 场考试、24 场已定稿、最近六场平均得分率 45%、题目正确率 30%
- 连续 AI 对话和图表旁“询问 AI”入口已验证
- 控制台无前端错误或警告
- Git 跟踪文件中的 DeepSeek 样式密钥扫描命中数为 0
- `.env` 文件保持 Git 忽略

DeepSeek 是否可用取决于本机到 `api.deepseek.com` 的网络和代理节点。模型网络不可用时，页面会显示可重试状态，不会伪造回答。

## 9. 回退方式

当前改造分支：

```text
codex/agent-architecture-refactor
```

回退存档分支：

```text
agent架构存档
```

存档提交：

```text
52e3503c5510b1a7c2e9c1542e32f4506b2df991
```

本轮改造尚未提交到 Git，便于继续修改和验收。
