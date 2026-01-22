package com.mindskip.wdd.viewmodel.menu;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 菜单分页返回
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class MenuPageResponseVM {
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
     * 菜单路径
     */
    private String path;

    /**
     * 名称
     */
    private String name;

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
    private String createTime;

    /**
     * 菜单层级
     */
    private Integer level;

    private String levelStr;

}
