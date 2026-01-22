package com.mindskip.wdd.viewmodel.train.course;

import lombok.Data;

import java.util.Date;

/**
 * @version 9.0.0
 * @description: 课程分页返回
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/26 10:45
 */
@Data
public class TrainCoursePageResponseVM {

    private Integer id;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 创建时间
     */
    private String createTime;


    /**
     * 课程分类
     */
    private String level;

    private String studyTimeStr;


}
