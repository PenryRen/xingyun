package com.mindskip.wdd.viewmodel.question;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @version 1.7.0
 * @description: 题目选项编辑
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class QuestionEditItemVM {

    /**
     * 选项UUID
     */
    private String itemUuid;

    /**
     * 选项前缀
     */
    @NotBlank
    private String prefix;

    /**
     * 选项内容
     */
    @NotBlank(message = "空内容不能为空")
    private String content;

    /**
     * 选项分数
     */
    private String score;

}
