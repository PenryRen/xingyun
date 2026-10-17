package com.mindskip.wdd.viewmodel.course.ware;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * @version 9.0.0
 * @description: 课件观看
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Data
public class WatchRequestVM {
    /**
     * 用户培训项
     */
    @NotNull
    private Long trainItemUserId;
    /**
     * 课件id
     */
    @NotNull
    private Integer courseWareId;
    /**
     * 当前观看时间
     */
    @NotNull
    private Integer watchTime;
    /**
     * 是否观看完
     */
    @NotNull
    private Boolean watchEnd;
}
