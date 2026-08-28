package com.mindskip.wdd.viewmodel.menu;

import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 菜单树展示
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class MenuShowVM {
    private Integer id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单标题
     */
    private String metaTitle;

    /**
     * 子节点
     */
    private List<MenuShowVM> child;
}
