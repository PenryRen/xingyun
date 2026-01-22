package com.mindskip.wdd.viewmodel.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @version 1.7.0
 * @description: 用户反馈
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class FeedbackRequestVM {

    /**
     * 联系方式
     */
    @NotBlank
    private String contact;

    /**
     * 反馈内容
     */
    @NotBlank
    private String content;

}
