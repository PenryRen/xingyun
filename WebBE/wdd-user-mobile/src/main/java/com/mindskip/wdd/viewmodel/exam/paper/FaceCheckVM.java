package com.mindskip.wdd.viewmodel.exam.paper;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @version 8.1.0
 * @description: 人脸识别
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/3 10:45
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
