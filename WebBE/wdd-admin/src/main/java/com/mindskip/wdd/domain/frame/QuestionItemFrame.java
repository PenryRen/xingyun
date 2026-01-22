package com.mindskip.wdd.domain.frame;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.7.0
 * @description: 题目选项
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class QuestionItemFrame implements Serializable {
    private static final long serialVersionUID = 2405862982551269814L;
    /**
     * 选项key
     */
    private Integer key;

    /**
     * 选项uuid
     */
    private String itemUuid;

    /**
     * 选项前缀
     */
    private String prefix;

    /**
     * 选项内容
     */
    private String content;

    /**
     * 选项分数
     */
    private Integer score;
}
