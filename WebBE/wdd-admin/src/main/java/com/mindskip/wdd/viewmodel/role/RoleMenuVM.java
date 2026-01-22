package com.mindskip.wdd.viewmodel.role;

import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 角色菜单
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class RoleMenuVM {
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 角色菜单列表
     */
    private List<RoleMenuItemVM> roleMenuItemVMList;
}
