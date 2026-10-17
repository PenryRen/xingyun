# 芯知天枢 — 实训教学管理平台

基于 Spring Boot + Vue 3 + FastAPI 的全栈实训教学管理平台，集成考试管理、培训课程、课件资源、交流社区、AI 学习中心等核心功能，面向院校实训教学场景提供一站式解决方案。

***

## Demo 体验

| 地址         | URL                          | 说明           |
| ---------- | ---------------------------- | ------------ |
| 用户端（学生侧）  | http://121.89.83.87          | 学生学习与考试平台     |
| 管理端（教师侧）  | http://121.89.83.87/admin    | 教学管理后台        |
| AI 学习中心   | http://121.89.83.87/#/ai     | AI 辅助学习（需先登录） |

### 测试账号

| 角色   | 账号       | 密码           | 权限                     |
| ---- | -------- | ------------ | ---------------------- |
| 管理员  | admin    | ueit!@#$1234 | 全部管理功能                 |
| 学生   | student  | ueit!@#$1234 | 考试、培训、课件、AI 学习中心等用户功能 |

> **提示**：AI 学习中心需先登录用户端后在导航栏点击「AI 学习中心」进入。AI 助教基于讯飞星火 4.0Ultra 模型，支持课程知识库问答和学情分析。

***

## 项目概览

| 项目信息 | 说明                                |
| ---- | --------------------------------- |
| 项目名称 | 芯知天枢实训平台                          |
| 版本   | 9.5.0                             |
| 定位   | 院校实训教学管理平台                        |
| 技术架构 | 前后端分离 + AI 智能体                    |
| 开发语言 | Java 8 / TypeScript / Python 3.11 |

***

## 技术栈

### 后端（Java）

| 技术           | 版本          | 用途                  |
| ------------ | ----------- | ------------------- |
| Spring Boot  | 2.7.14      | 应用框架                |
| MyBatis Plus | 3.5.3.1     | ORM 框架              |
| MySQL        | 8.0.33      | 关系型数据库              |
| Redis        | 7.x         | 缓存与会话管理             |
| Hutool       | 5.7.7       | Java 工具库（含 RSA 加解密） |
| PageHelper   | 1.4.1       | 分页插件                |
| MapStruct    | 1.4.2.Final | 对象映射                |
| Kaptcha      | 2.3.2       | 验证码                 |
| Lombok       | 1.18.34     | 代码简化                |
| 阿里云 OSS SDK  | 3.17.4      | 对象存储（可选）            |

### 前端（Vue 3）

| 技术           | 用途       |
| ------------ | -------- |
| Vue 3        | 前端框架     |
| Vite         | 构建工具     |
| Element Plus | UI 组件库   |
| Vue Router   | 路由管理     |
| Pinia        | 状态管理     |
| Axios        | HTTP 客户端 |
| wangEditor   | 富文本编辑器   |
| ECharts      | 数据可视化    |
| @vueuse/core | 组合式工具函数  |

### AI 后端（Python）

| 技术            | 用途          |
| ------------- | ----------- |
| FastAPI       | Web 框架      |
| Uvicorn       | ASGI 服务器    |
| LangChain     | LLM 应用框架    |
| LangGraph     | Agent 工作流编排 |
| OpenAI SDK    | 大模型 API 调用  |
| SQLAlchemy    | 数据库 ORM     |
| PyMySQL       | MySQL 驱动    |
| python-dotenv | 环境变量管理      |

### 基础设施

| 技术     | 用途                |
| ------ | ----------------- |
| Nginx  | 反向代理 + 静态资源服务     |
| Podman | 容器管理（MySQL、Redis） |
| Maven  | Java 依赖管理与构建      |
| npm    | 前端依赖管理            |

***

## 项目结构

