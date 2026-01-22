package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @version 1.7.0
 * @description: 课件部门
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Getter
@Setter
public class CourseWareDepartment {
    private Long id;

    /**
     * 课件id
     */
    private Integer courseWareId;

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
     * 创建人部门
     */
    private Integer createDepartmentId;

}