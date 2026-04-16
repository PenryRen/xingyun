package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.RoleMenuPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 角色菜单权限
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper
public interface RoleMenuPermissionMapper extends BaseMapper<RoleMenuPermission> {

    /**
     * 根据角色id获取角色菜单权限
     *
     * @param roleId the role id
     * @return the role menu permission
     */
    List<RoleMenuPermission> getRoleMenuPermission(@Param("roleId") Integer roleId);

}