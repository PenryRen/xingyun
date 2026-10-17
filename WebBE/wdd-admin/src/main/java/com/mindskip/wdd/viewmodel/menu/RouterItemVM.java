package com.mindskip.wdd.viewmodel.menu;


import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 路由展示
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class RouterItemVM {

    private Integer id;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 名称
     */
    private String name;

    /**
     * 菜单标题
     */
    private String metaTitle;

    /**
     * 菜单图标
     */
    private String metaIcon;

    /**
     * 视图缓存
     */
    private Boolean metaNoCache;

    private Boolean metaAffix;

    /**
     * 高亮菜单
     */
    private String metaActiveMenu;

    /**
     * 是否隐藏
     */
    private Boolean hidden;

    /**
     * 视图地址
     */
    private String component;

    /**
     * 子菜单
     */
    private List<RouterItemVM> child;
}
