# 麟航智训（星云）数据库表结构说明

> 来源：`Base/init.sql`（Navicat 导出的 MySQL 8.0 dump，17 张表 + 1204 条种子数据）
> 实际库名：本机开发库为 `jxsx`（生产配置为 `wdd`）
> 字符集：`utf8mb4` / 排序规则 `utf8mb4_0900_ai_ci`

## 总览

| 模块 | 表名 | 用途 | 行数 |
|---|---|---|---|
| 公告 | `t_announcement` | 公告主表 | 18 |
| 公告 | `t_announcement_archive` | 公告分类（树形） | 8 |
| 公告 | `t_announcement_department` | 公告-部门可见范围 | 279 |
| 公告 | `t_announcement_read` | 公告已读记录 | 8 |
| 报名 | `t_apply` | 报名活动主表 | 40 |
| 报名 | `t_apply_archive` | 报名分类（树形） | 13 |
| 报名 | `t_apply_audit` | 报名审核记录 | 11 |
| 报名 | `t_apply_department` | 报名-部门范围 | 274 |
| 课件 | `t_course_ware` | 课件主表 | 56 |
| 课件 | `t_course_ware_archive` | 课件分类（树形） | 5 |
| 课件 | `t_course_ware_question` | 课件-题目锚点 | 71 |
| 课件 | `t_course_ware_watch` | 观看记录（汇总） | 43 |
| 课件 | `t_course_ware_watch_detail` | 观看明细（分次） | 250 |
| 考试 | `t_exam_paper` | 试卷主表 | 78 |
| 考试 | `t_exam_paper_answer` | 答卷（答题记录） | 23 |
| 基础 | `t_department` | 部门（树形组织） | 16 |
| 基础 | `t_credential_template` | 证书模板 | 11 |

## 通用约定

- 几乎每张表都带审计字段：`create_user`、`create_time`、`create_department_id`、`deleted`（`bit(1)` 逻辑删除）。
- MyBatis-Plus 配置：`table-prefix: t_`，逻辑删除字段为 `deleted`。
- 树形表（`t_*_archive`、`t_department`）统一用 `parent_id + level + item_order` 结构。
- 试卷/答卷的题目内容通过 `*_frame_id`（`varchar(36)`）外挂存储，不在本库表中。

---

## 1. 公告模块

### t_announcement（公告主表）

| 字段 | 类型 | 注释 |
|---|---|---|
| id | int | - |
| title | varchar(255) | - |
| content | text | - |
| create_user | int | - |
| create_time | datetime | - |
| deleted | bit(1) | - |
| image_src | varchar(255) | - |
| importanted | bit(1) | - |
| overhead | bit(1) | - |
| announcement_archive_id | int | 分类 |
| create_department_id | int | - |

### t_announcement_archive（公告分类，树形）

| 字段 | 类型 | 注释 |
|---|---|---|
| id | int | - |
| name | varchar(255) | - |
| create_user | int | - |
| create_time | datetime | - |
| deleted | bit(1) | - |
| parent_id | int | - |
| level | varchar(1000) | - |
| create_department_id | int | - |
| item_order | int | - |

### t_announcement_department（公告-部门可见范围）

| 字段 | 类型 | 注释 |
|---|---|---|
| id | int | - |
| announcement_id | int | - |
| department_id | int | - |
| deleted | bit(1) | - |
| create_user_id | int | - |
| create_department_id | int | - |

### t_announcement_read（公告已读记录）

| 字段 | 类型 | 注释 |
|---|---|---|
| id | bigint | - |
| announcement_id | int | - |
| create_user | int | - |
| create_time | datetime | - |
| create_department_id | int | - |

---

## 2. 报名模块

### t_apply（报名活动主表）

