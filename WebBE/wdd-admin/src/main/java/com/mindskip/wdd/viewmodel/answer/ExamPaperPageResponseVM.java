package com.mindskip.wdd.viewmodel.answer;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 试卷分页返回
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class ExamPaperPageResponseVM {
    private Long id;

    /**
     * 试卷类型
     */
    private Integer buildType;

    /**
     * 试卷类型名称
     */
    private String buildTypeStr;

    /**
     * 试卷名称
     */
    private String name;

    /**
     * 待批改数量
     */
    private Integer judgeCount;

    /**
     * 完成数量
     */
    private Integer completeCount;

    /**
     * 所有数量
     */
    private Integer allCount;

    /**
     * 试卷分类
     */
    private String examPaperArchive;

    /**
     * 考试开始时间
     */
    private String limitStartTime;

    /**
     * 考试结束时间
     */
    private String limitEndTime;

    /**
     * 证书模板
     */
    private Integer credentialTemplateId;

}
