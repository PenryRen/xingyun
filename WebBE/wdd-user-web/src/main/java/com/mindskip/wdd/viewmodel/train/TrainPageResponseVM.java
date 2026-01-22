package com.mindskip.wdd.viewmodel.train;

import lombok.Data;

import java.util.Date;

/**
 * @version 9.0.0
 * @description: 培训分页返回
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/16 10:45
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
     * 培训项目数量
     */
    private Integer itemCount;

    /**
     * 培训时长
     */
    private String studyTimeStr;
}
