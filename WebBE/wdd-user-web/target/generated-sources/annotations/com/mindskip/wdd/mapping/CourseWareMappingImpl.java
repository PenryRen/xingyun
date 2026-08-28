package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.CourseWare;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:40:39+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class CourseWareMappingImpl implements CourseWareMapping {

    @Override
    public CourseWareVM toCourseWareVm(CourseWare courseWare) {
        if ( courseWare == null ) {
            return null;
        }

        CourseWareVM courseWareVM = new CourseWareVM();

        courseWareVM.setDescription( courseWare.getDescription() );
        courseWareVM.setFileName( courseWare.getFileName() );
        courseWareVM.setFileType( courseWare.getFileType() );
        courseWareVM.setId( courseWare.getId() );
        courseWareVM.setName( courseWare.getName() );
        courseWareVM.setOriginalPath( courseWare.getOriginalPath() );
        courseWareVM.setPreviewPath( courseWare.getPreviewPath() );
        courseWareVM.setVmType( courseWare.getVmType() );

        return courseWareVM;
    }
}
