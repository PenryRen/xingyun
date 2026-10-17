package com.mindskip.wdd.viewmodel.course.ware;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 课件文件
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class CourseWareFileVM {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件地址
     */
    private String path;
    /**
     * 原始文件路径
     */
    private String originalPath;
    /**
     * 文件预览地址
     */
    private String previewPath;

    /**
     * 视频长度
     */
    private Integer videoLength;
}
