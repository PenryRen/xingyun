package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.Train;
import com.mindskip.wdd.viewmodel.train.course.TrainCoursePageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/16 10:45
 */
@Mapper
public interface TrainMapper extends BaseMapper<Train> {

    /**
     * 课程
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<Train> page(TrainCoursePageRequestVM requestVM);
}