package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @version 1.7.0
 * @description: 角色菜单权限
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Getter
@Setter
public class RoleMenuPermission {
    private Integer id;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 菜单id
     */
    private Integer menuId;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 上级菜单id
     */
    private Integer menuParentId;

    /**
     * 菜单标题
     */
    private String menuMetaTitle;

    /**
     * 菜单权限标识列表
     */
    private String permissions;

    /**
     * 创建者
     */
    private Integer createUserId;

    /**
     * 创建人部门
     */
    private Integer createDepartmentId;

}