```
xingyun-develop/
├── WebBE/                              # Java 后端（Maven 多模块）
│   ├── pom.xml                         # 根 POM（groupId=com.mindskip, artifactId=wdd, version=9.5.0）
│   ├── wdd-user-web/                   # 用户端后端服务（端口 16000）
│   ├── wdd-admin/                      # 管理端后端服务（端口 16002）
│   └── wdd-user-mobile/               # 移动端后端服务（端口 6007）
│
├── AI_AGENT/                           # AI 智能体模块
│   ├── WebFE/                          # 前端源码
│   │   ├── wdd-user-web/               # 用户端前端（Vite 构建）
│   │   └── wdd-admin/                  # 管理端前端（Vite 构建）
│   └── projects/                       # AI 后端（FastAPI）
│       ├── .env                        # 环境配置
│       ├── requirements.txt            # Python 依赖清单
│       └── src/                        # 源代码
│           ├── main.py                 # FastAPI 应用入口（端口 5000）
│           ├── agents/                 # LangChain Agent 定义
│           └── graphs/                 # LangGraph 工作流
│
├── WebFE/                              # 旧版前端源码（参考用）
│   ├── wdd-user-web/
│   └── wdd-admin/
│
├── deploy/                             # 部署配置
│   ├── nginx/xingyun.conf              # Nginx 反向代理配置模板
│   └── scripts/                       # 部署辅助脚本
│
├── Base/                               # 数据库脚本
│   ├── init.sql                        # 建表语句 + 测试数据
│   └── scripts/
│       └── add_one_year_all_date_columns.sql  # 日期延期脚本
│
└── one-click-deploy.sh                 # Linux 一键部署脚本
```

***

## 功能模块

### 用户端（学生侧）

面向学生的学习与考试平台，提供以下功能：

| 模块      | 路由                    | 说明                        |
| ------- | --------------------- | ------------------------- |
| 首页      | `/index`              | 平台首页，展示通知公告               |
| 通知公告    | `/announcement/index` | 查看平台公告通知                  |
| 交流圈     | `/forum/index`        | 学生交流讨论社区                  |
| 报名中心    | `/apply/index`        | 考试与活动报名                   |
| 试卷中心    | `/paper/index`        | 在线考试试卷列表                  |
| 培训中心    | `/train/index`        | 培训课程学习                    |
| 考试记录    | `/record/index`       | 历史考试成绩查询                  |
| AI 学习中心 | `/ai/index`           | AI 辅助学习（含考情分析、学情档案、AI 助教） |
| 课件中心    | `/course/ware/index`  | 课件资源浏览                    |
| 个人信息    | `/user-info/index`    | 个人资料管理                    |
| 登录/注册   | `/login` `/register`  | 用户认证                      |

### 管理端（教师/管理员侧）

面向教师和管理员的运营管理后台：

| 模块   | API 前缀                    | 说明                    |
| ---- | ------------------------- | --------------------- |
| 仪表盘  | `/api/dashboard`          | 首页数据统计与系统信息           |
| 用户管理 | `/api/user`               | 学生/教师管理，批量导入          |
| 角色管理 | `/api/role`               | 角色与权限分配               |
| 菜单管理 | `/api/menu`               | 后台菜单配置                |
| 部门管理 | `/api/department`         | 组织架构管理                |
| 公告管理 | `/api/announcement`       | 公告发布与分类管理             |
| 题目管理 | `/api/question`           | 题库管理，支持 Excel/Word 导入 |
| 试卷管理 | `/api/exam/paper`         | 试卷组卷与发布               |
| 试卷组卷 | `/api/exam/paper/build`   | 智能组卷与手动组卷             |
| 考试监控 | `/api/exam/paper/monitor` | 实时考试监控                |
| 答卷管理 | `/api/exam/paper/answer`  | 答卷批改与成绩管理             |
| 培训管理 | `/api/train/course`       | 培训课程管理                |
| 课件管理 | `/api/course/ware`        | 课件上传与分类               |
| 报名管理 | `/api/apply`              | 报名审核管理                |
| 论坛管理 | `/api/forum`              | 交流圈内容管理               |
| 证书管理 | `/api/credential`         | 合格证书模板与发放             |
| 系统日志 | `/api/user/log`           | 用户操作日志                |
| 云桌面  | `/api/vmWare`             | 虚拟机桌面管理               |

