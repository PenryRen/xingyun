# AI Agent API Server 使用说明

## 概述

AI Agent API Server 是基于 FastAPI 框架构建的 RESTful API 服务，为麒麟系统教育平台的后端系统提供智能体功能调用接口。

## 功能特性

- **五大智能体类型**：
  - 考试分析（exam_analysis）：分析试卷、识别薄弱点
  - 学情分析（learning_profile）：维护学生档案、跟踪学习进度
  - 学情检测（learning_assessment）：智能组卷、生成测试
  - 办学助手（teaching_assistant）：解答问题、提供学习建议
  - 自动选择（auto）：根据用户意图自动选择合适智能体

- **两种调用模式**：
  - 单智能体模式：直接调用指定类型的智能体
  - 主管工作流模式：由主管智能体自动路由到合适的子智能体

- **支持流式响应**：使用 SSE（Server-Sent Events）实现流式输出

- **会话管理**：通过 session_id 保持多轮对话上下文

## 快速开始

### 1. 环境准备

确保已安装所有依赖：

```bash
cd AI_AGENT/projects
pip install -r requirements.txt
```

### 2. 配置环境变量

创建 `.env` 文件或在系统环境变量中配置：

```env
# OpenAI API 配置（优先使用）
OPENAI_API_KEY=your_openai_api_key
OPENAI_BASE_URL=https://api.openai.com/v1
MODEL_NAME=gpt-4o-mini
```

### 3. 启动服务

#### 方式一：直接运行

```bash
cd AI_AGENT/projects/src
python -m server.api
```

服务将在 `http://localhost:8000` 启动

#### 方式二：使用 uvicorn

```bash
cd AI_AGENT/projects/src
uvicorn server.api:app --host 0.0.0.0 --port 8000 --reload
```

#### 方式三：导入启动

```python
from server import start_server

# 启动服务器
start_server(host="0.0.0.0", port=8000, reload=False)
```

### 4. 访问 API 文档

启动服务后，访问以下地址查看交互式 API 文档：

- Swagger UI: `http://localhost:8000/docs`
- ReDoc: `http://localhost:8000/redoc`

## API 接口说明

### 基础信息

- **Base URL**: `http://localhost:8000`
- **Content-Type**: `application/json`

### 1. 健康检查

#### GET `/health`

检查服务是否正常运行。

**响应示例**：
```json
{
  "status": "ok",
  "message": "服务正常运行",
  "version": "1.0.0",
  "timestamp": "2026-03-30T12:00:00Z"
}
```

### 2. 获取智能体列表

#### GET `/api/v1/agents`

获取所有可用的智能体类型及其描述。

**响应示例**：
```json
{
  "success": true,
  "agents": [
    {
      "type": "exam_analysis",
      "name": "考试分析",
      "description": "分析试卷、识别薄弱点、统计错误率",
      "capabilities": ["试卷分析", "薄弱点识别", "错误率统计", "生成分析报告"]
    }
  ],
  "timestamp": "2026-03-30T12:00:00Z"
}
```

### 3. 单智能体对话（非流式）

#### POST `/api/v1/chat`

与指定类型的智能体进行对话。

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| message | string | 是 | 用户输入的消息 |
| agent_type | string | 否 | 智能体类型，可选值：exam_analysis, learning_profile, learning_assessment, teaching_assistant, auto。默认 auto |
| session_id | string | 否 | 会话ID，用于保持上下文。默认 default |
| use_local | boolean | 否 | 是否使用本地模型。默认 false |
| stream | boolean | 否 | 是否使用流式响应。默认 false |
| temperature | float | 否 | 温度参数，范围 0-2。默认 0.7 |
| context | object | 否 | 额外上下文信息 |

**请求示例**：
```json
{
  "message": "帮我分析这次考试的薄弱点",
  "agent_type": "exam_analysis",
  "session_id": "student_001",
  "use_local": false,
  "temperature": 0.7
}
```

