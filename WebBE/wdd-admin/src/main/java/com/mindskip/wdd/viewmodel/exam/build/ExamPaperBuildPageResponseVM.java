package com.mindskip.wdd.viewmodel.exam.build;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 组卷规则分页返回
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ExamPaperBuildPageResponseVM {
    private Long id;

    /**
     * 试卷类型
     */
    private Integer buildType;

    /**
     * 试卷类型
     */
    private String buildTypeStr;

    /**
     * 试卷名称
     */
    private String name;

    /**
     * 及格分
     */
    private String passScore;

    /**
     * 试卷总分
     */
    private String score;

    /**
     * 题目总数
     */
    private Integer questionCount;

    /**
     * 考试时长
     */
    private Integer suggestTime;

    /**
     * 考试时长
     */
    private String suggestTimeStr;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 考试开始时间
     */
    private String limitStartTime;

    /**
     * 考试结束时间
     */
    private String limitEndTime;

    /**
     * 发布状态
     */
    private Integer buildStatus;

    /**
     * 发布状态
     */
    private String buildStatusStr;

    /**
     * 试卷分类
     */
    private String examPaperArchive;

}
