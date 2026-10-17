# AI Agent 数据库迁移与交接指南

## 1. 迁移目标

AI Agent 的业务数据访问统一使用星云项目现有的 `wdd` MySQL。此次迁移只修改 `AI_AGENT/`，保留现有主管 Agent、专业 Agent、工具名称和调用入口，不修改 WebBE、WebFE、Nginx 或项目已有 Docker MySQL 数据卷。

迁移完成后的约束：

- 不再需要旧云数据库地址或访问密钥；
- 不再需要其他关系型数据库驱动；
- 模型不能生成或直接执行任意 SQL；
- WebBE 正式考试数据只读；
- Agent 自有数据与 WebBE 业务表隔离；
- 数据库密码只通过本地 `.env` 或部署环境注入，不进入 Git。

## 2. 迁移前后架构

迁移前存在多套并行实现：旧云数据库客户端、旧 SQLAlchemy 连接、外部数据库 Checkpointer，以及尚未接入旧工具链的 MySQL 查询模块。旧工具还存在函数名不一致的问题，部分工具即使建立数据库连接也会导入失败。

迁移后的运行链：

```text
WebBE 正式考试表 t_exam_paper_answer
        │ 固定、参数化、按用户隔离的只读查询
        ▼
mysql_learning_query.py ──→ 脱敏学情摘要

Agent 自有 ai_* 表
        │ SQLAlchemy + PyMySQL
        ▼
data_base_tools.py
        ▼
考试分析 / 学情管理 / 题库 / 组卷工具
        ▼
现有多 Agent 主流程
```

LangGraph 对话 Checkpoint 当前使用进程内 `MemorySaver`，不保存业务数据，也不再依赖外部数据库。服务重启后对话 Checkpoint 会清空；学生档案、题库、考试和知识点等业务数据仍持久化在 MySQL。

## 3. 数据边界

### 3.1 WebBE 业务数据

正式考试记录继续使用项目已有表：

- 表：`t_exam_paper_answer`
- 用户隔离：`create_user = :user_id`
- 软删除：`deleted = 0`
- 访问方式：固定 SQL + 绑定参数
- 权限要求：只需要 `SELECT`

当前只读取计算学情所需的最小字段，不读取姓名、Cookie、Token、API Key、完整试卷或完整答卷。

### 3.2 Agent 自有数据

旧智能体数据库模型迁移到同一个 `wdd` MySQL，并统一增加 `ai_` 前缀：

| 旧逻辑实体 | MySQL 表 | 用途 |
| --- | --- | --- |
| students | `ai_student` | Agent 使用的学生标识和基础档案 |
| learning_profiles | `ai_learning_profile` | 薄弱点、掌握点和综合评分 |
| student_knowledge_progress | `ai_student_knowledge_progress` | 知识点掌握度与练习次数 |
| questions | `ai_question` | Agent 生成或维护的题库 |
| exam_records | `ai_exam_record` | Agent 创建的练习或临时考试记录 |
| answer_records | `ai_answer_record` | Agent 练习答题记录 |

这些表不替代 WebBE 的正式考试表，也不会修改已有 `t_*` 表结构。

## 4. 关键代码

- `src/storage/database/mysql_client.py`：统一读取 MySQL 配置并创建连接池；
- `src/storage/database/db.py`：为旧工具保留 `get_session()` 等兼容入口；
- `src/storage/database/shared/model.py`：`ai_*` SQLAlchemy 模型；
- `src/tools/data_base_tools.py`：旧多 Agent 数据工具的 MySQL 实现；
- `src/tools/mysql_learning_query.py`：WebBE 考试表的只读学情查询；
- `src/storage/memory/memory_saver.py`：进程内 LangGraph Checkpoint；
- `migrations/001_mysql_ai_tables.sql`：Agent 自有表建表脚本；
- `tests/test_mysql_agent_repository.py`：旧工具兼容读写闭环测试。

## 5. 配置

复制 `.env.example` 为未跟踪的 `.env`，填写：

