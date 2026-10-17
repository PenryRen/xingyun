package com.mindskip.wdd.viewmodel.role;

import com.mindskip.wdd.domain.frame.RoleDataFilterFrame;
import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 角色编辑
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class RoleEditRequestVM {

    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色菜单
     */
    private List<RoleMenuVM> roleMenuVMList;

    /**
     * 角色数据权限
     */
    private RoleDataFilterFrame dataFilter;
}
