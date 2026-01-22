package com.mindskip.wdd.domain.frame;

import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 试卷构建标题
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class ExamPaperBuildTitle {

    /**
     * 试卷标题
     */
    private String name;

    /**
     * 试卷标题下的题目
     */
    private List<ExamPaperBuildQuestion> questionItems;
}
