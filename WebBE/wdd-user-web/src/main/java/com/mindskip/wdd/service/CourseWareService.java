package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mindskip.wdd.domain.CourseWare;
import com.mindskip.wdd.domain.CourseWareWatch;
import com.mindskip.wdd.domain.CourseWareWatchDetail;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareQuestionVM;

import java.util.Date;
import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训课件
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
public interface CourseWareService extends IService<CourseWare> {

    /**
     * 插件课件观看记录
     *
     * @param courseWareWatch
     */
    void courseWareWatchInsert(CourseWareWatch courseWareWatch);

    /**
     * 课件观看记录更新
     *
     * @param courseWareWatch
     */
    void courseWareWatchUpdate(CourseWareWatch courseWareWatch);

    /**
     * 获取课件观看记录
     *
     * @param userId
     * @param courseWareId
     * @return {@link CourseWareWatch}
     */
    CourseWareWatch getCourseWareWatch(Integer userId, Integer courseWareId);

    /**
     * 课件观看详情
     *
     * @param courseWareWatchDetail
     */
    void courseWareWatchDetailInsert(CourseWareWatchDetail courseWareWatchDetail);

    /**
     * 获取最新课件观看详情
     *
     * @param userId
     * @param courseWareId
     * @return {@link CourseWareWatchDetail}
     */
    CourseWareWatchDetail getLastItem(Integer userId, Integer courseWareId);

    /**
     * 获取课件观看间隔
     *
     * @return {@link Integer}
     */
    Integer getWatchInterval();

    /**
     * 获取课件题目
     *
     * @param courseWareId
     * @return {@link List}<{@link CourseWareQuestionVM}>
     */
    List<CourseWareQuestionVM> getCourseWareQuestion(Integer courseWareId);

    /**
     * 课件观看时长统计
     *
     * @param courseWareId
     * @param userId
     * @param startTime
     * @return {@link Integer}
     */
    Integer watchUserSum(Integer courseWareId, Integer userId, Date startTime);
}
