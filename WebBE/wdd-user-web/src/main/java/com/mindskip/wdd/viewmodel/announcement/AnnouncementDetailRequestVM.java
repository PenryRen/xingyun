package com.mindskip.wdd.viewmodel.announcement;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 公告详情
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class AnnouncementDetailRequestVM {

    private Integer id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告封面
     */
    private String imageSrc;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 创建人用户名
     */
    private String createUserName;

    /**
     * 创建人真实姓名
     */
    private String createRealName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 是否重要
     */
    private Boolean importanted;

    /**
     * 是否顶置
     */
    private Boolean overhead;
}
