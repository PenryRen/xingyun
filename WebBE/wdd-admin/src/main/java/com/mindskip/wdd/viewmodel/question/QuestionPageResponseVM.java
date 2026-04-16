package com.mindskip.wdd.viewmodel.question;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 题目分页返回
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class QuestionPageResponseVM {

    private Long id;

    private String questionFrameId;

    /**
     * 题干
     */
    private String title;

    /**
     * 题型
     */
    private Integer questionType;

    /**
     * 分数
     */
    private String score;

    /**
     * 难度
     */
    private Integer difficult;

    private String difficultStr;

    /**
     * 题目分类
     */
    private String questionArchive;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 题型
     */
    private String typeEnumStr;

    private QuestionEditRequestVM question = new QuestionEditRequestVM();
}
