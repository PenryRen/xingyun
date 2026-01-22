package com.mindskip.wdd.viewmodel.train.detail;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @version 1.7.0
 * @description: 课程编辑
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class TrainDetailVM {

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

    /**
     * 创建者用户名
     */
    private String createUserName;

    /**
     * 通过数
     */
    private Integer passCount;
    /**
     * 未通过数
     */
    private Integer noPassCount;
    /**
     * 进行中数
     */
    private Integer goingCount;
    /**
     * 未开始数
     */
    private Integer noStarCount;
    /**
     * 部门所有人数
     */
    private Integer allUserCount;

    /**
     * 待批改数
     */
    private Integer judgeCount;
    /**
     * 试卷总数
     */
    private Integer paperCount;
    /**
     * 试卷进行中
     */
    private Integer paperGoing;
    /**
     * 未通过数
     */
    private Integer paperNoPass;
    /**
     * 通过数
     */
    private Integer paperPass;
}
