package com.mindskip.wdd.viewmodel.course.ware;

import lombok.Data;

import java.util.List;

/**
 * @version 8.5.0
 * @description: 课件题目
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/7 10:45
 */
@Data
public class CourseWareVM {
    private Integer id;

    /**
     * 课件名称
     */
    private String name;

    /**
     * 课件类型
     */
    private Integer fileType;

    /**
     * 课件原始文件路径
     */
    private String originalPath;

    /**
     * 课件文件名
     */
    private String fileName;

    /**
     * 课件简介
     */
    private String description;

    /**
     * 课件预览路径
     */
    private String previewPath;

    /**
     * 创建人头像
     */
    private String createImagePath;
    /**
     * 创建人用户名
     */
    private String createUserName;
    /**
     * 创建人真实姓名
     */
    private String createRealName;

    /**
     * 当前观看时间
     */
    private Integer currentTime;

    /**
     * 课件题目列表
     */
    private List<CourseWareQuestionVM> courseWareQuestionVMList;

    /**
     * 实训环境
     */
    private String vmType;
}