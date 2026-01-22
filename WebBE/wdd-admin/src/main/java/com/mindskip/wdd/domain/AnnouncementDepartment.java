package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @version 1.7.0
 * @description: 公告发布部门
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Getter
@Setter
public class AnnouncementDepartment {
    private Integer id;

    /**
     * 公告id
     */
    private Integer announcementId;

    /**
     * 部门id
     */
    private Integer departmentId;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 创建者id
     */
    private Integer createUserId;

    /**
     * 创建者部门
     */
    private Integer createDepartmentId;
}