package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 公告阅读
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Getter
@Setter
public class AnnouncementRead {
    private Long id;

    /**
     * 公告id
     */
    private Integer announcementId;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建者部门
     */
    private Integer createDepartmentId;

}