package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Menu;
import com.mindskip.wdd.domain.Role;
import com.mindskip.wdd.domain.RoleMenuPermission;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.enums.MenuLevelEnum;
import com.mindskip.wdd.mapping.MenuMapping;
import com.mindskip.wdd.mapping.RoleMapping;
import com.mindskip.wdd.repository.MenuMapper;
import com.mindskip.wdd.repository.RoleMapper;
import com.mindskip.wdd.repository.RoleMenuPermissionMapper;
import com.mindskip.wdd.service.RoleService;
import com.mindskip.wdd.viewmodel.menu.RouterItemVM;
import com.mindskip.wdd.viewmodel.role.RoleEditRequestVM;
import com.mindskip.wdd.viewmodel.role.RoleMenuItemVM;
import com.mindskip.wdd.viewmodel.role.RolePageRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 角色
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@AllArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    private final RoleMapper roleMapper;
    private final RoleMapping roleMapping;
    private final MenuMapper menuMapper;
    private final RoleMenuPermissionMapper roleMenuPermissionMapper;
    private final MenuMapping menuMapping;

    @Override
    public List<RouterItemVM> getRouter(Integer roleId) {
        List<RoleMenuPermission> rolePermissionList = roleMenuPermissionMapper.getRoleMenuPermission(roleId);
        List<Menu> allMenu = menuMapper.listAll();
        List<RouterItemVM> rootVMList = new ArrayList<>();
        allMenu.stream()
                .filter(menu -> menu.getLevel().equals(MenuLevelEnum.ONE.getCode()))
                .sorted(Comparator.comparing(Menu::getItemOrder))
                .forEach(root -> {
                    if (rolePermissionList.stream().anyMatch(roleMenuPermission -> roleMenuPermission.getMenuParentId().equals(root.getId()))) {  //一级菜单包含
                        RouterItemVM rootRouter = menuMapping.toRouterItemVM(root);
                        List<RouterItemVM> childVMList = new ArrayList<>();
                        allMenu.stream()
                                .filter(d -> root.getId().equals(d.getParentId()))
                                .sorted(Comparator.comparing(Menu::getItemOrder))
                                .forEach(child -> {
                                    if (child.getHidden() || rolePermissionList.stream().anyMatch(roleMenuPermission -> roleMenuPermission.getMenuId().equals(child.getId()))) { //二级菜单包含
                                        RouterItemVM childRouter = menuMapping.toRouterItemVM(child);
                                        childVMList.add(childRouter);
                                    }
                                });
                        rootRouter.setChild(childVMList);
                        rootVMList.add(rootRouter);
                    }
                });
        return rootVMList;
    }

    @Override
    public List<RoleMenuPermission> getRoleMenuPermission(Integer roleId) {
        return roleMenuPermissionMapper.getRoleMenuPermission(roleId);
    }

    @Override
    public List<Role> list() {
        return roleMapper.list();
    }


    @Override
    public PageInfo<Role> page(RolePageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                roleMapper.page(requestVM)
        );
    }

    @Override
    @Transactional
    public void insertRole(RoleEditRequestVM roleEditRequestVM, User user) {
        Role newRole = roleMapping.toRole(roleEditRequestVM);
        newRole.setCreateUser(user.getId());
        newRole.setCreateDepartmentId(user.getDepartmentId());
        newRole.setCreateTime(new Date());
        newRole.setDeleted(false);
        roleMapper.insert(newRole);

        roleEditRequestVM.getRoleMenuVMList().stream().forEach(roleMenuVM -> {
            roleMenuVM.getRoleMenuItemVMList().stream()
                    .filter(roleMenuItemVM -> roleMenuItemVM.getRootSelect())
                    .forEach(roleMenuItemVM -> {
                        newRoleMenuPermissionInsert(roleMenuItemVM, newRole.getId(), user);
                    });
        });
    }

    @Override
    @Transactional
    public void updateRole(RoleEditRequestVM roleEditRequestVM, User user) {
        Role oldRole = roleMapper.selectById(roleEditRequestVM.getId());
        roleMapping.mapRole(roleEditRequestVM, oldRole);
        roleMapper.updateById(oldRole);

        roleMenuPermissionMapper.getRoleMenuPermission(oldRole.getId()).stream()
                .forEach(roleMenuPermission -> {
                    roleMenuPermission.setDeleted(true);
                    roleMenuPermissionMapper.updateById(roleMenuPermission);
                });

        roleEditRequestVM.getRoleMenuVMList().stream().forEach(roleMenuVM -> {
            roleMenuVM.getRoleMenuItemVMList().stream()
                    .filter(roleMenuItemVM -> roleMenuItemVM.getRootSelect())
                    .forEach(roleMenuItemVM -> {
                        newRoleMenuPermissionInsert(roleMenuItemVM, oldRole.getId(), user);
                    });
        });
    }

    @Override
    public List<RoleMenuPermission> getPermissionByRoleId(Integer roleId) {
        return roleMenuPermissionMapper.getRoleMenuPermission(roleId);
    }


    /**
     * 角色权限插入
     *
     * @param roleMenuItemVM
     * @param roleId
     * @param createUser
     */
    private void newRoleMenuPermissionInsert(RoleMenuItemVM roleMenuItemVM, Integer roleId, User createUser) {
        String permissions = roleMenuItemVM.getPermissionSelect().stream().collect(Collectors.joining(","));
        Menu menu = menuMapper.selectById(roleMenuItemVM.getId());
        RoleMenuPermission roleMenuPermission = new RoleMenuPermission();
        roleMenuPermission.setMenuId(menu.getId());
        roleMenuPermission.setMenuParentId(menu.getParentId());
        roleMenuPermission.setMenuMetaTitle(menu.getMetaTitle());
        roleMenuPermission.setDeleted(false);
        roleMenuPermission.setRoleId(roleId);
        roleMenuPermission.setPermissions(permissions);
        roleMenuPermission.setCreateUserId(createUser.getId());
        roleMenuPermission.setCreateDepartmentId(createUser.getDepartmentId());
        roleMenuPermissionMapper.insert(roleMenuPermission);
    }
}
