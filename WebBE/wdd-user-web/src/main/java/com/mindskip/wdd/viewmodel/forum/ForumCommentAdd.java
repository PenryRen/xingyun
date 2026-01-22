package com.mindskip.wdd.viewmodel.forum;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @version 1.9.0
 * @description: 文章评论新增
 * Copyright (C), 2024, 麒技团队
 * @date 2024/7/1 10:45
 */
@Data
public class ForumCommentAdd {

    /**
     * 文章分类id
     */
    private Integer forumArchiveId;

    /**
     * 文章id
     */
    @NotNull
    private Integer forumId;

    /**
     * 内容
     */
    @NotBlank
    private String content;

    /**
     * 上级评论id
     */
    private Long parentId;

}
