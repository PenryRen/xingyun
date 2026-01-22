package com.mindskip.wdd.service;

import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import com.mindskip.wdd.domain.frame.QuestionFrame;

/**
 * @version 1.7.0
 * @description: 答卷题目
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface ExamPaperQuestionAnswerService {

    /**
     * 题目选项还原
     *
     * @param questionFrame       the question frame
     * @param questionAnswerFrame the question answer frame
     */
    void questionItemMessRestore(QuestionFrame questionFrame, QuestionAnswerFrame questionAnswerFrame);
}
