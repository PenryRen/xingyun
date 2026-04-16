package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @version 1.7.0
 * @description: 报名发布部门
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Getter
@Setter
public class ApplyDepartment {
    private Integer id;

    /**
     * 报名id
     */
    private Integer applyId;

    /**
     * 部门id
     */
    private Integer departmentId;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 创建人用户id
     */
    private Integer createUserId;

    /**
     * 创建人部门
     */
    private Integer createDepartmentId;

}