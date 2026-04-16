package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 8.5.0
 * @description: 课件题目
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/7 10:45
 */
@Getter
@Setter
public class CourseWareQuestion {
    private Long id;

    /**
     * 课件Id
     */
    private Integer courseWareId;

    /**
     * 题目Id
     */
    private Long questionId;

    /**
     * 视频断点
     */
    private String anchorFormat;

    /**
     * 视频断点 秒
     */
    private Integer anchorSecond;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建部门
     */
    private Integer createDepartmentId;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 题目内容
     */
    private String questionFrameId;
}