package com.mindskip.wdd.viewmodel.forum;

import lombok.Data;

/**
 * @version 1.9.0
 * @description: 文章编辑
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/1 10:45
 */
@Data
public class ForumEditRequestVM {

    private Integer id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 文章分类id
     */
    private Integer forumArchiveId;

}
