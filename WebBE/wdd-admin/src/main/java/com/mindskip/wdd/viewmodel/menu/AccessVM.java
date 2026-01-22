package com.mindskip.wdd.viewmodel.menu;

import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 访问权限
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class AccessVM {
    /**
     * 路由列表
     */
    private List<RouterItemVM> routerItemVMList;
    /**
     * 权限列表
     */
    private List<String> permissionList;
}

