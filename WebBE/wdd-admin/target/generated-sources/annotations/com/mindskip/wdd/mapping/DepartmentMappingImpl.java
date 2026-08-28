package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Department;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.department.DepartmentEditRequestVM;
import com.mindskip.wdd.viewmodel.department.DepartmentTreeVM;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:40:32+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class DepartmentMappingImpl implements DepartmentMapping {

    @Override
    public Department toDepartment(DepartmentEditRequestVM departmentEditRequestVM) {
        if ( departmentEditRequestVM == null ) {
            return null;
        }

        Department department = new Department();

        department.setDeleted( departmentEditRequestVM.getDeleted() );
        department.setId( departmentEditRequestVM.getId() );
        department.setItemOrder( departmentEditRequestVM.getItemOrder() );
        department.setName( departmentEditRequestVM.getName() );
        department.setParentId( departmentEditRequestVM.getParentId() );

        return department;
    }

    @Override
    public void mapDepartment(DepartmentEditRequestVM departmentEditRequestVM, Department department) {
        if ( departmentEditRequestVM == null ) {
            return;
        }

        department.setDeleted( departmentEditRequestVM.getDeleted() );
        department.setId( departmentEditRequestVM.getId() );
        department.setItemOrder( departmentEditRequestVM.getItemOrder() );
        department.setName( departmentEditRequestVM.getName() );
        department.setParentId( departmentEditRequestVM.getParentId() );
    }

    @Override
    public DepartmentEditRequestVM toDepartmentEditRequestVM(Department department) {
        if ( department == null ) {
            return null;
        }

        DepartmentEditRequestVM departmentEditRequestVM = new DepartmentEditRequestVM();

        departmentEditRequestVM.setDeleted( department.getDeleted() );
        departmentEditRequestVM.setId( department.getId() );
        departmentEditRequestVM.setItemOrder( department.getItemOrder() );
        departmentEditRequestVM.setName( department.getName() );
        departmentEditRequestVM.setParentId( department.getParentId() );

        return departmentEditRequestVM;
    }

    @Override
    public DepartmentTreeVM toDepartmentTreeVM(Department department) {
        if ( department == null ) {
            return null;
        }

        DepartmentTreeVM departmentTreeVM = new DepartmentTreeVM();

        departmentTreeVM.setValue( department.getId() );
        departmentTreeVM.setLabel( department.getName() );
        departmentTreeVM.setId( department.getId() );
        departmentTreeVM.setName( department.getName() );

        return departmentTreeVM;
    }

    @Override
    public List<DepartmentTreeVM> toDepartmentTreeVMList(List<Department> departmentList) {
        if ( departmentList == null ) {
            return null;
        }

        List<DepartmentTreeVM> list = new ArrayList<DepartmentTreeVM>( departmentList.size() );
        for ( Department department : departmentList ) {
            list.add( toDepartmentTreeVM( department ) );
        }

        return list;
    }
}
