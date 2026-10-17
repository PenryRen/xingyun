package com.mindskip.wdd.domain.frame;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 试卷构建内的题目
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ExamPaperBuildQuestion {
    /**
     * 题目id
     */
    private Long id;
    /**
     * 题目分数
     */
    private String score;
    /**
     * 题目内容id
     */
    private String questionFrameId;
}
