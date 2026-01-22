package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.CourseWareQuestion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 8.5.0
 * @description: 课件题目
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/7 10:45
 */
@Mapper
public interface CourseWareQuestionMapper extends BaseMapper<CourseWareQuestion> {

    /**
     * 获取课件题目列表
     *
     * @param courseWareId
     * @return {@link List}<{@link CourseWareQuestion}>
     */
    List<CourseWareQuestion> getCourseWareQuestion(Integer courseWareId);
}