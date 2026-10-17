package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @version 1.7.0
 * @description: 菜单权限
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Getter
@Setter
public class MenuPermission {
    private Integer id;

    /**
     * 菜单id
     */
    private Integer menuId;

    /**
     * 权限标识
     */
    private String identification;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 菜单排序
     */
    private Integer itemOrder;

    /**
     * 创建人
     */
    private Integer createUserId;

    /**
     * 创建人部门
     */
    private Integer createDepartmentId;

}