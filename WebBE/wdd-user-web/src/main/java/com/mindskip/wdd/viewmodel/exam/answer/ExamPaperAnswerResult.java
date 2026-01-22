package com.mindskip.wdd.viewmodel.exam.answer;

import com.mindskip.wdd.service.enums.ResultEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.7.0
 * @description: 试卷提交结果
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
@NoArgsConstructor
public class ExamPaperAnswerResult {

    public ExamPaperAnswerResult(ResultEnum resultEnum, String message) {
        this.resultEnum = resultEnum;
        this.message = message;
    }

    /**
     * 操作结果
     */
    private ResultEnum resultEnum;
    /**
     * 操作消息
     */
    private String message;

    /**
     * 操作状态
     */
    private Integer status;
}
