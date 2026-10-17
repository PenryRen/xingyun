package com.mindskip.wdd.viewmodel.course.ware;

import lombok.Data;

/**
 * @version 8.5.0
 * @description: 课件题目
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/7 10:45
 */
@Data
public class CourseWareQuestionVM {
    /**
     * 题目Id
     */
    private Long questionId;

    private String questionFrameId;
    /**
     * 题干
     */
    private String title;
    /**
     * 视频题目格式化
     */
    private String anchorFormat;
    /**
     * 视频题目秒
     */
    private Integer anchorSecond;
}
