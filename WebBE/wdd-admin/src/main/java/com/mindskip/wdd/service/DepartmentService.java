package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.Department;
import com.mindskip.wdd.viewmodel.department.DepartmentMoveRequestVM;

import java.util.List;


/**
 * @version 1.7.0
 * @description: 部门
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface DepartmentService extends IService<Department> {

    /**
     * 保存部门
     *
     * @param department
     * @return boolean
     */
    boolean save(Department department);

    /**
     * 更新部门
     *
     * @param department
     * @return boolean
     */
    boolean updateById(Department department);

    /**
     * 获取一级节点部门
     *
     * @return
     */
    List<Department> getRootDepartment();

    /**
     * 根据父节点id获取部门
     *
     * @param id
     * @return
     */
    List<Department> getDepartmentByParentId(Integer id);

    /**
     * 更新层级
     *
     * @param originalLevel
     * @param targetLevel
     * @return
     */
    int updateLevel(String originalLevel, String targetLevel);

    /**
     * 根据层级获取部门
     *
     * @param level
     * @return
     */
    Department getByLevel(String level);

    /**
     * 根据层级删除部门
     *
     * @param level
     * @return
     */
    int deleteByLevel(String level);

    /**
     * 根据id获取部门
     *
     * @param idList
     * @return
     */
    List<Department> getDepartmentById(List<Integer> idList);

    /**
     * 部门位置移动
     *
     * @param departmentMoveRequestVM
     * @return {@link RestResponse}
     */
    RestResponse move(DepartmentMoveRequestVM departmentMoveRequestVM);
}
