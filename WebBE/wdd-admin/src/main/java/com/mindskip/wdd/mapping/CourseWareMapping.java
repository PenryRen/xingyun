package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.CourseWare;
import com.mindskip.wdd.domain.CourseWareQuestion;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareEditRequestVM;
import com.mindskip.wdd.viewmodel.course.ware.CourseWarePageResponseVM;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareQuestionVM;
import org.mapstruct.*;


/**
 * @version 1.7.0
 * @description: CourseWareMapping
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface CourseWareMapping {


    /**
     * To course ware response vm course ware page response vm.
     *
     * @param courseWare the course ware
     * @return the course ware page response vm
     */
    @Mappings({@Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(courseWare.getCreateTime()))")})
    CourseWarePageResponseVM toCourseWareResponseVM(CourseWare courseWare);

    /**
     * To course ware course ware.
     *
     * @param courseWareEditRequestVM the course ware edit request vm
     * @return the course ware
     */
    CourseWare toCourseWare(CourseWareEditRequestVM courseWareEditRequestVM);

    /**
     * Map course ware.
     *
     * @param courseWareEditRequestVM the course ware edit request vm
     * @param courseWare              the course ware
     */
    @InheritConfiguration
    void mapCourseWare(CourseWareEditRequestVM courseWareEditRequestVM, @MappingTarget CourseWare courseWare);

    /**
     * To course ware edit request vm course ware edit request vm.
     *
     * @param courseWare the course ware
     * @return the course ware edit request vm
     */
    CourseWareEditRequestVM toCourseWareEditRequestVM(CourseWare courseWare);


    CourseWareQuestionVM toCourseWareQuestionVM(CourseWareQuestion courseWareQuestion);


    CourseWareQuestion toCourseWareQuestion(CourseWareQuestionVM courseWareQuestionVM);

}
