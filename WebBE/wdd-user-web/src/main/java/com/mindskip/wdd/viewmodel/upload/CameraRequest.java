package com.mindskip.wdd.viewmodel.upload;

import lombok.Data;

/**
 * @version 9.0.0
 * @description: 人脸识别
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Data
public class CameraRequest {
    /**
     * 试卷id
     */
    private Long paperId;
    /**
     * 人脸图片
     */
    private String imageBase64;
}
