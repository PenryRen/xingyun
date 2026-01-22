package com.mindskip.wdd.viewmodel.train.course;

import lombok.Data;

import java.util.Date;

/**
 * @version 9.0.0
 * @description: 培训课件用户分页
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/26 10:45
 */
@Data
public class TrainCourseWarePageResponseVM {
    private Long id;
    /**
     * 课件名称
     */
    private String name;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 当前分数线
     */
    private Integer currentNumber;
    private String currentNumberStr;
    /**
     * 合格分数线
     */
    private Integer passNumber;
    private String passNumberStr;
    /**
     * 状态
     */
    private Integer status;
    private String statusStr;
    /**
     * 完成时间
     */
    private Date completeTime;
    private String completeTimeStr;
}
