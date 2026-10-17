package com.mindskip.wdd.viewmodel.exam.answer;

import com.mindskip.wdd.domain.frame.ExamPaperAnswerFrame;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 答卷信息
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
@AllArgsConstructor
public class AnswerMQ {
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
     * 答卷内容
     */
    private ExamPaperAnswerFrame answerRequestVM;
    /**
     * 创建时间
     */
    private Date createTime;
}
