package com.mindskip.wdd.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mindskip.wdd.configuration.mybaties.ExamPaperBuildConfigJsonTypeHandler;
import lombok.Getter;
import lombok.Setter;
import com.mindskip.wdd.domain.frame.ExamPaperBuildConfig;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 试卷组卷规则
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Getter
@Setter
@TableName(value = "t_exam_paper_build", autoResultMap = true)
public class ExamPaperBuild {
    private Long id;

    /**
     * 试卷分类
     */
    private Integer examPaperArchiveId;

    /**
     * 试卷名称
     */
    private String name;

    /**
     * 组卷类型
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
     * 考试时长
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
     * 试卷发布时间
     */
    private Date publishTime;

    /**
     * 试卷发布状态
     */
    private Integer buildStatus;

    /**
     * 创建者
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
     * 试卷id
     */
    private Long examPaperId;

    /**
     * 证书模板
     */
    private Integer credentialTemplateId;

    /**
     * 考试发布范围
     */
    private Integer rangeType;

    /**
     * 组卷规则配置
     */
    @TableField(typeHandler = ExamPaperBuildConfigJsonTypeHandler.class)
    private ExamPaperBuildConfig buildConfig;

    /**
     * 创建者部门
     */
    private Integer createDepartmentId;

    /**
     * 实训环境
     */
    private String vmType;

}