package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.CourseWare;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareVM;
import org.mapstruct.Mapper;

/**
 * @version 1.7.0
 * @description: The interface Course ware mapping.
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface CourseWareMapping {

    CourseWareVM toCourseWareVm(CourseWare courseWare);

}
