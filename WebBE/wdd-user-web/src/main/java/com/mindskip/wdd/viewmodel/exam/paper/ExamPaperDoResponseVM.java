package com.mindskip.wdd.viewmodel.exam.paper;

import com.mindskip.wdd.domain.frame.ExamPaperAnswerFrame;
import lombok.Data;

/**
 * @version 1.7.0
 * @description: 答卷信息
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ExamPaperDoResponseVM {
    /**
     * 作弊次数
     */
    private Integer cheatCount;
    /**
     * 剩余时间
     */
    private Integer remainTime;
    /**
     * 试卷信息
     */
    private ExamPaperDoPaperVM paper;
    /**
     * 答卷信息
     */
    private ExamPaperAnswerFrame answer;

    /**
     * 首次进入考试页面
     */
    private Boolean initFirst;
}
