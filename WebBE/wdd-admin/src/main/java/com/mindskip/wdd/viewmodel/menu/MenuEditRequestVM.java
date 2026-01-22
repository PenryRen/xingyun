package com.mindskip.wdd.viewmodel.menu;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 菜单编辑
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class MenuEditRequestVM {

    private Integer id;

    /**
     * 菜单名称
     */
    @NotNull
    private String menuName;

    /**
     * 菜单路径
     */
    @NotNull
    private String path;

    /**
     * 视图名称
     */
    private String name;

    /**
     * 菜单标题
     */
    @NotNull
    private String metaTitle;

    /**
     * 菜单图标
     */
    private String metaIcon;

    /**
     * 视图地址
     */
    private String component;

    /**
     * 上级节点id
     */
    private Integer parentId;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 高亮菜单
     */
    private String metaActiveMenu;

    /**
     * 是否隐藏
     */
    private Boolean hidden;

    /**
     * 菜单排序
     */
    private Integer itemOrder;

    /**
     * 菜单权限
     */
    @Valid
    private List<MenuPermissionVM> permissionList;

}
