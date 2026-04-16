package com.mindskip.wdd.domain.frame;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 试卷发布范围 - 报名
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ExamPaperApplySelect {
    /**
     * 报名id
     */
    private Integer id;
    /**
     * 报名名称
     */
    private String name;
    /**
     * 考试开始时间
     */
    private String limitStartTime;
    /**
     * 考试结束时间
     */
    private String limitEndTime;

}
