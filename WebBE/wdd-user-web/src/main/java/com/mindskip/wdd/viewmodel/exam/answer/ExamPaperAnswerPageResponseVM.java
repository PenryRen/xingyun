package com.mindskip.wdd.viewmodel.exam.answer;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 答卷分页返回
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ExamPaperAnswerPageResponseVM {

    private Long id;

    /**
     * 试卷信息
     */
    private String paperName;

    /**
     * 系统批改分数
     */
    private String systemScore;

    /**
     * 用户最终得分
     */
    private String userScore;

    /**
     * 试卷总分
     */
    private String paperScore;

    /**
     * 正确题数
     */
    private Integer questionCorrect;

    /**
     * 题目总数
     */
    private Integer questionCount;

    /**
     * 耗时
     */
    private String doTime;

    /**
     * 答卷状态
     */
    private Integer status;

    private String statusStr;

    /**
     * 是否允许查看试卷
     */
    private Boolean watch;

    /**
     * 是否合格
     */
    private Boolean passed;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 合格分数线
     */
    private String passScore;
}
