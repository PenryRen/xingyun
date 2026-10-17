package com.mindskip.wdd.viewmodel.forum.comment;

import lombok.Data;

/**
 * @version 1.9.0
 * @description: 文章评论返回
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/1 10:45
 */
@Data
public class ForumCommentPageResponseVM {

    private Integer id;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 创建人
     */
    private String createUserStr;

    /**
     * 创建时间
     */
    private String createTime;

}
