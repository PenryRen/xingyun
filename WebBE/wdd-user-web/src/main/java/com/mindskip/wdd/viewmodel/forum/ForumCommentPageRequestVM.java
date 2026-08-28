package com.mindskip.wdd.viewmodel.forum;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @version 1.9.0
 * @description: 文章评论分页查询
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/1 10:45
 */
@Data
public class ForumCommentPageRequestVM extends BasePage {

    /**
     * 文章id
     */
    @NotNull
    private Integer forumId;
}
