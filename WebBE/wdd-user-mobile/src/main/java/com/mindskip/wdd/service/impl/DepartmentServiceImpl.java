package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.domain.Department;
import com.mindskip.wdd.mapping.DepartmentMapping;
import com.mindskip.wdd.repository.DepartmentMapper;
import com.mindskip.wdd.service.DepartmentService;
import com.mindskip.wdd.viewmodel.common.DepartmentVM;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 部门
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Service
@AllArgsConstructor
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    private final DepartmentMapper departmentMapper;
    private final DepartmentMapping departmentMapping;
    private final static String CACHE_NAME = "ueit:department";


    @Override
    @Cacheable(value = CACHE_NAME, key = "'unique'", unless = "#result.size() == 0")
    public List<DepartmentVM> getDepartmentByCache() {
        List<Department> departmentRoot = getRootDepartment();
        List<DepartmentVM> departmentVMRoot = departmentMapping.toDepartmentVM(departmentRoot);
        departmentRecursion(departmentVMRoot);
        return departmentVMRoot;
    }

    @Override
    public List<Department> getRootDepartment() {
        return departmentMapper.getRootDepartment();
    }

    @Override
    public List<Department> getDepartmentByParentId(Integer id) {
        return departmentMapper.getDepartmentByParentId(id);
    }


    /**
     * 部门模型转化
     *
     * @param departmentPointList
     */
    private void departmentRecursion(List<DepartmentVM> departmentPointList) {
        departmentPointList.forEach(item -> {
            List<Department> departmentChild = getDepartmentByParentId(item.getId());
            List<DepartmentVM> departmentVM = departmentMapping.toDepartmentVM(departmentChild);
            if (0 != departmentVM.size()) {
                item.setChild(departmentVM);
                departmentRecursion(departmentVM);
            }
        });
    }
}
