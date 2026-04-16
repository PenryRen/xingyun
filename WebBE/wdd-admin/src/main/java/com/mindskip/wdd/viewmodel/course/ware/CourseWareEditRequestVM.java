package com.mindskip.wdd.viewmodel.course.ware;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 课件编辑
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class CourseWareEditRequestVM {

    private Integer id;

    /**
     * 课件名称
     */
    @NotBlank(message = "课件名称不能为空")
    private String name;

    /**
     * 课件分类id
     */
    private Integer courseWareArchiveId;

    /**
     * 课件描述
     */
    @NotBlank(message = "课件描述不能为空")
    private String description;

    /**
     * 课件类型
     */
    @NotNull(message = "课件类型不能为空")
    private Integer fileType;

    /**
     * 课件文件地址
     */
    @NotBlank(message = "文件地址不能为空")
    private String originalPath;

    /**
     * 课件预览地址
     */
    private String previewPath;

    /**
     * 文件名
     */
    @NotBlank(message = "文件名称不能为空")
    private String fileName;

    /**
     * 最大时长
     */
    private Integer maxLength;

    private String maxLengthStr;

    /**
     * 视频题目列表
     */
    private List<CourseWareQuestionVM> questionList;

    /**
     * 实训环境
     */
    private String vmType;
}
