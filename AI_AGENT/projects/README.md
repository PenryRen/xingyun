# 项目结构说明

# 本地运行
## 运行流程
bash scripts/local_run.sh -m flow

## 运行节点
bash scripts/local_run.sh -m node -n node_name

# 启动HTTP服务
bash scripts/http_run.sh -m http -p 5000

## 🚀 使用示例

### 创建学生档案

```plaintext
你好，我是学生张三，学号2025001。请帮我创建一个学生档案。
```

### 分析考试

```plaintext
帮我分析一下这次麒麟系统考试的薄弱点
```

### 生成测试卷

```plaintext
帮我生成一份关于进程管理的强化测试卷，10道题，中等难度
```

### 查询学情

```plaintext
帮我查看张三的学情报告
```

### 答疑解惑

```plaintext
什么是麒麟系统的微内核架构？有什么特点？
```



# 更新题库有以下几种方式：

## 方法一：通过 Agent 直接添加题目

你可以直接与 Agent 对话，提供题目信息：

```plaintext
请帮我添加以下题目到题库：

题目内容：麒麟系统的微内核架构有哪些特点？
分类：内核架构
难度：0.5
正确答案：模块化设计、可扩展性、安全性高
题目解析：微内核将核心功能最小化，其他功能作为服务运行
标签：{"keyword": "微内核", "chapter": "第一章"}
```

## 方法二：批量添加题目（JSON 格式）

如果你想批量添加多道题目，可以提供 JSON 格式的数据：

```json
[
  {
    "question_text": "麒麟系统的微内核架构有哪些特点？",
    "category": "内核架构",
    "difficulty": 0.5,
    "answer": "模块化设计、可扩展性、安全性高",
    "explanation": "微内核将核心功能最小化，其他功能作为服务运行",
    "tags": {"keyword": "微内核", "chapter": "第一章"}
  },
  {
    "question_text": "麒麟系统中进程调度使用的是什么算法？",
    "category": "进程管理",
    "difficulty": 0.7,
    "answer": "CFS（完全公平调度器）",
    "explanation": "CFS是Linux内核中使用的进程调度算法，麒麟系统沿用了这一设计",
    "tags": {"keyword": "调度", "chapter": "第二章"}
  }
]
```

## 题目数据格式说明

每个题目包含以下字段：

| 字段            | 类型   | 必填 | 说明                                           |
| --------------- | ------ | ---- | ---------------------------------------------- |
| `question_text` | String | ✅    | 题目内容                                       |
| `category`      | String | ✅    | 题目分类（如：内核架构、进程管理、文件系统等） |
| `difficulty`    | Float  | ✅    | 难度系数（0.0-1.0，0.0 最简单，1.0 最难）      |
| `answer`        | String | ✅    | 正确答案                                       |
| `explanation`   | String | ❌    | 题目解析（可选）                               |
| `tags`          | Object | ❌    | 标签信息（可选，JSON 格式）                    |

## 难度系数参考

- **0.0 - 0.3**：简单题（基础概念）
- **0.3 - 0.6**：中等题（应用理解）
- **0.6 - 1.0**：困难题（综合分析）

## 查询题库

你也可以查询现有题库：

```plaintext
请帮我查询关于"进程管理"的所有题目
```

或者：

```plaintext
请帮我查看题库中难度为中等（0.4-0.7）的题目
```

## 完整示例

下面是一个完整的对话示例：

**用户**：

```plaintext
请帮我批量添加以下题目到题库：

1. 题目：麒麟系统的进程地址空间是如何划分的？
   分类：内存管理
   难度：0.6
   答案：用户空间和内核空间
   解析：进程地址空间分为用户空间（0-3GB）和内核空间（3-4GB）
   
2. 题目：麒麟系统中系统调用的执行流程是什么？
   分类：系统调用
   难度：0.8
   答案：用户态→内核态→执行系统调用→返回用户态
   解析：通过软中断陷入内核，执行完系统调用后返回
```

**Agent**：

```plaintext
已成功添加 2 道题目到题库：

✅ 题目1：麒麟系统的进程地址空间是如何划分的？
   - 分类：内存管理
   - 难度：0.6（中等）
   - ID: 1

✅ 题目2：麒麟系统中系统调用的执行流程是什么？
   - 分类：系统调用
   - 难度：0.8（困难）
   - ID: 2

添加结果：
- 总计：2道
- 成功：2道
- 失败：0道
```

## 注意事项

1. **分类命名**：建议使用统一的分类命名规范，如："内核架构"、"进程管理"、"文件系统" 等
2. **难度设置**：确保难度系数在 0.0-1.0 范围内



