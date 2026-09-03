# MySQL 学情查询模块

## 目标与边界

本模块从星云 WebBE 已有的 `wdd` MySQL 数据库中读取学生考试答卷，使用确定性代码计算学情指标。它是独立查询模块，当前不注册为 LangChain 工具，也不修改或接入 Agent 主流程。

数据库迁移后的范围包括：

- `src/storage/database/mysql_client.py`：统一 MySQL 连接池和只读事务；
- `src/storage/database/db.py`：旧工具兼容的 MySQL Session 入口；
- `src/storage/database/shared/model.py`：Agent 自有 `ai_*` ORM 模型；
- `src/tools/mysql_learning_query.py`：固定参数化查询及指标计算；
- `src/tools/data_base_tools.py`：原多 Agent 数据工具的 MySQL 实现；
- `migrations/001_mysql_ai_tables.sql`：可重复执行的 Agent 自有表迁移；
- `tests/`：隔离、统计、空值和配置测试；
- MySQL 驱动、环境变量和部署文档。

未改动 WebBE、前端、Nginx、多 Agent 工作流、系统提示词或智能组卷逻辑。旧数据库客户端、旧连接层和旧持久化 Checkpointer 已从 AI Agent 的运行依赖中移除。

## 两类 MySQL 数据

1. WebBE 业务数据：`t_exam_paper_answer` 是正式考试数据源，Agent 只通过固定、参数化的只读查询访问，不创建、不修改。
2. Agent 自有数据：旧智能体工具需要的学生档案、知识点进度、题库和临时练习记录使用 `ai_*` 表，避免与 WebBE 的 `t_*` 业务表冲突。

Agent 自有表包括：

- `ai_student`
- `ai_learning_profile`
- `ai_student_knowledge_progress`
- `ai_question`
- `ai_exam_record`
- `ai_answer_record`

生产部署应显式执行 `migrations/001_mysql_ai_tables.sql`。该脚本只有 `CREATE TABLE IF NOT EXISTS`，不会删除或修改 WebBE 业务表。对话 Checkpoint 当前使用进程内 `MemorySaver`；业务数据全部持久化在 MySQL。

## 数据来源与口径

- 数据库：项目 Docker MySQL 中的 `wdd`；
- 业务表：`t_exam_paper_answer`；
- 学生关联：`create_user = :user_id`；
- 软删除：只读取 `deleted = 0`；
- 排序：`create_time DESC, id DESC`；
- 状态：1待批改、2完成、3待核验、4核验失败；
- 成绩：`user_score / paper_score * 100`；
- 最近成绩：最近六条“完成且分数有效”的记录，输出按时间正序；
- 正确率：完成记录中有效 `question_correct / question_count` 的汇总；
- 波动：最近有效成绩百分比的总体标准差；
- `paper_type`是人工/抽题/随机组卷方式，不作为考试记录筛选条件；
- 知识点数据尚无稳定字段映射，明确返回 `QUESTION_TAGS_NOT_AVAILABLE`。

缺失分数返回 `null` 并从成绩统计中排除，真实0分保留为0。空学生不会伪造成0分，而是返回空趋势和 `null` 指标。

## 配置

复制 `.env.example` 中的以下变量到本地 `.env`：

```dotenv
XINGYUN_MYSQL_HOST=mysql
XINGYUN_MYSQL_PORT=3306
XINGYUN_MYSQL_DATABASE=wdd
XINGYUN_MYSQL_USERNAME=
XINGYUN_MYSQL_PASSWORD=
XINGYUN_MYSQL_AUTO_CREATE_AI_SCHEMA=true
```

在 Docker 网络内使用 `mysql`；从 Windows 宿主机运行测试时使用 `127.0.0.1`。自动建表只建议本地开发使用；生产执行迁移后应设为 `false`。生产或共享环境建议创建独立账号：对所需 WebBE `t_*` 表只授予 `SELECT`，对 `ai_*` 表授予读写权限。真实密码不得写入源码、日志或 Git。

## 独立调用示例

```python
from tools.mysql_learning_query import get_student_learning_context

# user_id必须来自可信登录上下文，不能从聊天文本直接读取。
context = get_student_learning_context(user_id=101)
```

结果只包含计数、分数百分比、趋势、正确率和知识点可用状态，不包含姓名、工号、Cookie、Token、API Key、完整试卷或完整答卷。

## 后续接入契约

对方补充主Agent逻辑时，只需调用：

```python
learning_context = get_student_learning_context(current_user_id)
```

本模块不决定何时调用大模型、不处理意图路由、不维护会话，也不允许模型生成或执行SQL。后续接入层必须从服务端认证信息取得 `current_user_id`，并且只能将脱敏结果交给模型。

## 测试

```bash
cd AI_AGENT/projects
pytest -q tests/test_mysql_client.py tests/test_mysql_learning_query.py tests/test_mysql_agent_repository.py
```

单元测试使用内存数据库，不连接或修改本机 Docker MySQL，并覆盖旧工具接口的学生、题库、考试、答题、档案和知识点读写闭环。联调 WebBE 数据时只执行固定 `SELECT`。
