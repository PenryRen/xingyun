package com.mindskip.wdd.viewmodel.exam.paper;

import com.mindskip.wdd.domain.frame.QuestionFrame;
import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 试卷标题
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ExamPaperDoTitle {
    /**
     * 标题
     */
    private String name;
    /**
     * 题目信息
     */
    private List<QuestionFrame> questionFrameList;
}
