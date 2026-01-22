package com.mindskip.wdd.viewmodel.apply;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 报名分页返回
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class ApplyPageResponseVM {

    private Integer id;

    /**
     * 报名名称
     */
    private String name;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 考试结束时间
     */
    private String limitEndTime;

    /**
     * 考试开始时间
     */
    private String limitStartTime;

    /**
     * 报名结束时间
     */
    private String applyEndTime;

    /**
     * 报名人数限制
     */
    private String limitCount;


}
