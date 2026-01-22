package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 用户抓拍
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Getter
@Setter
public class ExamPaperUserCamera {
    private Long id;

    /**
     * 试卷id
     */
    private Long examPaperId;

    /**
     * 抓拍图片地址
     */
    private String imagePath;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建者部门
     */
    private Integer createDepartmentId;

}