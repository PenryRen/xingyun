package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.CourseWare;
import com.mindskip.wdd.viewmodel.course.ware.CourseWarePageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 课件
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface CourseWareMapper extends BaseMapper<CourseWare> {
    /**
     * 课件分页
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<CourseWare> page(CourseWarePageRequestVM requestVM);
}