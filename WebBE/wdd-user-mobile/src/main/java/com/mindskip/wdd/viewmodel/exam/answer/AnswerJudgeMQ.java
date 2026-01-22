package com.mindskip.wdd.viewmodel.exam.answer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 试卷批改
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
@AllArgsConstructor
public class AnswerJudgeMQ {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 部门
     */
    private Integer departmentId;
    /**
     * 答卷信息
     */
    private ExamPaperAnswerInfoResponseVM examPaperAnswerRequestVM;
    /**
     * 创建时间
     */
    private Date createTime;
}
