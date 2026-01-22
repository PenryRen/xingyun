package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 1.9.0
 * @description: 文章
 * Copyright (C), 2024, 麒技团队
 * @date 2024/7/1 10:45
 */
@Getter
@Setter
public class Forum {
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
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 文章分类id
     */
    private Integer forumArchiveId;

    /**
     * 创建人部门
     */
    private Integer createDepartmentId;

}