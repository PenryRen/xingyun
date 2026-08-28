package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Role;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.role.RoleEditRequestVM;
import com.mindskip.wdd.viewmodel.role.RoleListResponseVM;
import com.mindskip.wdd.viewmodel.role.RolePageResponseVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-28T15:49:38+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class RoleMappingImpl implements RoleMapping {

    @Override
    public RoleListResponseVM toRoleListResponseVM(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleListResponseVM roleListResponseVM = new RoleListResponseVM();

        roleListResponseVM.setId( role.getId() );
        roleListResponseVM.setName( role.getName() );

        return roleListResponseVM;
    }

    @Override
    public RolePageResponseVM toRolePageResponseVM(Role role) {
        if ( role == null ) {
            return null;
        }

        RolePageResponseVM rolePageResponseVM = new RolePageResponseVM();

        rolePageResponseVM.setId( role.getId() );
        rolePageResponseVM.setName( role.getName() );

        rolePageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(role.getCreateTime()) );

        return rolePageResponseVM;
    }

    @Override
    public RoleEditRequestVM toRoleEditRequestVM(Role role) {
        if ( role == null ) {
            return null;
        }

        RoleEditRequestVM roleEditRequestVM = new RoleEditRequestVM();

        roleEditRequestVM.setDataFilter( role.getDataFilter() );
        roleEditRequestVM.setId( role.getId() );
        roleEditRequestVM.setName( role.getName() );

        return roleEditRequestVM;
    }

    @Override
    public Role toRole(RoleEditRequestVM permissionEditRequestVM) {
        if ( permissionEditRequestVM == null ) {
            return null;
        }

        Role role = new Role();

        role.setDataFilter( permissionEditRequestVM.getDataFilter() );
        role.setId( permissionEditRequestVM.getId() );
        role.setName( permissionEditRequestVM.getName() );

        return role;
    }

    @Override
    public void mapRole(RoleEditRequestVM permissionEditRequestVM, Role role) {
        if ( permissionEditRequestVM == null ) {
            return;
        }

        role.setDataFilter( permissionEditRequestVM.getDataFilter() );
        role.setId( permissionEditRequestVM.getId() );
        role.setName( permissionEditRequestVM.getName() );
    }
}
