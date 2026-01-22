package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 菜单
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Getter
@Setter
public class Menu {
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
     * 菜单标题
     */
    private String metaTitle;

    /**
     * 菜单图标
     */
    private String metaIcon;

    /**
     * 菜单缓存
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
     * 视图路径
     */
    private String component;

    /**
     * 上级节点id
     */
    private Integer parentId;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 菜单级别
     */
    private Integer level;

    /**
     * 是否常驻显示
     */
    private Boolean alwaysShow;

    /**
     * 排序
     */
    private Integer itemOrder;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 创建者部门
     */
    private Integer createDepartmentId;

}