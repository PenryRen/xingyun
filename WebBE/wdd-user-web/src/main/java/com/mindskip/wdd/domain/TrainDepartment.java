package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @version 9.0.0
 * @description: 培训部门
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/16 10:45
 */
@Getter
@Setter
public class TrainDepartment {
    private Long id;

    /**
     * 培训Id
     */
    private Integer trainId;

    /**
     * 部门
     */
    private Integer departmentId;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 创建人
     */
    private Integer createUserId;

    /**
     * 创建人部门
     */
    private Integer createDepartmentId;

}