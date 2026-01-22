package com.mindskip.wdd.controller;


import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.MenuPermission;
import com.mindskip.wdd.domain.Role;
import com.mindskip.wdd.domain.RoleMenuPermission;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.frame.RoleDataFilterFrame;
import com.mindskip.wdd.mapping.MenuMapping;
import com.mindskip.wdd.mapping.RoleMapping;
import com.mindskip.wdd.service.DepartmentService;
import com.mindskip.wdd.service.MenuService;
import com.mindskip.wdd.service.RoleService;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.menu.AccessVM;
import com.mindskip.wdd.viewmodel.menu.RouterItemVM;
import com.mindskip.wdd.viewmodel.role.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 角色接口
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/role")
public class RoleController extends BaseApiController {

    private final MenuService menuService;
    private final RoleService roleService;
    private final RoleMapping roleMapping;
    private final MenuMapping menuMapping;
    private final DepartmentService departmentService;


    /**
     * 获取路由、权限列表
     *
     * @return the rest response
     */
    @PostMapping("/menu")
    public RestResponse<AccessVM> menu() {
        User user = getCurrentUser();
        List<RouterItemVM> routerItemVMList = roleService.getRouter(user.getRoleId());
        List<String> permission = roleService.getRoleMenuPermission(user.getRoleId()).stream()
                .flatMap(or -> Arrays.stream(or.getPermissions().split(","))).collect(Collectors.toList());
        AccessVM accessVM = new AccessVM();
        accessVM.setRouterItemVMList(routerItemVMList);
        accessVM.setPermissionList(permission);
        return RestResponse.ok(accessVM);
    }


    /**
     * 获取角色列表
     *
     * @return the rest response
     */
    @PostMapping("/list")
    public RestResponse<List<RoleListResponseVM>> list() {
        List<Role> roleList = roleService.list();
        List<RoleListResponseVM> vm = roleList.stream()
                .map(d -> roleMapping.toRoleListResponseVM(d))
                .collect(Collectors.toList());
        return RestResponse.ok(vm);
    }


    /**
     * 角色分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/page")
    @PreAuthorize("role:page")
    public RestResponse<PageInfo<RolePageResponseVM>> pageList(@RequestBody RolePageRequestVM model) {
        PageInfo<Role> pageInfo = roleService.page(model);
        PageInfo<RolePageResponseVM> page = PageInfoHelper.copyMap(pageInfo, item -> {
            Role role = roleService.getById(item.getId());
            RolePageResponseVM rolePageResponseVM = roleMapping.toRolePageResponseVM(role);
            String permissionStr = roleService.getPermissionByRoleId(role.getId()).stream()
                    .map(orp -> orp.getMenuMetaTitle())
                    .collect(Collectors.joining(" "));
            rolePageResponseVM.setMenu(permissionStr);
            String dataFilter = role.getDataFilter().getDepartmentIdList().stream()
                    .map(de -> departmentService.getById(de).getName())
                    .collect(Collectors.joining(" "));
            rolePageResponseVM.setDataFilterStr(dataFilter);
            return rolePageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 创建角色
     *
     * @return the rest response
     */
    @PostMapping("/create")
    @PreAuthorize("role:create")
    public RestResponse<RoleEditRequestVM> create() {
        RoleEditRequestVM roleEditRequestVM = new RoleEditRequestVM();
        List<RoleMenuVM> roleMenuVMList = menuService.getTreeMenu().stream().map(top -> {
            RoleMenuVM roleMenuVM = new RoleMenuVM();
            roleMenuVM.setMenuName(top.getMenuName());
            List<RoleMenuItemVM> roleMenuItemVMList = top.getChild().stream().map(second -> {
                RoleMenuItemVM roleMenuItemVM = menuMapping.toRoleMenuItemVM(second);
                List<MenuPermission> menuPermission = menuService.getMenuPermission(second.getId());
                roleMenuItemVM.setMenuPermissions(menuPermission);
                roleMenuItemVM.setPermissionSelect(new ArrayList<>());
                List<String> permissionAllSelect = roleMenuItemVM.getMenuPermissions().stream().map(d -> d.getIdentification()).collect(Collectors.toList());
                roleMenuItemVM.setPermissionAllSelect(permissionAllSelect);
                roleMenuItemVM.setRootSelect(false);
                return roleMenuItemVM;
            }).collect(Collectors.toList());
            roleMenuVM.setRoleMenuItemVMList(roleMenuItemVMList);
            return roleMenuVM;
        }).collect(Collectors.toList());
        roleEditRequestVM.setRoleMenuVMList(roleMenuVMList);
        RoleDataFilterFrame roleDataFilterFrame = new RoleDataFilterFrame();
        roleDataFilterFrame.setDepartmentIdList(new ArrayList<>());
        roleEditRequestVM.setDataFilter(roleDataFilterFrame);
        return RestResponse.ok(roleEditRequestVM);
    }


