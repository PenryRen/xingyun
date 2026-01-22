package com.mindskip.wdd.viewmodel.role;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 角色分页返回
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class RolePageResponseVM {
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 菜单名称
     */
    private String menu;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 数据权限
     */
    private String dataFilterStr;
}
