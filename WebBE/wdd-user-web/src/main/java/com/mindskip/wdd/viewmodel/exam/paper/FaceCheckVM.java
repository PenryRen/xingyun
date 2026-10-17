package com.mindskip.wdd.viewmodel.exam.paper;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @version 8.1.0
 * @description: 人脸识别
 * Copyright (C), 2025, 麟航团队
 * @date 2025/10/3 10:45
 */
@Data
public class FaceCheckVM {
    /**
     * 试卷id
     */
    @NotNull
    private Long id;
    /**
     * 试卷类型
     */
    @NotNull
    private Integer paperType;

    /**
     * 人脸图片
     */
    @NotBlank
    private String imageBase;
}
