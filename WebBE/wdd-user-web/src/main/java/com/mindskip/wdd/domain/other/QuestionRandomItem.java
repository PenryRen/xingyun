package com.mindskip.wdd.domain.other;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 随机组卷题目
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class QuestionRandomItem {
    /**
     * 题目id
     */
    private Long id;

    /**
     * 题目内容id
     */
    private String questionFrameId;
}
