package com.mindskip.wdd.mapping;


import com.mindskip.wdd.domain.Department;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.department.DepartmentEditRequestVM;
import com.mindskip.wdd.viewmodel.department.DepartmentTreeVM;
import org.mapstruct.*;

import java.util.List;


/**
 * @version 1.7.0
 * @description: DepartmentMapping
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface DepartmentMapping {


    /**
     * To department department.
     *
     * @param departmentEditRequestVM the department edit request vm
     * @return the department
     */
    Department toDepartment(DepartmentEditRequestVM departmentEditRequestVM);

    /**
     * Map department.
     *
     * @param departmentEditRequestVM the department edit request vm
     * @param department              the department
     */
    @InheritConfiguration
    void mapDepartment(DepartmentEditRequestVM departmentEditRequestVM, @MappingTarget Department department);

    /**
     * To department edit request vm department edit request vm.
     *
     * @param department the department
     * @return the department edit request vm
     */
    DepartmentEditRequestVM toDepartmentEditRequestVM(Department department);

    @Mappings({
            @Mapping(target = "value",source = "id"),
            @Mapping(target = "label",source = "name"),
    })
    DepartmentTreeVM toDepartmentTreeVM(Department department);

    List<DepartmentTreeVM> toDepartmentTreeVMList(List<Department> departmentList);

}
