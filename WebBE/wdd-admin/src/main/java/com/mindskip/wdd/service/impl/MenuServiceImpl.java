package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Menu;
import com.mindskip.wdd.domain.MenuPermission;
import com.mindskip.wdd.mapping.MenuMapping;
import com.mindskip.wdd.repository.MenuMapper;
import com.mindskip.wdd.repository.MenuPermissionMapper;
import com.mindskip.wdd.service.MenuService;
import com.mindskip.wdd.viewmodel.menu.MenuListRequestVM;
import com.mindskip.wdd.viewmodel.menu.MenuPageRequestVM;
import com.mindskip.wdd.viewmodel.menu.MenuShowVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 菜单
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@AllArgsConstructor
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {


    private final MenuPermissionMapper menuPermissionMapper;
    private final MenuMapper permissionMapper;
    private final MenuMapping menuMapping;


    @Override
    public List<Menu> list(MenuListRequestVM menuListRequestVM) {
        return permissionMapper.list(menuListRequestVM);
    }

    @Override
    public PageInfo<Menu> page(MenuPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                permissionMapper.page(requestVM)
        );
    }

    @Override
    public List<MenuShowVM> getTreeMenu() {
        MenuListRequestVM model = new MenuListRequestVM();
        model.setLevel(1);
        List<Menu> rootMenuList = list(model);
        model.setLevel(2);
        List<Menu> childMenuList = list(model);
        List<MenuShowVM> menuShowVMS = menuMapping.toMenuShowVMList(rootMenuList);
        menuShowVMS.forEach(menuShowVM -> {
            List<Menu> childItem = childMenuList.stream().filter(d -> d.getParentId().equals(menuShowVM.getId())).sorted(Comparator.comparing(Menu::getItemOrder)).collect(Collectors.toList());
            List<MenuShowVM> menuShowVMList = menuMapping.toMenuShowVMList(childItem);
            menuShowVM.setChild(menuShowVMList);
        });
        return menuShowVMS;
    }

    @Override
    public List<MenuPermission> getMenuPermission(Integer menuId) {
        return menuPermissionMapper.getMenuPermission(menuId);
    }

    @Override
    public int insertMenuPermission(MenuPermission menuPermission) {
        return menuPermissionMapper.insert(menuPermission);
    }

    @Override
    public int updateMenuPermission(MenuPermission menuPermission) {
        return menuPermissionMapper.updateById(menuPermission);
    }
}
