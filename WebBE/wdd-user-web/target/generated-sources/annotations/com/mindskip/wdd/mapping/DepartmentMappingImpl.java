package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Department;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.common.DepartmentVM;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-28T15:49:51+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class DepartmentMappingImpl implements DepartmentMapping {

    @Override
    public DepartmentVM toDepartmentVM(Department department) {
        if ( department == null ) {
            return null;
        }

        DepartmentVM departmentVM = new DepartmentVM();

        departmentVM.setId( department.getId() );
        departmentVM.setLevel( department.getLevel() );
        departmentVM.setName( department.getName() );

        return departmentVM;
    }

    @Override
    public List<DepartmentVM> toDepartmentVM(List<Department> departmentList) {
        if ( departmentList == null ) {
            return null;
        }

        List<DepartmentVM> list = new ArrayList<DepartmentVM>( departmentList.size() );
        for ( Department department : departmentList ) {
            list.add( toDepartmentVM( department ) );
        }

        return list;
    }
}
