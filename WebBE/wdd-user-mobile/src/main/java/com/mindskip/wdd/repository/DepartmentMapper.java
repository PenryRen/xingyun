package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.Department;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 部门
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {
    /**
     * 获取根节点部门
     *
     * @return the root department
     */
    List<Department> getRootDepartment();

    /**
     * 获取部门根据父节点
     *
     * @param id the id
     * @return the department by parent id
     */
    List<Department> getDepartmentByParentId(Integer id);
}