**响应示例**：
```json
{
  "success": true,
  "message": "根据您的考试结果，我发现您在进程管理部分存在薄弱点...",
  "agent_type": "exam_analysis",
  "session_id": "student_001",
  "timestamp": "2026-03-30T12:00:00Z",
  "data": {
    "weak_points": ["进程调度", "内存管理"],
    "error_rate": 0.35
  }
}
```

### 4. 单智能体对话（流式）

#### POST `/api/v1/chat/stream`

与智能体进行流式对话，使用 SSE 返回响应。

**请求参数**：与 `/api/v1/chat` 相同

**响应格式**：Server-Sent Events (SSE)

```
data: {"type": "message", "content": "根据", "agent_type": "exam_analysis", "timestamp": "..."}

data: {"type": "message", "content": "您的", "agent_type": "exam_analysis", "timestamp": "..."}

data: {"type": "end", "content": "完整响应内容", "agent_type": "exam_analysis", "timestamp": "..."}
```

### 5. 主管工作流（非流式）

#### POST `/api/v1/workflow`

调用主管架构工作流，由主管自动分析用户意图并路由到合适的子智能体。

**请求参数**：

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| message | string | 是 | 用户输入的消息 |
| session_id | string | 否 | 会话ID。默认 default |
| use_local | boolean | 否 | 是否使用本地模型。默认 false |
| stream | boolean | 否 | 是否使用流式响应。默认 false |
| context | object | 否 | 额外上下文信息 |

**请求示例**：
```json
{
  "message": "帮我生成一份关于进程管理的测试卷",
  "session_id": "student_001",
  "use_local": false
}
```

**响应示例**：
```json
{
  "success": true,
  "message": "已为您生成一份进程管理测试卷...",
  "selected_agent": "learning_assessment",
  "session_id": "student_001",
  "timestamp": "2026-03-30T12:00:00Z",
  "data": {
    "questions": [...],
    "total_score": 100
  }
}
```

### 6. 主管工作流（流式）

#### POST `/api/v1/workflow/stream`

流式调用主管工作流，可以看到主管的决策过程和智能体切换。

**SSE 事件类型**：
- `progress`: 主管分析中
- `agent_switch`: 智能体切换
- `message`: 智能体输出内容
- `end`: 处理完成
- `error`: 发生错误

## 后端集成示例

### Python 示例

```python
import requests

BASE_URL = "http://localhost:8000"

# 1. 调用考试分析智能体
def analyze_exam(student_id: str, exam_content: str):
    response = requests.post(
        f"{BASE_URL}/api/v1/chat",
        json={
            "message": f"请分析以下考试内容：{exam_content}",
            "agent_type": "exam_analysis",
            "session_id": student_id,
            "use_local": False
        }
    )
    return response.json()

# 2. 使用主管工作流自动处理
def auto_process(student_id: str, user_input: str):
    response = requests.post(
        f"{BASE_URL}/api/v1/workflow",
        json={
            "message": user_input,
            "session_id": student_id,
            "use_local": False
        }
    )
    return response.json()

# 3. 流式调用示例
def stream_chat(student_id: str, message: str):
    import json
    
    response = requests.post(
        f"{BASE_URL}/api/v1/chat/stream",
        json={
            "message": message,
            "agent_type": "teaching_assistant",
            "session_id": student_id
        },
        stream=True
    )
    
    for line in response.iter_lines():
        if line:
            data = json.loads(line.decode('utf-8').replace('data: ', ''))
            print(data)

# 使用示例
if __name__ == "__main__":
    # 分析考试
    result = analyze_exam("student_001", "数学考试：代数部分得分80%，几何部分得分60%")
    print(result)
    
    # 自动处理用户请求
    result = auto_process("student_001", "帮我生成一份代数练习题")
    print(result)
```

### JavaScript/TypeScript 示例

