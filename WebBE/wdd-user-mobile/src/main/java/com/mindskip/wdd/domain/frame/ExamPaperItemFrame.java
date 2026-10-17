package com.mindskip.wdd.domain.frame;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 试卷标题
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ExamPaperItemFrame implements Serializable {

    private static final long serialVersionUID = -3547074728146856986L;
    /**
     * 试卷标题
     */
    private String name;

    /**
     * 试卷标题下的题目列表
     */
    private List<ExamPaperItemQuestionFrame> examPaperItemQuestionFrames;
}
