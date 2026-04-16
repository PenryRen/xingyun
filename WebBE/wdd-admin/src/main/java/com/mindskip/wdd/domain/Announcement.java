package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 通知公告
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Getter
@Setter
public class Announcement {
    private Integer id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
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
     * 公告封面
     */
    private String imageSrc;

    /**
     * 是否重要
     */
    private Boolean importanted;

    /**
     * 是否顶置
     */
    private Boolean overhead;

    /**
     * 公告分类
     */
    private Integer announcementArchiveId;

    /**
     * 创建人部门
     */
    private Integer createDepartmentId;
}