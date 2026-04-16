package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 课件
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Getter
@Setter
public class CourseWare {
    private Integer id;

    /**
     * 课件名称
     */
    private String name;

    /**
     * 创建者
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

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
     * 创建者部门
     */
    private Integer createDepartmentId;

    /**
     * 课件分类
     */
    private Integer courseWareArchiveId;

    /**
     * 最大时长
     */
    private Integer maxLength;

}