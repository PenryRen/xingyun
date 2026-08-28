package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Menu;
import com.mindskip.wdd.domain.MenuPermission;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.menu.MenuEditRequestVM;
import com.mindskip.wdd.viewmodel.menu.MenuListResponseVM;
import com.mindskip.wdd.viewmodel.menu.MenuPageResponseVM;
import com.mindskip.wdd.viewmodel.menu.MenuPermissionVM;
import com.mindskip.wdd.viewmodel.menu.MenuShowVM;
import com.mindskip.wdd.viewmodel.menu.RouterItemVM;
import com.mindskip.wdd.viewmodel.role.RoleMenuItemVM;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:40:32+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class MenuMappingImpl implements MenuMapping {

    @Override
    public MenuListResponseVM toMenuListResponseVM(Menu menu) {
        if ( menu == null ) {
            return null;
        }

        MenuListResponseVM menuListResponseVM = new MenuListResponseVM();

        menuListResponseVM.setId( menu.getId() );
        menuListResponseVM.setMenuName( menu.getMenuName() );
        menuListResponseVM.setMetaTitle( menu.getMetaTitle() );
        menuListResponseVM.setName( menu.getName() );
        menuListResponseVM.setPath( menu.getPath() );

        return menuListResponseVM;
    }

    @Override
    public MenuPageResponseVM toMenuPageResponseVM(Menu menu) {
        if ( menu == null ) {
            return null;
        }

        MenuPageResponseVM menuPageResponseVM = new MenuPageResponseVM();

        menuPageResponseVM.setComponent( menu.getComponent() );
        menuPageResponseVM.setCreateUser( menu.getCreateUser() );
        menuPageResponseVM.setDeleted( menu.getDeleted() );
        menuPageResponseVM.setHidden( menu.getHidden() );
        menuPageResponseVM.setId( menu.getId() );
        menuPageResponseVM.setLevel( menu.getLevel() );
        menuPageResponseVM.setMenuName( menu.getMenuName() );
        menuPageResponseVM.setMetaTitle( menu.getMetaTitle() );
        menuPageResponseVM.setName( menu.getName() );
        menuPageResponseVM.setParentId( menu.getParentId() );
        menuPageResponseVM.setPath( menu.getPath() );

        menuPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(menu.getCreateTime()) );

        return menuPageResponseVM;
    }

    @Override
    public MenuEditRequestVM toMenuEditRequestVM(Menu menu) {
        if ( menu == null ) {
            return null;
        }

        MenuEditRequestVM menuEditRequestVM = new MenuEditRequestVM();

        menuEditRequestVM.setComponent( menu.getComponent() );
        menuEditRequestVM.setHidden( menu.getHidden() );
        menuEditRequestVM.setId( menu.getId() );
        menuEditRequestVM.setItemOrder( menu.getItemOrder() );
        menuEditRequestVM.setLevel( menu.getLevel() );
        menuEditRequestVM.setMenuName( menu.getMenuName() );
        menuEditRequestVM.setMetaActiveMenu( menu.getMetaActiveMenu() );
        menuEditRequestVM.setMetaIcon( menu.getMetaIcon() );
        menuEditRequestVM.setMetaTitle( menu.getMetaTitle() );
        menuEditRequestVM.setName( menu.getName() );
        menuEditRequestVM.setParentId( menu.getParentId() );
        menuEditRequestVM.setPath( menu.getPath() );

        return menuEditRequestVM;
    }

    @Override
    public Menu toMenu(MenuEditRequestVM menuEditRequestVM) {
        if ( menuEditRequestVM == null ) {
            return null;
        }

        Menu menu = new Menu();

        menu.setComponent( menuEditRequestVM.getComponent() );
        menu.setHidden( menuEditRequestVM.getHidden() );
        menu.setId( menuEditRequestVM.getId() );
        menu.setItemOrder( menuEditRequestVM.getItemOrder() );
        menu.setLevel( menuEditRequestVM.getLevel() );
        menu.setMenuName( menuEditRequestVM.getMenuName() );
        menu.setMetaActiveMenu( menuEditRequestVM.getMetaActiveMenu() );
        menu.setMetaIcon( menuEditRequestVM.getMetaIcon() );
        menu.setMetaTitle( menuEditRequestVM.getMetaTitle() );
        menu.setName( menuEditRequestVM.getName() );
        menu.setParentId( menuEditRequestVM.getParentId() );
        menu.setPath( menuEditRequestVM.getPath() );

        return menu;
    }

    @Override
    public void mapMenu(MenuEditRequestVM menuEditRequestVM, Menu menu) {
        if ( menuEditRequestVM == null ) {
            return;
        }

        menu.setComponent( menuEditRequestVM.getComponent() );
        menu.setHidden( menuEditRequestVM.getHidden() );
        menu.setId( menuEditRequestVM.getId() );
        menu.setItemOrder( menuEditRequestVM.getItemOrder() );
        menu.setLevel( menuEditRequestVM.getLevel() );
        menu.setMenuName( menuEditRequestVM.getMenuName() );
        menu.setMetaActiveMenu( menuEditRequestVM.getMetaActiveMenu() );
        menu.setMetaIcon( menuEditRequestVM.getMetaIcon() );
        menu.setMetaTitle( menuEditRequestVM.getMetaTitle() );
        menu.setName( menuEditRequestVM.getName() );
        menu.setParentId( menuEditRequestVM.getParentId() );
        menu.setPath( menuEditRequestVM.getPath() );
    }

    @Override
    public MenuShowVM toMenuShowVM(Menu menu) {
        if ( menu == null ) {
            return null;
        }

        MenuShowVM menuShowVM = new MenuShowVM();

        menuShowVM.setId( menu.getId() );
        menuShowVM.setMenuName( menu.getMenuName() );
        menuShowVM.setMetaTitle( menu.getMetaTitle() );

        return menuShowVM;
    }

    @Override
    public List<MenuShowVM> toMenuShowVMList(List<Menu> menuList) {
        if ( menuList == null ) {
            return null;
        }

        List<MenuShowVM> list = new ArrayList<MenuShowVM>( menuList.size() );
        for ( Menu menu : menuList ) {
            list.add( toMenuShowVM( menu ) );
        }

        return list;
    }

    @Override
    public RoleMenuItemVM toRoleMenuItemVM(MenuShowVM menuShowVM) {
        if ( menuShowVM == null ) {
            return null;
        }

        RoleMenuItemVM roleMenuItemVM = new RoleMenuItemVM();

        roleMenuItemVM.setId( menuShowVM.getId() );
        roleMenuItemVM.setMenuName( menuShowVM.getMenuName() );

        return roleMenuItemVM;
    }

    @Override
    public RouterItemVM toRouterItemVM(Menu menu) {
        if ( menu == null ) {
            return null;
        }

        RouterItemVM routerItemVM = new RouterItemVM();

        routerItemVM.setComponent( menu.getComponent() );
        routerItemVM.setHidden( menu.getHidden() );
        routerItemVM.setId( menu.getId() );
        routerItemVM.setMetaActiveMenu( menu.getMetaActiveMenu() );
        routerItemVM.setMetaAffix( menu.getMetaAffix() );
        routerItemVM.setMetaIcon( menu.getMetaIcon() );
        routerItemVM.setMetaNoCache( menu.getMetaNoCache() );
        routerItemVM.setMetaTitle( menu.getMetaTitle() );
        routerItemVM.setName( menu.getName() );
        routerItemVM.setPath( menu.getPath() );

        return routerItemVM;
    }

    @Override
    public MenuPermissionVM toMenuPermissionVM(MenuPermission menuPermission) {
        if ( menuPermission == null ) {
            return null;
        }

        MenuPermissionVM menuPermissionVM = new MenuPermissionVM();

        menuPermissionVM.setId( menuPermission.getId() );
        menuPermissionVM.setIdentification( menuPermission.getIdentification() );
        menuPermissionVM.setMenuId( menuPermission.getMenuId() );
        menuPermissionVM.setName( menuPermission.getName() );

        return menuPermissionVM;
    }

    @Override
    public List<MenuPermissionVM> toMenuPermissionVMList(List<MenuPermission> menuPermissionList) {
        if ( menuPermissionList == null ) {
            return null;
        }

        List<MenuPermissionVM> list = new ArrayList<MenuPermissionVM>( menuPermissionList.size() );
        for ( MenuPermission menuPermission : menuPermissionList ) {
            list.add( toMenuPermissionVM( menuPermission ) );
        }

        return list;
    }

    @Override
    public MenuPermission toMenuPermission(MenuPermissionVM menuPermissionVM) {
        if ( menuPermissionVM == null ) {
            return null;
        }

        MenuPermission menuPermission = new MenuPermission();

        menuPermission.setId( menuPermissionVM.getId() );
        menuPermission.setIdentification( menuPermissionVM.getIdentification() );
        menuPermission.setMenuId( menuPermissionVM.getMenuId() );
        menuPermission.setName( menuPermissionVM.getName() );

        return menuPermission;
    }
}
