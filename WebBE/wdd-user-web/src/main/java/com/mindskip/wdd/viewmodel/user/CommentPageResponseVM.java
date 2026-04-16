package com.mindskip.wdd.viewmodel.user;

import lombok.Data;

/**
 * @version 2.0.0
 * @description: 我的评论
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/12 10:45
 */
@Data
public class CommentPageResponseVM {

    /**
     * 文章id
     */
    private Integer forumId;

    /**
     * 标题
     */
    private String title;

    private Long id;

    /**
     * 评论内容
     */
    private String content;


    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 回复评论内容
     */
    private String replyContent = "";
}