    /**
     * 查询角色
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/{id}")
    @PreAuthorize("role:update")
    public RestResponse<RoleEditRequestVM> select(@PathVariable Integer id) {
        Role role = roleService.getById(id);
        RoleEditRequestVM roleEditRequestVM = roleMapping.toRoleEditRequestVM(role);
        List<RoleMenuPermission> roleMenuPermissionList = roleService.getPermissionByRoleId(role.getId());
        List<RoleMenuVM> roleMenuVMList = menuService.getTreeMenu().stream().map(top -> {
            RoleMenuVM roleMenuVM = new RoleMenuVM();
            roleMenuVM.setMenuName(top.getMenuName());
            List<RoleMenuItemVM> roleMenuItemVMList = top.getChild().stream().map(second -> {
                RoleMenuItemVM roleMenuItemVM = menuMapping.toRoleMenuItemVM(second);
                RoleMenuPermission roleMenuPermission = roleMenuPermissionList.stream()
                        .filter(orp -> orp.getMenuId().equals(second.getId()))
                        .findFirst().orElse(null);
                List<MenuPermission> menuPermissions = menuService.getMenuPermission(second.getId());
                roleMenuItemVM.setMenuPermissions(menuPermissions);
                if (null == roleMenuPermission) {
                    roleMenuItemVM.setPermissionSelect(new ArrayList<>());
                    roleMenuItemVM.setRootSelect(false);
                } else {
                    List<String> permissionSelect = Arrays.stream(roleMenuPermission.getPermissions().split(",")).collect(Collectors.toList());
                    ;
                    roleMenuItemVM.setPermissionSelect(permissionSelect);
                    roleMenuItemVM.setRootSelect(true);
                }
                List<String> permissionAllSelect = roleMenuItemVM.getMenuPermissions().stream().map(d -> d.getIdentification()).collect(Collectors.toList());
                roleMenuItemVM.setPermissionAllSelect(permissionAllSelect);
                return roleMenuItemVM;
            }).collect(Collectors.toList());
            roleMenuVM.setRoleMenuItemVMList(roleMenuItemVMList);
            return roleMenuVM;
        }).collect(Collectors.toList());
        roleEditRequestVM.setRoleMenuVMList(roleMenuVMList);
        return RestResponse.ok(roleEditRequestVM);
    }


    /**
     * 更新角色
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/edit")
    @PreAuthorize({"role:create", "role:update"})
    public RestResponse edit(@RequestBody @Valid RoleEditRequestVM model) {
        Boolean empty = model.getRoleMenuVMList().stream().allMatch(item -> item.getRoleMenuItemVMList().stream().allMatch(cItem -> cItem.getPermissionSelect().size() == 0));
        if (empty) {
            return RestResponse.fail(2, "请选择权限");
        }
        model.getDataFilter().setDepartmentIdList(selectDepartmentFilter(model.getDataFilter().getDepartmentIdList()));
        if (model.getId() == null) {
            roleService.insertRole(model, getCurrentUser());
        } else {
            roleService.updateRole(model, getCurrentUser());
        }
        return RestResponse.ok();
    }


    /**
     * 删除角色
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("role:delete")
    public RestResponse delete(@PathVariable Integer id) {
        Role role = roleService.getById(id);
        role.setDeleted(true);
        roleService.updateById(role);
        return RestResponse.ok();
    }
}