### AI 学习中心

基于 LangChain + LangGraph 构建的 AI 辅助学习模块：

| 功能        | API 路径                              | 说明                |
| --------- | ----------------------------------- | ----------------- |
| AI 助教对话   | `/learning/assistant`               | 智能问答，基于课程知识库和学情数据 |
| 会话重置      | `/learning/assistant/session/reset` | 清空对话历史            |
| 学习工作区     | `/learning/workspace`               | 获取结构化学习分析数据       |
| Agent 执行  | `/run`                              | 执行 LangGraph 工作流  |
| 流式执行      | `/stream_run`                       | SSE 流式输出执行结果      |
| 取消执行      | `/cancel/{run_id}`                  | 取消正在运行的 Agent     |
| 节点执行      | `/node_run/{node_id}`               | 按 Graph 节点单独执行    |
| 参数 Schema | `/graph_parameter`                  | 获取工作流输入/输出结构      |

**AI 助教能力**：

- 基于课程知识库回答学生提问

- 查询学生考试记录和学情数据

- 提供个性化学习建议

- 支持流式输出（SSE），实时显示 AI 回复

***

## 后端 API 接口

### 用户端后端（wdd-user-web，端口 16000）

| Controller                | 路径前缀                     | 主要功能              |
| ------------------------- | ------------------------ | ----------------- |
| UserController            | `/api/user`              | 用户登录、注册、信息查询      |
| AnnouncementController    | `/api/announcement`      | 公告列表、详情、阅读记录      |
| ApplyController           | `/api/apply`             | 报名列表、提交报名         |
| ExamPaperController       | `/api/exam/paper`        | 试卷列表、考试答题         |
| ExamPaperAnswerController | `/api/exam/paper/answer` | 提交答案、查询成绩         |
| ForumController           | `/api/forum`             | 论坛帖子列表、发帖         |
| TrainController           | `/api/train`             | 培训课程列表、学习进度       |
| TrainExamPaperController  | `/api/train/exam/paper`  | 培训关联考试            |
| PracticeController        | `/api/practice`          | 模拟练习              |
| CourseWareController      | `/api/course/ware`       | 课件列表、观看记录         |
| CredentialController      | `/api/credential`        | 合格证书查询            |
| UploadController          | `/api/upload`            | 文件上传（图片/视频/音频/附件） |
| KaptchaController         | `/api/kaptcha`           | 验证码生成与刷新          |
| VmWareController          | `/api/vmWare`            | 云桌面操作             |

### 管理端后端（wdd-admin，端口 16002）

| Controller                    | 路径前缀                        | 主要功能                  |
| ----------------------------- | --------------------------- | --------------------- |
| DashboardController           | `/api/dashboard`            | 统计数据、系统信息             |
| UserController                | `/api/user`                 | 用户 CRUD、批量导入          |
| RoleController                | `/api/role`                 | 角色 CRUD               |
| MenuController                | `/api/menu`                 | 菜单树管理                 |
| DepartmentController          | `/api/department`           | 部门树管理                 |
| AnnouncementController        | `/api/announcement`         | 公告 CRUD               |
| AnnouncementArchiveController | `/api/announcement/archive` | 公告分类管理                |
| QuestionController            | `/api/question`             | 题目 CRUD、Excel/Word 导入 |
| QuestionArchiveController     | `/api/question/archive`     | 题目分类管理                |
| ExamPaperBuildController      | `/api/exam/paper/build`     | 组卷管理                  |
| ExamPaperMonitorController    | `/api/exam/paper/monitor`   | 考试实时监控                |
| ExamPaperAnswerController     | `/api/exam/paper/answer`    | 答卷批改                  |
| ExamPaperArchiveController    | `/api/exam/paper/archive`   | 试卷分类管理                |
| TrainController               | `/api/train/course`         | 培训课程 CRUD             |
| TrainArchiveController        | `/api/train/archive`        | 培训分类管理                |
| CourseWareController          | `/api/course/ware`          | 课件上传管理                |
| CourseWareArchiveController   | `/api/course/ware/archive`  | 课件分类管理                |
| ApplyController               | `/api/apply`                | 报名管理                  |
| ApplyArchiveController        | `/api/apply/archive`        | 报名分类管理                |
| ForumController               | `/api/forum`                | 论坛管理                  |
| ForumArchiveController        | `/api/forum/archive`        | 论坛分类管理                |
| CredentialController          | `/api/credential`           | 证书模板管理                |
| ProfileController             | `/api/profile`              | 个人资料管理                |
| UserEventLogController        | `/api/user/log`             | 操作日志查询                |
| VmWareController              | `/api/vmWare`               | 云桌面模板管理               |
| SshController                 | `/api/ssh`                  | SSH 连接管理              |
| UploadController              | `/api/upload`               | 文件上传                  |

