package com.mindskip.wdd.viewmodel.forum.comment;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

/**
 * @version 1.9.0
 * @description: 文章评论
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/1 10:45
 */
@Data
public class ForumCommentPageRequestVM extends BasePage {

    /**
     * 文章id
     */
    private Integer forumId;


    /**
     * 用户名
     */
    private String userName;
}