```dotenv
XINGYUN_MYSQL_HOST=mysql
XINGYUN_MYSQL_PORT=3306
XINGYUN_MYSQL_DATABASE=wdd
XINGYUN_MYSQL_USERNAME=
XINGYUN_MYSQL_PASSWORD=
XINGYUN_MYSQL_AUTO_CREATE_AI_SCHEMA=true
```

- Docker 网络内的主机名通常为 `mysql`；
- 从 Windows 宿主机运行时通常使用 `127.0.0.1`；
- `XINGYUN_MYSQL_AUTO_CREATE_AI_SCHEMA=true` 仅建议本地开发；
- 生产环境应先执行迁移脚本，再将自动建表设置为 `false`。

## 6. 建表与权限

迁移脚本只包含 `CREATE TABLE IF NOT EXISTS`，不会执行 `DROP TABLE` 或 `ALTER TABLE`：

```bash
mysql -h MYSQL_HOST -P 3306 -u MYSQL_ADMIN_USER -p wdd \
  < migrations/001_mysql_ai_tables.sql
```

生产环境建议为 AI Agent 使用独立数据库账号。最小权限原则：

```sql
GRANT SELECT ON wdd.t_exam_paper_answer TO 'xingyun_ai'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON wdd.ai_student TO 'xingyun_ai'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON wdd.ai_learning_profile TO 'xingyun_ai'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON wdd.ai_student_knowledge_progress TO 'xingyun_ai'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON wdd.ai_question TO 'xingyun_ai'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON wdd.ai_exam_record TO 'xingyun_ai'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON wdd.ai_answer_record TO 'xingyun_ai'@'%';
```

账号创建和密码配置应由部署环境管理，不应写入仓库脚本。

## 7. 启动前验证

```bash
cd AI_AGENT/projects
python -m pip install -r requirements_core.txt
python -m pytest -q tests
python -m compileall -q src tests
```

当前数据库测试覆盖：

- MySQL URL 结构化构建和必填凭证；
- 不在日志或 URL 字符串中暴露密码；
- 学生数据隔离和软删除；
- 真实 0 分与缺失分数区分；
- 最近成绩、平均分、波动、正确率和状态统计；
- Agent 自有学生、题库、考试、答题、档案和知识点读写闭环；
- 建表脚本无删除或修改已有表的语句；
- 旧工具需要的函数名称保持可用。

## 8. 安全设计

- 所有查询使用 SQLAlchemy 表达式或绑定参数；
- 不提供“执行任意 SQL”的 Agent 工具；
- `user_id` 必须来自可信认证上下文，不能从聊天文本直接采用；
- 正式考试表只有只读权限；
- Agent 自有表使用 `ai_*` 命名空间隔离；
- `.env` 已由项目忽略，不应使用 `git add -f` 提交；
- API Key、Cookie、Token、数据库密码不得写入日志、测试数据或 PR 描述。

## 9. 历史数据说明

本次完成的是代码、配置、模型和依赖迁移，不会主动连接旧云数据库，也不会自动复制旧云端历史数据。

如果旧环境仍有必须保留的学生档案、题库或学习进度，需要另行执行一次性 ETL：

1. 在受控环境导出旧数据；
2. 按第 3.2 节完成字段映射；
3. 清洗重复学生标识和无效外键；
4. 在测试库导入并核对数量；
5. 备份目标 MySQL 后再执行正式导入；
6. 导入完成后撤销旧数据库访问凭证。

该 ETL 不属于本次代码 PR，避免在开发机或 CI 中重新引入旧数据库凭证。

## 10. 回退方案

代码回退时只需撤销本次迁移提交。`ai_*` 表可以暂时保留，不会影响 WebBE；这样可以避免误删已经产生的 Agent 数据。

只有在确认不再需要这些数据并完成备份后，才应单独制定删除表操作。本迁移脚本和自动建表逻辑都不会删除数据。

## 11. 已知边界

- 当前知识点数据尚未与 WebBE 题目标签建立稳定映射，学情查询明确返回不可用状态；
- MySQL 学情查询模块不会自行决定何时调用模型；
- 对话 Checkpoint 为进程内存储，服务重启后不保留连续对话；
- 本地 Docker MySQL 联调需要 Docker Desktop daemon 处于运行状态。