***

## 数据库设计

### 核心系统表

| 表名                       | 说明              |
| ------------------------ | --------------- |
| `t_user`                 | 用户信息（学生/教师/管理员） |
| `t_role`                 | 角色定义            |
| `t_menu`                 | 后台菜单配置          |
| `t_department`           | 部门/组织架构         |
| `t_user_token`           | 用户登录 Token      |
| `t_user_event_log`       | 用户操作日志          |
| `t_sys_config`           | 系统配置参数          |
| `t_role_menu_permission` | 角色-菜单权限关联       |

### 业务表

| 表名                           | 说明              |
| ---------------------------- | --------------- |
| `t_announcement`             | 通知公告            |
| `t_announcement_archive`     | 公告分类            |
| `t_announcement_department`  | 公告-部门关联（控制可见范围） |
| `t_announcement_read`        | 公告阅读记录          |
| `t_apply`                    | 报名活动            |
| `t_apply_archive`            | 报名分类            |
| `t_apply_audit`              | 报名审核记录          |
| `t_apply_department`         | 报名-部门关联         |
| `t_exam_paper`               | 试卷              |
| `t_exam_paper_archive`       | 试卷分类            |
| `t_exam_paper_user`          | 试卷-用户分配（个人可见范围） |
| `t_exam_paper_department`    | 试卷-部门分配（部门可见范围） |
| `t_exam_paper_answer`        | 考试答卷            |
| `t_course_ware`              | 课件资源            |
| `t_course_ware_archive`      | 课件分类            |
| `t_course_ware_question`     | 课件关联题目          |
| `t_course_ware_watch`        | 课件观看记录          |
| `t_course_ware_watch_detail` | 观看明细            |
| `t_credential_template`      | 证书模板            |
| `t_forum`                    | 论坛帖子            |

### AI 相关表

| 表名                              | 说明          |
| ------------------------------- | ----------- |
| `ai_student`                    | 学生信息（AI 模块） |
| `ai_question`                   | 题目库（AI 模块）  |
| `ai_exam_record`                | 考试记录        |
| `ai_answer_record`              | 答题记录        |
| `ai_learning_profile`           | 学情档案        |
| `ai_student_knowledge_progress` | 知识点掌握进度     |

> 试卷可见范围说明：系统没有"所有人可见"选项，试卷必须通过 `t_exam_paper_department`（部门分配）或 `t_exam_paper_user`（个人分配）指定可见范围。

***

## 本地开发

### 环境要求

| 工具      | 版本   |
| ------- | ---- |
| JDK     | 1.8  |
| Maven   | 3.9+ |
| Node.js | 16+  |
| Python  | 3.11 |
| MySQL   | 8.0+ |
| Redis   | 7.0+ |

### 后端启动

```bash
cd WebBE

# 开发环境启动（使用 dev profile，默认连接本地 MySQL/Redis）
mvn clean install -Dmaven.test.skip=true -pl wdd-user-web,wdd-admin -am

# 用户端后端
cd wdd-user-web
mvn spring-boot:run

# 管理端后端
cd wdd-admin
mvn spring-boot:run
```

