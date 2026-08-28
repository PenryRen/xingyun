package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.CourseWare;
import com.mindskip.wdd.domain.CourseWareQuestion;
import com.mindskip.wdd.repository.*;
import com.mindskip.wdd.service.CourseWareService;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareEditRequestVM;
import com.mindskip.wdd.viewmodel.course.ware.CourseWarePageRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 课件
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@AllArgsConstructor
public class CourseWareServiceImpl extends ServiceImpl<CourseWareMapper, CourseWare> implements CourseWareService {

    private final CourseWareMapper courseWareMapper;
    private final CourseWareQuestionMapper courseWareQuestionMapper;


    @Override
    public PageInfo<CourseWare> page(CourseWarePageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                courseWareMapper.page(requestVM)
        );
    }


    @Override
    public void deleteCourseWareQuestionByCourseWareId(Integer courseWareId) {
        courseWareQuestionMapper.updateDeleteByCourseWareId(courseWareId);
    }

    @Override
    public void insertCourseWareQuestion(CourseWareQuestion courseWareQuestion) {
        courseWareQuestionMapper.insert(courseWareQuestion);
    }

    @Override
    public List<CourseWareQuestion> getCourseWareQuestion(Integer courseWareId) {
        return courseWareQuestionMapper.getCourseWareQuestion(courseWareId);
    }

    @Override
    public String warName(Integer id) {
        return courseWareQuestionMapper.warName(id);
    }

    @Override
    public void updatewarTrain(CourseWareEditRequestVM model) {
        courseWareQuestionMapper.updatewarTrain(model);
    }
}
