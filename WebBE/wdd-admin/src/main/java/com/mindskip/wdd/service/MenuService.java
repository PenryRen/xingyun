package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Menu;
import com.mindskip.wdd.domain.MenuPermission;
import com.mindskip.wdd.viewmodel.menu.MenuListRequestVM;
import com.mindskip.wdd.viewmodel.menu.MenuPageRequestVM;
import com.mindskip.wdd.viewmodel.menu.MenuShowVM;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 菜单  Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface MenuService extends IService<Menu> {

    /**
     * 获取所有菜单
     *
     * @param menuListRequestVM the menu list request vm
     * @return the list
     */
    List<Menu> list(MenuListRequestVM menuListRequestVM);

    /**
     * 菜单分页
     *
     * @param requestVM the request vm
     * @return the page info
     */
    PageInfo<Menu> page(MenuPageRequestVM requestVM);

    /**
     * 获取树形菜单
     *
     * @return the tree menu
     */
    List<MenuShowVM> getTreeMenu();

    /**
     * 获取菜单权限
     *
     * @param menuId the menu id
     * @return the menu permission
     */
    List<MenuPermission> getMenuPermission(Integer menuId);

    /**
     * 插入菜单权限
     *
     * @param menuPermission the menu permission
     * @return the int
     */
    int insertMenuPermission(MenuPermission menuPermission);

    /**
     * 更新菜单权限
     *
     * @param menuPermission the menu permission
     * @return the int
     */
    int updateMenuPermission(MenuPermission menuPermission);
}