| 字段 | 类型 | 注释 |
|---|---|---|
| id | int | - |
| name | varchar(255) | 报名名称 |
| limit_end_time | datetime | 考试结束时间 |
| limit_start_time | datetime | 考试开始时间 |
| create_user | int | 创建人 |
| create_time | datetime | 创建时间 |
| deleted | bit(1) | 是否删除 |
| limited | bit(1) | 是否限制人数 |
| count | int | 限制报名人数量 |
| status | int | 报名状态：1.未发布 2.已发布 3.已关闭 |
| apply_end_time | datetime | 报名截止时间 |
| already_apply_count | int | 已报名人数 |
| apply_archive_id | int | 报名分类 |
| create_department_id | int | 创建者部门id |
| need_audit | bit(1) | 是否需要审核 |

### t_apply_archive（报名分类，树形）

| 字段 | 类型 | 注释 |
|---|---|---|
| id | int | - |
| name | varchar(255) | - |
| create_user | int | - |
| create_time | datetime | - |
| deleted | bit(1) | - |
| parent_id | int | - |
| level | varchar(1000) | - |
| create_department_id | int | - |
| item_order | int | - |

### t_apply_audit（报名审核记录）

| 字段 | 类型 | 注释 |
|---|---|---|
| id | bigint | - |
| apply_id | int | 报名id |
| user_id | int | 用户id |
| deleted | bit(1) | 是否删除 |
| create_time | datetime | 创建时间 |
| create_department_id | int | 创建人部门 |

### t_apply_department（报名-部门范围）

| 字段 | 类型 | 注释 |
|---|---|---|
| id | int | - |
| apply_id | int | - |
| department_id | int | - |
| deleted | bit(1) | - |
| create_user_id | int | - |
| create_department_id | int | - |

---

## 3. 课件模块

### t_course_ware（课件主表）

| 字段 | 类型 | 注释 |
|---|---|---|
| id | int | - |
| name | varchar(255) | - |
| create_user | int | - |
| create_time | datetime | - |
| deleted | bit(1) | - |
| file_type | int | 课件类型：1.视频 2.文档 |
| original_path | varchar(255) | 原始文档 |
| file_name | varchar(255) | - |
| description | varchar(500) | 描述 |
| preview_path | varchar(255) | 预览文件 |
| create_department_id | int | - |
| course_ware_archive_id | int | - |
| max_length | int | 课件时长 |
| vm_type | varchar(255) | 虚拟机环境 |

### t_course_ware_archive（课件分类，树形）

| 字段 | 类型 | 注释 |
|---|---|---|
| id | int | - |
| name | varchar(255) | - |
| create_user | int | - |
| create_time | datetime | - |
| deleted | bit(1) | - |
| parent_id | int | - |
| level | varchar(1000) | - |
| create_department_id | int | - |
| item_order | int | - |

### t_course_ware_question（课件-题目锚点）

| 字段 | 类型 | 注释 |
|---|---|---|
| id | bigint | - |
| course_ware_id | int | - |
| question_id | bigint | - |
| anchor_format | varchar(255) | - |
| anchor_second | int | - |
| create_user | int | - |
| create_time | datetime | - |
| create_department_id | int | - |
| deleted | bit(1) | - |
| question_frame_id | varchar(36) | 题目内容Id |

### t_course_ware_watch（观看记录，汇总）

| 字段 | 类型 | 注释 |
|---|---|---|
| id | bigint | - |
| course_ware_id | int | - |
| create_user | int | - |
| create_time | datetime | - |
| create_department_id | int | - |
| watch_total_length | bigint | - |
| watch_current_time | int | - |

### t_course_ware_watch_detail（观看明细，分次）

| 字段 | 类型 | 注释 |
|---|---|---|
| id | bigint | - |
| course_ware_id | int | - |
| create_user | int | - |
| create_time | datetime | - |
| create_department_id | int | - |
| watch_interval | int | 观看间隔（秒） |

---

## 4. 考试模块

### t_exam_paper（试卷主表）

