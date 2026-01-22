package com.mindskip.wdd.viewmodel.announcement;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 公告分页返回
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class AnnouncementPageResponseVM {

    private Integer id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建用户名
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
     * 封面
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
}
