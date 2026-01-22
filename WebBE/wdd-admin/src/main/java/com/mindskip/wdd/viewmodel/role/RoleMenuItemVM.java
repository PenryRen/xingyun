package com.mindskip.wdd.viewmodel.role;

import com.mindskip.wdd.domain.MenuPermission;
import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 角色菜单
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class RoleMenuItemVM {
    private Integer id;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 已选权限
     */
    private List<String> permissionSelect;
    /**
     * 所有权限
     */
    private List<String> permissionAllSelect;
    /**
     * 菜单权限
     */
    private List<MenuPermission> menuPermissions;
    /**
     * 是否全选
     */
    private Boolean rootSelect;
}
