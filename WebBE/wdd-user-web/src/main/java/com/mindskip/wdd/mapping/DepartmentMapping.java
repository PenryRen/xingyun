package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Department;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.common.DepartmentVM;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @version 5.9.0
 * @description: 部门
 * Copyright (C), 2024, 麒技团队
 * @date 2024 /9/7 2:42
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface DepartmentMapping {

    /**
     * To department vm department vm.
     *
     * @param department the department
     * @return the department vm
     */
    DepartmentVM toDepartmentVM(Department department);

    /**
     * To department vm list list.
     *
     * @param departmentList the department list
     * @return the list
     */
    List<DepartmentVM> toDepartmentVM(List<Department> departmentList);
}
