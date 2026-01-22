package com.mindskip.wdd.viewmodel.forum;

import lombok.Data;

/**
 * @version 1.9.0
 * @description: 文章分页返回
 * Copyright (C), 2024, 麒技团队
 * @date 2024/7/1 10:45
 */
@Data
public class ForumPageResponseVM {
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
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 头像地址
     */
    private String imagePath;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 文章分类
     */
    private String forumArchive;

    /**
     * 文章分类id
     */
    private Integer forumArchiveId;

}
