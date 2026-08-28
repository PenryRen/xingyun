package com.mindskip.wdd.base;


import com.mindskip.wdd.context.WebContext;
import com.mindskip.wdd.domain.Role;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @version 1.7.0
 * @description: 控制器基础类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public class BaseApiController {
    /**
     * 默认分页数量
     */
    protected final static String DEFAULT_PAGE_SIZE = "10";

    /**
     * The Web context.
     */
    @Autowired
    protected WebContext webContext;

    @Autowired
    private RoleService roleService;


    /**
     * 获取当前用户
     *
     * @return the current user
     */
    protected User getCurrentUser() {
        return webContext.getCurrentUser();
    }

    /**
     * 获取当前用户角色
     *
     * @return the role
     */
    protected Role getRole() {
        User user = getCurrentUser();
        Role role = roleService.getById(user.getRoleId());
        return role;
    }

    /**
     * 初始化查询数据权限
     *
     * @param baseFilter the base filter
     */
    protected void initPermission(BaseFilter baseFilter) {
        Role role = getRole();
        baseFilter.setDataFilterDepartmentList(role.getDataFilter().getDepartmentIdList());
    }

    protected List<Integer> selectDepartmentFilter(List<Integer> departmentIdList) {
        if (departmentIdList.size() > 0) {
            Role role = getRole();
            List<Integer> filterDepartmentList = role.getDataFilter().getDepartmentIdList();
            if (filterDepartmentList.size() > 0) {
                return departmentIdList.stream()
                        .filter(departId -> filterDepartmentList.stream().anyMatch(filterId -> departId.equals(filterId)))
                        .collect(Collectors.toList());
            }
        }
        return departmentIdList;
    }

}
