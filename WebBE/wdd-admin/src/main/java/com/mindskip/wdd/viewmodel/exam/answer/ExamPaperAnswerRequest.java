package com.mindskip.wdd.viewmodel.exam.answer;

import lombok.Data;

/**
 * @version 7.1.0
 * @description: 答卷详情
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/8 10:45
 */
@Data
public class ExamPaperAnswerRequest {


    /**
     * sql 列
     */
    private String column = "count(*)";
    /**
     * 试卷构建配置Id
     */
    private Long examPaperBuildId;
    /**
     * 答卷状态
     */
    private Integer status;
    /**
     * 是否合格
     */
    private Boolean passed;


    public ExamPaperAnswerRequest(Long examPaperBuildId) {
        this.examPaperBuildId = examPaperBuildId;
    }

    public ExamPaperAnswerRequest(Long examPaperBuildId, Integer status) {
        this.examPaperBuildId = examPaperBuildId;
        this.status = status;
    }

    public ExamPaperAnswerRequest(Long examPaperBuildId, Integer status, Boolean passed) {
        this.examPaperBuildId = examPaperBuildId;
        this.status = status;
        this.passed = passed;
    }

    public ExamPaperAnswerRequest(String column, Long examPaperBuildId, Integer status) {
        this.column = column;
        this.examPaperBuildId = examPaperBuildId;
        this.status = status;
    }

    public ExamPaperAnswerRequest(String column, Long examPaperBuildId, Integer status, Boolean passed) {
        this.column = column;
        this.examPaperBuildId = examPaperBuildId;
        this.status = status;
        this.passed = passed;
    }


}
