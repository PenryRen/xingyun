package com.mindskip.wdd.viewmodel.exam.paper;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @version 8.1.0
 * @description: 人脸识别
 * Copyright (C), 2025, 麟航团队
 * @date 2025/10/3 10:45
 */
@Data
public class FaceCheckVM {
    @NotNull
    private Long paperId;
    @NotNull
    private Integer paperType;
    @NotBlank
    private String imageUrl;
}
