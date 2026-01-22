package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.MenuPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 菜单权限
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface MenuPermissionMapper extends BaseMapper<MenuPermission> {
    /**
     * 根据菜单id获取菜单权限
     *
     * @param menuId the menu id
     * @return the menu permission
     */
    List<MenuPermission> getMenuPermission(@Param("menuId") Integer menuId);
}