### 前端启动

```bash
# 用户端前端
cd AI_AGENT/WebFE/wdd-user-web
npm install
npm run serve    # 开发服务器，端口 16001

# 管理端前端
cd AI_AGENT/WebFE/wdd-admin
npm install
npm run serve    # 开发服务器，端口 16003
```

### AI 后端启动

```bash
cd AI_AGENT/projects

# 创建虚拟环境
python3.11 -m venv .venv
source .venv/bin/activate    # Linux/macOS
# .venv\Scripts\activate     # Windows

# 安装依赖
pip install -r requirements.txt

# 配置环境变量
cp .env.example .env
# 编辑 .env，填入 MySQL 连接信息和 API Key

# 启动
python src/main.py            # 默认端口 5000
```

### 数据库初始化

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE wdd CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 导入建表语句和测试数据
mysql -u root -p --default-character-set=utf8mb4 wdd < Base/init.sql
```

***

## 前端构建

```bash
# 用户端前端
cd AI_AGENT/WebFE/wdd-user-web
npm run build    # 产物输出到 ueit-user-web/ 目录

# 管理端前端
cd AI_AGENT/WebFE/wdd-admin
npm run build    # 产物输出到 ueit-admin/ 目录
```

> **注意**：Vite 配置中 `outDir` 为 `ueit-user-web` 和 `ueit-admin`，不是默认的 `dist`。

***

## 后端构建

```bash
cd WebBE

# 清理并打包（生产环境）
mvn clean install -Dmaven.test.skip=true -Pprod -pl wdd-user-web,wdd-admin -am

# 产物
# wdd-user-web/target/ueit-user-web.jar
# wdd-admin/target/ueit-admin.jar
```

***

## 项目配置

### 后端配置文件

| 文件                     | 说明                        |
| ---------------------- | ------------------------- |
| `application.yml`      | 基础配置（开发环境默认）              |
| `application-prod.yml` | 生产环境配置（端口、数据库、Redis、文件路径） |

关键配置项支持环境变量覆盖：

```yaml
spring:
  datasource:
    url: ${MYSQL_URL:jdbc:mysql://127.0.0.1:3306/wdd...}
    username: ${MYSQL_USERNAME:root}
    password: ${MYSQL_PASSWORD:}
  redis:
    host: ${REDIS_HOST:127.0.0.1}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PASSWORD:}
    database: ${REDIS_DB:1}
```

### 前端配置

用户端前端环境变量文件 `.env`（开发）/ `.env.production`（生产）：

```ini
VITE_APP_SYSTEM_NAME=麟航智训
VITE_APP_PAIR_ONE_PUBLIC_KEY=<RSA公钥>
```

### AI 后端配置

`AI_AGENT/projects/.env`：

```ini
XINGYUN_MYSQL_HOST=127.0.0.1
XINGYUN_MYSQL_PORT=3306
XINGYUN_MYSQL_DATABASE=wdd
XINGYUN_MYSQL_USERNAME=wdd
XINGYUN_MYSQL_PASSWORD=<密码>
OPENAI_API_KEY=<API Key>
OPENAI_BASE_URL=https://api.deepseek.com
MODEL_NAME=deepseek-chat
ENV=development
```

***

## 安全设计

### 认证与授权

- 基于 Token 的认证机制，Token 存储于 Redis

- 角色权限控制：管理员（ADMIN=2）、普通用户（EMPLOYEE=1）

- 密码传输使用 RSA 加密，后端使用 Hutool RSA API 解密

- 登录验证码（Kaptcha）防止暴力破解

### 数据安全

- MySQL/Redis 端口绑定 127.0.0.1，不对外暴露

- 数据库用户最小权限原则（仅 SELECT/INSERT/UPDATE/DELETE）

- Nginx 隐藏版本号、限制上传大小、添加安全响应头

- AI 接口限流（令牌桶，每秒 2 次，突发 10 次）

***

## License

本项目为私有项目，版权归开发团队所有。

***

## 团队

河南财经政法大学麒麟团队
