package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.Menu;
import com.mindskip.wdd.viewmodel.menu.MenuListRequestVM;
import com.mindskip.wdd.viewmodel.menu.MenuPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 菜单
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 获取所有菜单
     *
     * @return the list
     */
    List<Menu> listAll();

    /**
     * 获取菜单
     *
     * @param menuListRequestVM the menu list request vm
     * @return the list
     */
    List<Menu> list(MenuListRequestVM menuListRequestVM);

    /**
     * 菜单分页
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<Menu> page(MenuPageRequestVM requestVM);
}