package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 10.0.0
 * @description: 课件观看详情
 * Copyright (C), 2025, 麟航团队
 * @date 2025/12/16 10:45
 */
@Getter
@Setter
public class CourseWareWatchDetail {
    private Long id;

    /**
     * 课件id
     */
    private Integer courseWareId;

    /**
     * 创建人
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
     * 观看时长(秒)
     */
    private Integer watchInterval;

}