| 字段 | 类型 | 注释 |
|---|---|---|
| id | bigint | - |
| name | varchar(255) | 试卷名称 |
| paper_type | int | 试卷类型(人工组卷、抽题组卷、随机组卷) |
| score | int | 试卷分数 |
| question_count | int | 题目数量 |
| suggest_time | int | 考试时长 |
| limit_start_time | datetime | 考试开始时间 |
| limit_end_time | datetime | 考试结束时间 |
| paper_frame_id | varchar(36) | 试卷结构信息表Id |
| create_user | int | 创建人 |
| create_time | datetime | 创建时间 |
| deleted | bit(1) | 是否删除 |
| question_item_mess | bit(1) | 选项打乱 |
| question_mess | bit(1) | 题目打乱 |
| cheat | bit(1) | 是否防作弊 |
| exam_paper_archive_id | int | 试卷分类 |
| pass_score | int | 合格分 |
| exam_paper_build_id | bigint | 试卷构建Id |
| credential_template_id | int | 证书模板 |
| capture | bit(1) | 考试抓拍 |
| create_department_id | int | 创建人部门 |
| watch | bit(1) | 是否允许查看答卷 |
| max_cheat_count | int | 最大作弊次数 |
| face_check | bit(1) | 人脸识别 |
| vm_type | varchar(255) | 虚拟机类别 |

### t_exam_paper_answer（答卷）

| 字段 | 类型 | 注释 |
|---|---|---|
| id | bigint | - |
| exam_paper_id | bigint | 试卷Id |
| paper_name | varchar(255) | 试卷名称 |
| paper_type | int | 试卷类型(人工组卷、抽题组卷、随机组卷) |
| system_score | int | 系统判分 |
| user_score | int | 用户得分 |
| paper_score | int | 试卷总分 |
| question_correct | int | 正确题数 |
| question_count | int | 题目总数 |
| do_time | int | 耗时 |
| status | int | 答卷状态 |
| create_user | int | 创建人 |
| create_time | datetime | 创建时间 |
| judge_user | int | 批改人 |
| answer_frame_id | varchar(36) | 答卷内容Id |
| exam_paper_archive_id | int | 试卷分类Id |
| passed | bit(1) | 是否合格 |
| pass_score | int | 合格分 |
| exam_paper_build_id | bigint | 试卷构建Id |
| credential_template_id | int | 证书模板 |
| limit_start_time | datetime | 考试开始时间 |
| limit_end_time | datetime | 考试结束时间 |
| create_department_id | int | 创建人部门 |
| deleted | bit(1) | 是否删除 |
| watch | bit(1) | 是否允许查看答卷 |
| preview_file_path | varchar(255) | 答卷导出 |
| exam_paper_child_id | bigint | 子试卷id |

---

## 5. 基础/其他

### t_department（部门，树形组织）

| 字段 | 类型 | 注释 |
|---|---|---|
| id | int | - |
| name | varchar(255) | 部门名称 |
| create_user | int | 创建人 |
| create_time | datetime | 创建时间 |
| deleted | bit(1) | 是否删除 |
| parent_id | int | 父级Id |
| level | varchar(1000) | 部门层级 |
| create_department_id | int | - |
| item_order | int | - |

### t_credential_template（证书模板）

| 字段 | 类型 | 注释 |
|---|---|---|
| id | int | - |
| name | varchar(255) | 模板名称 |
| company | varchar(255) | - |
| template_image_path | varchar(255) | 模板图片地址 |
| create_user | int | - |
| create_time | datetime | - |
| deleted | bit(1) | - |
| create_department_id | int | - |
| configuration | json | - |

---

## 关联关系

- 树形分类：`t_announcement_archive` / `t_apply_archive` / `t_course_ware_archive` / `t_department`，均以 `parent_id` 自关联。
- 可见范围（多对多）：`t_announcement_department`、`t_apply_department` 关联 `t_department`。
- 课件-题目：`t_course_ware_question` 关联 `t_course_ware` 与题目（`question_id`/`question_frame_id`）。
- 试卷-答卷：`t_exam_paper_answer.exam_paper_id` → `t_exam_paper.id`。
- AI_AGENT 只读查询依赖：`t_exam_paper_answer`（`user_score`/`paper_score`/`question_correct`/`question_count`/`status`）。