```typescript
const BASE_URL = "http://localhost:8000";

// 调用智能体接口
async function chatWithAgent(
  message: string,
  agentType: string = "auto",
  sessionId: string = "default"
) {
  const response = await fetch(`${BASE_URL}/api/v1/chat`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      message,
      agent_type: agentType,
      session_id: sessionId,
      use_local: false,
    }),
  });
  return await response.json();
}

// 流式调用示例
async function streamChat(message: string, sessionId: string) {
  const response = await fetch(`${BASE_URL}/api/v1/chat/stream`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      message,
      agent_type: "teaching_assistant",
      session_id: sessionId,
    }),
  });

  const reader = response.body?.getReader();
  const decoder = new TextDecoder();

  while (reader) {
    const { done, value } = await reader.read();
    if (done) break;

    const chunk = decoder.decode(value);
    const lines = chunk.split("\n\n");

    for (const line of lines) {
      if (line.startsWith("data: ")) {
        const data = JSON.parse(line.slice(6));
        console.log(data);
      }
    }
  }
}

// 使用示例
chatWithAgent("什么是微内核架构？", "teaching_assistant", "student_001")
  .then((result) => console.log(result));
```

### Java 示例

```java
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class AIAgentClient {
    private static final String BASE_URL = "http://localhost:8000";
    private final HttpClient client = HttpClient.newHttpClient();

    public String chat(String message, String agentType, String sessionId) throws Exception {
        String json = String.format(
            "{\"message\": \"%s\", \"agent_type\": \"%s\", \"session_id\": \"%s\"}",
            message, agentType, sessionId
        );

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL + "/api/v1/chat"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();

        HttpResponse<String> response = client.send(request, 
            HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static void main(String[] args) throws Exception {
        AIAgentClient client = new AIAgentClient();
        String result = client.chat(
            "帮我分析这次考试",
            "exam_analysis",
            "student_001"
        );
        System.out.println(result);
    }
}
```

## 智能体选择指南

| 用户意图 | 推荐智能体 | 关键词 |
|---------|-----------|--------|
| 分析考试、查看薄弱点 | exam_analysis | 分析、试卷、薄弱点、错题、考试 |
| 查看学情、更新档案 | learning_profile | 档案、学情、记录、进度 |
| 生成试卷、进行测试 | learning_assessment | 试卷、测试、组卷、强化、题库 |
| 学习问题、概念询问 | teaching_assistant | 什么是、为什么、怎么、如何 |
| 不确定意图 | auto | - |

## 错误处理

### 错误响应格式

```json
{
  "success": false,
  "error_code": "ERROR_CODE",
  "error_message": "错误描述",
  "details": {...},
  "timestamp": "2026-03-30T12:00:00Z"
}
```

### 常见错误码

| 错误码 | 说明 | 解决方法 |
|-------|------|---------|
| CHAT_ERROR | 聊天处理错误 | 检查请求参数 |
| WORKFLOW_ERROR | 工作流处理错误 | 检查模型配置 |
| MODEL_INIT_ERROR | 模型初始化失败 | 检查API密钥配置 |
| INTERNAL_ERROR | 内部错误 | 查看服务器日志 |

## 性能优化建议

1. **使用缓存**：API Server 会自动缓存 Agent 实例，避免重复创建
2. **合理设置 session_id**：相同用户的对话使用相同的 session_id 保持上下文
3. **流式响应**：对于长文本生成，使用流式接口提升用户体验
4. **本地模型降级**：在网络不稳定时，可以设置 `use_local: true` 使用本地模型

## 注意事项

1. **环境变量**：确保正确配置 OpenAI API 密钥
2. **网络连接**：远程模型需要稳定的网络连接
3. **会话管理**：session_id 用于保持对话上下文，建议按用户分配
4. **超时设置**：默认超时时间为 600 秒，长对话可能需要更长时间

## 技术支持

如有问题，请查看：
- API 文档：`http://localhost:8000/docs`
- 项目 README：`AI_AGENT/projects/README.md`
- 开发文档：`AI_AGENT/projects/README_DEVELOPMENT.md`
