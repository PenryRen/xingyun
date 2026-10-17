package com.mindskip.wdd.viewmodel.forum;

import lombok.Data;

import java.util.List;

/**
 * @version 1.9.0
 * @description: 文章评论分页返回
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/1 10:45
 */
@Data
public class ForumCommentPageResponseVM {
    private Long id;

    /**
     * 文章id
     */
    private Integer forumId;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 上级节点id
     */
    private Long parentId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 回复人用户名
     */
    private String replyUserName;

    /**
     * 回复人真实姓名
     */
    private String replyRealName;

    /**
     * 头像
     */
    private String imagePath;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 文章分类id
     */
    private Integer forumArchiveId;

    /**
     * 是否是自己创建的
     */
    private Boolean createByMe;

    /**
     * 子评论
     */
    private List<ForumCommentPageResponseVM> child;

}
