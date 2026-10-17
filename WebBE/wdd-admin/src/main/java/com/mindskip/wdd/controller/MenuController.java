package com.mindskip.wdd.controller;


import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.Menu;
import com.mindskip.wdd.domain.MenuPermission;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.enums.MenuLevelEnum;
import com.mindskip.wdd.mapping.MenuMapping;
import com.mindskip.wdd.service.MenuService;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.menu.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 菜单接口
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/menu")
public class MenuController extends BaseApiController {

    private final MenuService menuService;
    private final MenuMapping menuMapping;

    /**
     * 获取所有菜单
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/list")
    public RestResponse<List<MenuListResponseVM>> list(@RequestBody MenuListRequestVM model) {
        List<Menu> menuList = menuService.list(model);
        List<MenuListResponseVM> vm = menuList.stream()
                .map(d -> menuMapping.toMenuListResponseVM(d))
                .collect(Collectors.toList());
        return RestResponse.ok(vm);
    }


    /**
     * 获取树形菜单
     *
     * @return the rest response
     */
    @PostMapping("/level")
    public RestResponse<List<MenuShowVM>> listLevel() {
        List<MenuShowVM> menuShowVMS = menuService.getTreeMenu();
        return RestResponse.ok(menuShowVMS);
    }

    /**
     * 菜单分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/page")
    @PreAuthorize("menu:page")
    public RestResponse<PageInfo<MenuPageResponseVM>> pageList(@RequestBody MenuPageRequestVM model) {
        PageInfo<Menu> pageInfo = menuService.page(model);
        PageInfo<MenuPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, item -> {
            MenuPageResponseVM menuPageResponseVM = menuMapping.toMenuPageResponseVM(item);
            menuPageResponseVM.setLevelStr(MenuLevelEnum.fromCode(item.getLevel()).getName());
            return menuPageResponseVM;
        });
        return RestResponse.ok(page);
    }

    /**
     * 菜单查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/{id}")
    @PreAuthorize("menu:update")
    public RestResponse<MenuEditRequestVM> select(@PathVariable Integer id) {
        Menu menu = menuService.getById(id);
        MenuEditRequestVM menuEditRequestVM = menuMapping.toMenuEditRequestVM(menu);
        List<MenuPermission> menuPermissionList = menuService.getMenuPermission(menu.getId());
        List<MenuPermissionVM> menuPermissionVMList = menuMapping.toMenuPermissionVMList(menuPermissionList);
        menuEditRequestVM.setPermissionList(menuPermissionVMList);
        return RestResponse.ok(menuEditRequestVM);
    }


    /**
     * 菜单创建、更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/edit")
    @PreAuthorize({"menu:create", "menu:update"})
    public RestResponse edit(@RequestBody @Valid MenuEditRequestVM model) {
        User user = getCurrentUser();
        if (model.getId() == null) {
            Menu newMenu = menuMapping.toMenu(model);
            newMenu.setCreateUser(user.getId());
            newMenu.setCreateDepartmentId(user.getDepartmentId());
            newMenu.setCreateTime(new Date());
            newMenu.setDeleted(false);
            MenuLevelEnum menuLevelEnum = MenuLevelEnum.fromCode(model.getLevel());
            if (menuLevelEnum == MenuLevelEnum.ONE) {
                newMenu.setAlwaysShow(true);
            } else if (menuLevelEnum == MenuLevelEnum.TWO) {
                newMenu.setMetaNoCache(true);
            }
            menuService.save(newMenu);
            for (int i = 0; i < model.getPermissionList().size(); i++) {
                MenuPermission menuPermission = menuMapping.toMenuPermission(model.getPermissionList().get(i));
                menuPermission.setMenuId(newMenu.getId());
                menuPermission.setItemOrder(i + 1);
                menuPermission.setDeleted(false);
                menuPermission.setCreateUserId(user.getId());
                menuPermission.setCreateDepartmentId(user.getDepartmentId());
                menuService.insertMenuPermission(menuPermission);
            }
        } else {
            Menu oldMenu = menuService.getById(model.getId());
            menuMapping.mapMenu(model, oldMenu);
            menuService.updateById(oldMenu);
            List<MenuPermission> menuPermissionList = menuService.getMenuPermission(oldMenu.getId());
            menuPermissionList.forEach(item -> {
                item.setDeleted(true);
                menuService.updateMenuPermission(item);
            });
            for (int i = 0; i < model.getPermissionList().size(); i++) {
                MenuPermission menuPermission = menuMapping.toMenuPermission(model.getPermissionList().get(i));
                menuPermission.setId(null);
                menuPermission.setMenuId(oldMenu.getId());
                menuPermission.setItemOrder(i + 1);
                menuPermission.setDeleted(false);
                menuPermission.setCreateUserId(user.getId());
                menuPermission.setCreateDepartmentId(user.getDepartmentId());
                menuService.insertMenuPermission(menuPermission);
            }
        }
        return RestResponse.ok();
    }


    /**
     * 菜单删除
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("menu:delete")
    public RestResponse delete(@PathVariable Integer id) {
        Menu menu = menuService.getById(id);
        menu.setDeleted(true);
        menuService.updateById(menu);
        return RestResponse.ok();
    }
}
