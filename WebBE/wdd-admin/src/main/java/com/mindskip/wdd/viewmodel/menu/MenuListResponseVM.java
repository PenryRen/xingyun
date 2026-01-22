package com.mindskip.wdd.viewmodel.menu;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 菜单列表返回
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class MenuListResponseVM {
    private Integer id;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单路径
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

}
