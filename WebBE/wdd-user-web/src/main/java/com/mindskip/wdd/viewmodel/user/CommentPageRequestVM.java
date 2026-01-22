package com.mindskip.wdd.viewmodel.user;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

/**
 * @version 2.0.0
 * @description: 我的评论
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/12 10:45
 */
@Data
public class CommentPageRequestVM extends BasePage {
    /**
     * 用户id
     */
    private Integer userId;
}
