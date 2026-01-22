package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mindskip.wdd.domain.Department;
import com.mindskip.wdd.viewmodel.common.DepartmentVM;

import java.util.List;


/**
 * @version 1.7.0
 * @description: 部门
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface DepartmentService extends IService<Department> {


    /**
     * 部门缓存
     *
     * @return {@link List}<{@link DepartmentVM}>
     */
    List<DepartmentVM> getDepartmentByCache();

    /**
     * 获取根节点部门
     *
     * @return the root department
     */
    List<Department> getRootDepartment();

    /**
     * 获取部门根据父节点id
     *
     * @param id the id
     * @return the department by parent id
     */
    List<Department> getDepartmentByParentId(Integer id);

    /**
     * 根据层级获取部门
     *
     * @param level the level
     * @return the by level
     */
    Department getByLevel(String level);

}
