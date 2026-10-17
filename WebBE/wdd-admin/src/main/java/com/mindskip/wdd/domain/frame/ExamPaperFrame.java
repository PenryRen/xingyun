package com.mindskip.wdd.domain.frame;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 试卷详情
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ExamPaperFrame implements Serializable {

    private static final long serialVersionUID = -2457260317007195868L;
    /**
     * 试卷id
     */
    private String id;

    /**
     * 试卷标题列表
     */
    private List<ExamPaperItemFrame> examPaperItemFrames;
}
