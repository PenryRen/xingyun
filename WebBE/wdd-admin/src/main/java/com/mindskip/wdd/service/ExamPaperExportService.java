package com.mindskip.wdd.service;

import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerEditResponseVM;

/**
 * @version 5.9.0
 * @description: 试卷导出
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/3 11:45
 */
public interface ExamPaperExportService {


    /**
     * 答卷导出
     *
     * @param examPaperAnswerEditResponseVM examPaperAnswerEditResponseVM
     * @return {@link String}
     */
    String answerExport(ExamPaperAnswerEditResponseVM examPaperAnswerEditResponseVM);
}
