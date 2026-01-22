package com.mindskip.wdd.viewmodel.exam.answer;

import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoPaperVM;
import lombok.Data;

/**
 * @version 1.7.0
 * @description: 答卷信息
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class ExamPaperAnswerEditResponseVM {
    /**
     * 试卷信息
     */
    private ExamPaperDoPaperVM paper;
    /**
     * 答卷信息
     */
    private ExamPaperAnswerInfoResponseVM answer;
}
