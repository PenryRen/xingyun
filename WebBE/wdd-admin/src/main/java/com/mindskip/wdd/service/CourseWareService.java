package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.CourseWare;
import com.mindskip.wdd.domain.CourseWareQuestion;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareEditRequestVM;
import com.mindskip.wdd.viewmodel.course.ware.CourseWarePageRequestVM;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 课件
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface CourseWareService extends IService<CourseWare> {
    /**
     * 课件分页
     *
     * @param requestVM
     * @return
     */
    PageInfo<CourseWare> page(CourseWarePageRequestVM requestVM);


    /**
     * 删除课件题目
     *
     * @param courseWareId
     */
    void deleteCourseWareQuestionByCourseWareId(Integer courseWareId);


    /**
     * 插入课件题目
     *
     * @param courseWareQuestion
     */
    void insertCourseWareQuestion(CourseWareQuestion courseWareQuestion);

    /**
     * 获取课件列表
     *
     * @param courseWareId
     * @return {@link List}<{@link CourseWareQuestion}>
     */
    List<CourseWareQuestion> getCourseWareQuestion(Integer courseWareId);

    String warName(Integer id);

    void updatewarTrain(CourseWareEditRequestVM model);
}
