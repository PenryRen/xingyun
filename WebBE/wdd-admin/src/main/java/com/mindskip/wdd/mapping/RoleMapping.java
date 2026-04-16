package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Role;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.role.RoleEditRequestVM;
import com.mindskip.wdd.viewmodel.role.RoleListResponseVM;
import com.mindskip.wdd.viewmodel.role.RolePageResponseVM;
import org.mapstruct.*;


/**
 * @version 1.7.0
 * @description: RoleMapping
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface RoleMapping {

    /**
     * To role list response vm role list response vm.
     *
     * @param role the role
     * @return the role list response vm
     */
    RoleListResponseVM toRoleListResponseVM(Role role);

    /**
     * To role page response vm role page response vm.
     *
     * @param role the role
     * @return the role page response vm
     */
    @Mappings({
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(role.getCreateTime()))")
    })
    RolePageResponseVM toRolePageResponseVM(Role role);

    /**
     * To role edit request vm role edit request vm.
     *
     * @param role the role
     * @return the role edit request vm
     */
    RoleEditRequestVM toRoleEditRequestVM(Role role);

    /**
     * To role role.
     *
     * @param permissionEditRequestVM the permission edit request vm
     * @return the role
     */
    Role toRole(RoleEditRequestVM permissionEditRequestVM);

    /**
     * Map role.
     *
     * @param permissionEditRequestVM the permission edit request vm
     * @param role                    the role
     */
    @InheritConfiguration
    void mapRole(RoleEditRequestVM permissionEditRequestVM, @MappingTarget Role role);



}
