package com.mindskip.wdd.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mindskip.wdd.configuration.mybaties.ExamPaperBuildConfigJsonTypeHandler;
import com.mindskip.wdd.domain.frame.ExamPaperBuildConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 9.5.0
 * @description: 模拟练习规则表
 * Copyright (C), 2025, 麟航团队
 * @date 2025/09/11 10:45
 */
@Getter
@Setter
@TableName(value = "t_practice_build", autoResultMap = true)
public class PracticeBuild {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 试卷分类Id
     */
    private Integer examPaperArchiveId;

    /**
     * 试卷名称
     */
    private String name;

    /**
     * 1.人工组卷 2.抽题组卷 3.随机组卷
     */
    private Integer buildType;

    /**
     * 考试合格分数
     */
    private Integer passScore;

    /**
     * 试卷总分
     */
    private Integer score;

    /**
     * 题目总数
     */
    private Integer questionCount;

    /**
     * 试卷时长
     */
    private Integer suggestTime;

    /**
     * 考试开始时间
     */
    private Date limitStartTime;

    /**
     * 考试结束时间
     */
    private Date limitEndTime;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 试卷Id
     */
    private Long practiceExamPaperId;

    /**
     * 组卷规则配置
     */
    @TableField(typeHandler = ExamPaperBuildConfigJsonTypeHandler.class)
    private ExamPaperBuildConfig buildConfig;

    /**
     * 创建人部门
     */
    private Integer createDepartmentId;
}
