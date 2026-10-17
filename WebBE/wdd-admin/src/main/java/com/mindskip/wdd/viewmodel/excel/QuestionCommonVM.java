package com.mindskip.wdd.viewmodel.excel;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.7.0
 * @description: 题目公用属性
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class QuestionCommonVM implements Serializable {

    /**
     * 题目分类
     */
    private String level;

    /**
     * 题目解析
     */
    private String analyze;

    /**
     * 题目分数
     */
    private String score;

    /**
     * 题目难度
     */
    private Integer difficult;

    /**
     * 题目标答
     */
    private String correct;

}
