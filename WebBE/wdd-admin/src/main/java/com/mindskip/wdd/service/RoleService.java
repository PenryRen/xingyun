package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Role;
import com.mindskip.wdd.domain.RoleMenuPermission;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.viewmodel.menu.RouterItemVM;
import com.mindskip.wdd.viewmodel.role.RoleEditRequestVM;
import com.mindskip.wdd.viewmodel.role.RolePageRequestVM;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 角色
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public interface RoleService extends IService<Role> {

    /**
     * 根据角色id,获取路由
     *
     * @param roleId the role id
     * @return the router
     */
    List<RouterItemVM> getRouter(Integer roleId);

    /**
     * 获取角色菜单权限
     *
     * @param roleId the role id
     * @return the role menu permission
     */
    List<RoleMenuPermission> getRoleMenuPermission(Integer roleId);

    /**
     * 查询所有角色
     *
     * @return the list
     */
    List<Role> list();

    /**
     * 角色分页
     *
     * @param requestVM the request vm
     * @return the page info
     */
    PageInfo<Role> page(RolePageRequestVM requestVM);

    /**
     * 插入所有角色
     *
     * @param roleEditRequestVM the role edit request vm
     * @param user              the user
     */
    void insertRole(RoleEditRequestVM roleEditRequestVM, User user);

    /**
     * 更细所有角色
     *
     * @param roleEditRequestVM the role edit request vm
     * @param user              the user
     */
    void updateRole(RoleEditRequestVM roleEditRequestVM, User user);

    /**
     * 获取角色权限
     *
     * @param roleId the role id
     * @return the permission by role id
     */
    List<RoleMenuPermission> getPermissionByRoleId(Integer roleId);
}
