package com.mindskip.wdd.viewmodel.train;

import lombok.Data;

/**
 * @version 9.0.0
 * @description: 培训分页返回
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Data
public class TrainPageResponseVM {
    private Integer id;

    /**
     * 培训名称
     */
    private String name;

    /**
     * 培训封面
     */
    private String coverPath;

    /**
     * 课程描述
     */
    private String description;

    /**
     * 培训项目数量
     */
    private Integer itemCount;

    /**
     * 培训时长
     */
    private String studyTimeStr;
}
