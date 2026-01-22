package com.mindskip.wdd.viewmodel.menu;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 菜单列表过滤
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class MenuListRequestVM {
    /**
     * 菜单层级
     */
    private Integer level;
}
