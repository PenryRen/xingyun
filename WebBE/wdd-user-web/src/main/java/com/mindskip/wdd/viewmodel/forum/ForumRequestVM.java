package com.mindskip.wdd.viewmodel.forum;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @version 1.9.0
 * @description: 文章
 * Copyright (C), 2024, 麒技团队
 * @date 2024/7/1 10:45
 */
@Data
public class ForumRequestVM {


    /**
     * 文章分类
     */
    private Integer forumArchiveId;

    /**
     * 标题
     */
    @NotBlank
    private String title;

    /**
     * 内容
     */
    @NotBlank
    private String content;

}
