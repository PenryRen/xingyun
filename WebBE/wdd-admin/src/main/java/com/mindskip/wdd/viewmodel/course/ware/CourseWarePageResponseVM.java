package com.mindskip.wdd.viewmodel.course.ware;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 课件分页返回
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class CourseWarePageResponseVM {

    private Integer id;

    /**
     * 课件名称
     */
    private String name;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 课件类型
     */
    private Integer fileType;

    /**
     * 课件类型
     */
    private String fileTypeStr;

    /**
     * 课件文件地址
     */
    private String filePath;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 课件分类
     */
    private String level;

    /**
     * 课件时长
     */
    private Integer maxLength;
    private String maxLengthStr;

    /**
     * 课件预览
     */
    private String previewPath;

    /**
     * 实训环境
     */
    private String vmType;
}
