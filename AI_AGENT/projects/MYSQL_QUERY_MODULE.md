# MySQL 学情查询模块

## 目标与边界

本模块从星云 WebBE 已有的 `wdd` MySQL 数据库中读取学生考试答卷，使用确定性代码计算学情指标。它是独立查询模块，当前不注册为 LangChain 工具，也不修改或接入 Agent 主流程。

当前改动范围仅包括：

- `src/storage/database/mysql_client.py`：MySQL 连接池和只读事务；
- `src/tools/mysql_learning_query.py`：固定参数化查询及指标计算；
- `tests/`：隔离、统计、空值和配置测试；
- MySQL 驱动与环境变量示例。

未改动 WebBE、前端、Nginx、多 Agent 工作流、系统提示词或智能组卷逻辑。

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
```

在 Docker 网络内使用 `mysql`；从Windows宿主机运行测试时使用 `127.0.0.1`。生产或共享环境建议创建只拥有所需表 `SELECT` 权限的独立账号。真实密码不得写入源码、日志或Git。

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

本模块不决定何时调用DeepSeek、不处理意图路由、不维护会话，也不允许模型生成或执行SQL。后续接入层必须从服务端认证信息取得 `current_user_id`，并且只能将脱敏结果交给模型。

## 测试

```bash
cd AI_AGENT/projects
pytest -q tests/test_mysql_client.py tests/test_mysql_learning_query.py
```

单元测试使用内存数据库，不连接或修改本机Docker MySQL。联调时只执行固定 `SELECT`，不写入业务数据。
