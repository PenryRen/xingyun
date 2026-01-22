package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 部门
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 获取一级部门
     *
     * @return the root department
     */
    List<Department> getRootDepartment();

    /**
     * 根据父节点获取部门
     *
     * @param id the id
     * @return the department by parent id
     */
    List<Department> getDepartmentByParentId(Integer id);


    /**
     * 更新层级
     *
     * @param regexLevel  the regex level
     * @param targetLevel the target level
     * @return the int
     */
    int updateLevel(@Param("regexLevel") String regexLevel, @Param("targetLevel") String targetLevel);

    /**
     * 根据层级获取部门
     *
     * @param level the level
     * @return the by level
     */
    Department getByLevel(String level);

    /**
     * 删除部门，根据层级
     *
     * @param level the level
     * @return the int
     */
    int deleteByLevel(String level);

    /**
     * 根据id获取部门
     *
     * @param idList the id list
     * @return the department by id
     */
    List<Department> getDepartmentById(@Param("idList") List<Integer> idList);
}