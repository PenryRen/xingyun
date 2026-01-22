package com.mindskip.wdd.viewmodel.user;

import lombok.Data;

/**
 * @version 2.0.0
 * @description: 我的评论
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/12 10:45
 */
@Data
public class CommentPageResponseVM {

    private Integer forumId;

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
