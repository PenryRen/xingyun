package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.Role;
import com.mindskip.wdd.viewmodel.role.RolePageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 角色
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 获取所有角色
     *
     * @return the list
     */
    List<Role> list();

    /**
     * 角色分页
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<Role> page(RolePageRequestVM requestVM);
}