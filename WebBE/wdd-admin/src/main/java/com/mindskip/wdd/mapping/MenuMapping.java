package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Menu;
import com.mindskip.wdd.domain.MenuPermission;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.menu.*;
import com.mindskip.wdd.viewmodel.role.RoleMenuItemVM;
import org.mapstruct.*;

import java.util.List;


/**
 * @version 1.7.0
 * @description: MenuMapping
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface MenuMapping {

    /**
     * To menu list response vm menu list response vm.
     *
     * @param menu the menu
     * @return the menu list response vm
     */
    MenuListResponseVM toMenuListResponseVM(Menu menu);

    /**
     * To menu page response vm menu page response vm.
     *
     * @param menu the menu
     * @return the menu page response vm
     */
    @Mappings({
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(menu.getCreateTime()))")
    })
    MenuPageResponseVM toMenuPageResponseVM(Menu menu);

    /**
     * To menu edit request vm menu edit request vm.
     *
     * @param menu the menu
     * @return the menu edit request vm
     */
    MenuEditRequestVM toMenuEditRequestVM(Menu menu);

    /**
     * To menu menu.
     *
     * @param menuEditRequestVM the menu edit request vm
     * @return the menu
     */
    Menu toMenu(MenuEditRequestVM menuEditRequestVM);

    /**
     * Map menu.
     *
     * @param menuEditRequestVM the menu edit request vm
     * @param menu              the menu
     */
    @InheritConfiguration
    void mapMenu(MenuEditRequestVM menuEditRequestVM, @MappingTarget Menu menu);


    /**
     * To menu show vm menu show vm.
     *
     * @param menu the menu
     * @return the menu show vm
     */
    MenuShowVM toMenuShowVM(Menu menu);

    /**
     * To menu show vm list list.
     *
     * @param menuList the menu list
     * @return the list
     */
    List<MenuShowVM> toMenuShowVMList(List<Menu> menuList);


    /**
     * To role menu item vm role menu item vm.
     *
     * @param menuShowVM the menu show vm
     * @return the role menu item vm
     */
    RoleMenuItemVM toRoleMenuItemVM(MenuShowVM menuShowVM);

    /**
     * To router item vm router item vm.
     *
     * @param menu the menu
     * @return the router item vm
     */
    RouterItemVM toRouterItemVM(Menu menu);

    /**
     * To menu permission vm menu permission vm.
     *
     * @param menuPermission the menu permission
     * @return the menu permission vm
     */
    MenuPermissionVM toMenuPermissionVM(MenuPermission menuPermission);

    /**
     * To menu permission vm list list.
     *
     * @param menuPermissionList the menu permission list
     * @return the list
     */
    List<MenuPermissionVM> toMenuPermissionVMList(List<MenuPermission> menuPermissionList);

    /**
     * To menu permission menu permission.
     *
     * @param menuPermissionVM the menu permission vm
     * @return the menu permission
     */
    MenuPermission toMenuPermission(MenuPermissionVM menuPermissionVM);
}
