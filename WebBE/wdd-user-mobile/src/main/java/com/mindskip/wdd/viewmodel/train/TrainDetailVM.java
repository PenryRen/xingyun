package com.mindskip.wdd.viewmodel.train;

import lombok.Data;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 用户培训信息
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/16 10:45
 */
@Data
public class TrainDetailVM {

    private Integer id;

    /**
     * 培训名称
     */
    private String name;

    /**
     * 封面
     */
    private String coverPath;

    /**
     * 描述
     */
    private String description;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 培训内容数量
     */
    private Integer itemCount;

    /**
     * 培训时长
     */
    private String studyTimeStr;

    /**
     * 培训课件列表
     */
    private List<TrainDetailItem> courseWareList;

    /**
     * 培训试卷
     */
    private TrainDetailItem examPaper;

    /**
     * 培训证书
     */
    private TrainDetailItem credential;
}
