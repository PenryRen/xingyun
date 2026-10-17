package com.mindskip.wdd.viewmodel.announcement;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 公告基本信息
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class AnnouncementInfoVM {

    private Integer id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 创建者
     */
    private String createUser;

    /**
     * 创建者用户名
     */
    private String createUserName;

    /**
     * 创建者真实姓名
     */
    private String createRealName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 公告封面
     */
    private String imageSrc;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 是否重要
     */
    private Boolean importanted;

    /**
     * 是否顶置
     */
    private Boolean overhead;

    /**
     * 是否重要格式化
     */
    private String importantedStr;

    /**
     * 是否顶置格式化
     */
    private String overheadStr;

    /**
     * 发部部门名称
     */
    private String departmentNameList;

    /**
     * 部门用户人数
     */
    private Integer departmentUserCount;

    /**
     * 公告分类
     */
    private String announcementArchive;
}