# 📦 本地部署指南

### 一、环境准备

#### 1. 系统要求

- **操作系统**：Linux / macOS / Windows (WSL2)
- **Python 版本**：Python 3.8+

#### 2. 安装 Python 依赖

```bash
# 进入项目目录
cd /workspace/projects

# 安装依赖
pip install -r requirements.txt
```

### 二、配置环境变量

项目需要以下环境变量配置：

#### 必需的环境变量

创建 `.env` 文件（在项目根目录）：

```bash
# Supabase 数据库配置
SUPABASE_URL=your_supabase_project_url
SUPABASE_KEY=your_supabase_anon_key

# 豆包大模型配置
COZE_WORKLOAD_IDENTITY_API_KEY=your_api_key
COZE_INTEGRATION_MODEL_BASE_URL=https://your-model-base-url.com

# 工作目录
COZE_WORKSPACE_PATH=/path/to/your/project
```

#### 获取 Supabase 凭证

1. 注册并登录 [Supabase](https://supabase.com/)
2. 创建新项目
3. 在项目设置中找到：
   - Project URL（用于 `SUPABASE_URL`）
   - anon/public key（用于 `SUPABASE_KEY`）

#### 获取豆包大模型 API Key

1. 注册并登录火山引擎方舟平台
2. 创建 API Key
3. 获取 Base URL

### 三、数据库初始化

#### 1. 同步数据库表结构

```bash
# 进入项目目录
cd /workspace/projects

# 生成模型
coze-coding-ai db generate-models

# 执行数据库迁移
coze-coding-ai db upgrade
```

#### 2. 验证数据库连接

你可以运行以下 Python 脚本验证连接：

```python
# test_db_connection.py
from storage.database.supabase_client import get_supabase_client

try:
    client = get_supabase_client()
    response = client.table('health_check').select('*').execute()
    print("✅ 数据库连接成功！")
    print(response.data)
except Exception as e:
    print(f"❌ 数据库连接失败：{e}")
```

运行测试：

```bash
python test_db_connection.py
```

#### 四、启动 HTTP 服务

```bash
# 在默认端口 8000 启动服务
bash scripts/http_run.sh -p 8000

# 或指定其他端口
bash scripts/http_run.sh -p 5000
```

启动后，你可以通过以下方式访问：

**浏览器访问**：

```plaintext
http://localhost:8000/docs
```

这将显示 FastAPI 的自动生成的 API 文档界面。

### 五、API 使用示例

服务启动后，你可以通过 HTTP API 调用智能体：

#### 1. 同步调用

```bash
curl -X POST "http://localhost:8000/run" \
  -H "Content-Type: application/json" \
  -d '{
    "messages": [
      {
        "role": "user",
        "content": "你好，我是学生张三，请帮我创建档案"
      }
    ]
  }'
```

#### 2. 流式调用

```bash
curl -X POST "http://localhost:8000/stream" \
  -H "Content-Type: application/json" \
  -d '{
    "messages": [
      {
        "role": "user",
        "content": "帮我分析一下这次考试的薄弱点"
      }
    ]
  }'
```

### 六、快速测试脚本

创建 `test_agent.py` 文件进行快速测试：

```python
#!/usr/bin/env python3
"""
快速测试脚本
"""
import sys
import os

# 添加项目路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), 'src'))

from agents.agent import build_agent
from langchain_core.messages import HumanMessage

def test_agent():
    print("🚀 正在初始化智能体...")
    
    # 构建智能体
    agent = build_agent()
    
    print("✅ 智能体初始化成功！")
    print("=" * 50)
    
    # 测试对话
    test_queries = [
        "你好，我是学生张三，学号2025001，请帮我创建档案",
        "什么是麒麟系统的微内核架构？",
        "帮我生成一份关于进程管理的测试卷"
    ]
    
    for query in test_queries:
        print(f"\n👤 用户：{query}")
        print("-" * 50)
        
        try:
            # 调用智能体
            response = agent.invoke({
                "messages": [HumanMessage(content=query)]
            })
            
            # 获取回复
            if hasattr(response, 'messages') and response.messages:
                reply = response.messages[-1].content
                print(f"🤖 助手：{reply}")
            else:
                print("🤖 助手：未能获取回复")
                
        except Exception as e:
            print(f"❌ 错误：{e}")
        
        print("=" * 50)

if __name__ == "__main__":
    test_agent()
```

运行测试：

```bash
python test_agent.py
```
