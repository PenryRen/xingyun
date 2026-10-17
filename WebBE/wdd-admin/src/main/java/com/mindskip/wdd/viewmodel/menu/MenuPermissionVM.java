package com.mindskip.wdd.viewmodel.menu;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @version 1.7.0
 * @description: 菜单权限
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class MenuPermissionVM {
    private Integer id;

    /**
     * 菜单id
     */
    private Integer menuId;

    /**
     * 权限标识
     */
    @NotNull
    private String identification;

    /**
     * 菜单名称
     */
    @NotNull
    private String name;
}
