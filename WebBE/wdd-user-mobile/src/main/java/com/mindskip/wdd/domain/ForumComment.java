package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 1.9.0
 * @description: 文章评论
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/1 10:45
 */
@Getter
@Setter
public class ForumComment {
    private Long id;

    /**
     * 文章id
     */
    private Integer forumId;

    /**
     * 文章分类id
     */
    private Integer forumArchiveId;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 上级评论id
     */
    private Long parentId;

    /**
     * 创建者
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人部门
     */
    private Integer createDepartmentId;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 评论id层级
     */
    private String idLevel;

    /**
     * 回复人
     */
    private Integer replyUser;

    /**
     * 回复人部门
     */
    private Integer replyDepartmentId;
}