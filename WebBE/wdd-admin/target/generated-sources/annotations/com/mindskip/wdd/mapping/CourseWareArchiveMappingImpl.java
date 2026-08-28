package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.CourseWareArchive;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareArchiveEditRequestVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:40:32+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class CourseWareArchiveMappingImpl implements CourseWareArchiveMapping {

    @Override
    public CourseWareArchive toCourseWareArchive(CourseWareArchiveEditRequestVM courseWareArchiveEditRequestVM) {
        if ( courseWareArchiveEditRequestVM == null ) {
            return null;
        }

        CourseWareArchive courseWareArchive = new CourseWareArchive();

        courseWareArchive.setDeleted( courseWareArchiveEditRequestVM.getDeleted() );
        courseWareArchive.setId( courseWareArchiveEditRequestVM.getId() );
        courseWareArchive.setItemOrder( courseWareArchiveEditRequestVM.getItemOrder() );
        courseWareArchive.setName( courseWareArchiveEditRequestVM.getName() );
        courseWareArchive.setParentId( courseWareArchiveEditRequestVM.getParentId() );

        return courseWareArchive;
    }

    @Override
    public void mapCourseWareArchive(CourseWareArchiveEditRequestVM courseWareArchiveEditRequestVM, CourseWareArchive courseWareArchive) {
        if ( courseWareArchiveEditRequestVM == null ) {
            return;
        }

        courseWareArchive.setDeleted( courseWareArchiveEditRequestVM.getDeleted() );
        courseWareArchive.setId( courseWareArchiveEditRequestVM.getId() );
        courseWareArchive.setItemOrder( courseWareArchiveEditRequestVM.getItemOrder() );
        courseWareArchive.setName( courseWareArchiveEditRequestVM.getName() );
        courseWareArchive.setParentId( courseWareArchiveEditRequestVM.getParentId() );
    }

    @Override
    public CourseWareArchiveEditRequestVM toCourseWareArchiveEditRequestVM(CourseWareArchive courseWareArchive) {
        if ( courseWareArchive == null ) {
            return null;
        }

        CourseWareArchiveEditRequestVM courseWareArchiveEditRequestVM = new CourseWareArchiveEditRequestVM();

        courseWareArchiveEditRequestVM.setDeleted( courseWareArchive.getDeleted() );
        courseWareArchiveEditRequestVM.setId( courseWareArchive.getId() );
        courseWareArchiveEditRequestVM.setItemOrder( courseWareArchive.getItemOrder() );
        courseWareArchiveEditRequestVM.setName( courseWareArchive.getName() );
        courseWareArchiveEditRequestVM.setParentId( courseWareArchive.getParentId() );

        return courseWareArchiveEditRequestVM;
    }

    @Override
    public ArchiveVM toArchiveVM(CourseWareArchive courseWareArchive) {
        if ( courseWareArchive == null ) {
            return null;
        }

        ArchiveVM archiveVM = new ArchiveVM();

        archiveVM.setValue( courseWareArchive.getId() );
        archiveVM.setLabel( courseWareArchive.getName() );
        archiveVM.setId( courseWareArchive.getId() );
        archiveVM.setName( courseWareArchive.getName() );

        return archiveVM;
    }
}
