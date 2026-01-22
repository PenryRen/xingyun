package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 课件观看记录
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Getter
@Setter
public class CourseWareWatch {
    private Long id;

    /**
     * 课件id
     */
    private Integer courseWareId;

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
     * 观看总时长
     */
    private Long watchTotalLength;

    /**
     * 观看当前时长
     */
    private Integer watchCurrentTime;
}