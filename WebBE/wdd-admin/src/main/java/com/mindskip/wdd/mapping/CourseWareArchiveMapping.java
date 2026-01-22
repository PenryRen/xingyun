package com.mindskip.wdd.mapping;


import com.mindskip.wdd.domain.CourseWareArchive;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareArchiveEditRequestVM;
import org.mapstruct.*;


/**
 * @version 6.0.0
 * @description: 课件分类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/15 10:28
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface CourseWareArchiveMapping {


    /**
     * To courseWare archive courseWare archive.
     *
     * @param courseWareArchiveEditRequestVM the courseWare archive edit request vm
     * @return the courseWare archive
     */
    CourseWareArchive toCourseWareArchive(CourseWareArchiveEditRequestVM courseWareArchiveEditRequestVM);

    /**
     * Map courseWare archive.
     *
     * @param courseWareArchiveEditRequestVM the courseWare archive edit request vm
     * @param courseWareArchive              the courseWare archive
     */
    @InheritConfiguration
    void mapCourseWareArchive(CourseWareArchiveEditRequestVM courseWareArchiveEditRequestVM, @MappingTarget CourseWareArchive courseWareArchive);

    /**
     * To courseWare archive edit request vm courseWare archive edit request vm.
     *
     * @param courseWareArchive the courseWare archive
     * @return the courseWare archive edit request vm
     */
    CourseWareArchiveEditRequestVM toCourseWareArchiveEditRequestVM(CourseWareArchive courseWareArchive);


    @Mappings({
            @Mapping(target = "value", source = "id"),
            @Mapping(target = "label", source = "name"),
    })
    ArchiveVM toArchiveVM(CourseWareArchive courseWareArchive);

}
