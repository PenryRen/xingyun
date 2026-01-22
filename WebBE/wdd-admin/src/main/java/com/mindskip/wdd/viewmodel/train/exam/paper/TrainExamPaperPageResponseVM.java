package com.mindskip.wdd.viewmodel.train.exam.paper;

import lombok.Data;

/**
 * @version 9.0.0
 * @description: 培训试卷返回
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/16 10:45
 */
@Data
public class TrainExamPaperPageResponseVM {

    private Integer id;

    /**
     * 课件名称
     */
    private String name;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private String createTime;

    private String level;

    /**
     * 试卷总分
     */
    private String score;

    /**
     * 题目总数
     */
    private Integer questionCount;

    /**
     * 考试时长
     */
    private Integer suggestTime;

    /**
     * 考试时长
     */
    private String suggestTimeStr;
}
