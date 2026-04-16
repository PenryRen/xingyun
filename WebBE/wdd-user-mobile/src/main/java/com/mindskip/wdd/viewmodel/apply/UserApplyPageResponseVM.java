package com.mindskip.wdd.viewmodel.apply;

import lombok.Data;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 用户报名记录
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class UserApplyPageResponseVM {

    private Long id;

    /**
     * 报名名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 考试结束时间
     */
    private Date limitEndTime;

    /**
     * 考试开始时间
     */
    private Date limitStartTime;

    /**
     * 报名结束时间
     */
    private Date applyEndTime;

    /**
     * 报名时间
     */
    private Date appDateTime;

    /**
     * 报名人数限制
     */
    private String limitCount;

    /**
     * 是否限制人数
     */
    private Boolean limited;

    /**
     * 总人数
     */
    private Integer count;

    /**
     * 已报名人数
     */
    private Integer alreadyApplyCount;

    /**
     * 创建时间
     */
    private String createTimeStr;

    /**
     * 考试结束时间
     */
    private String limitEndTimeStr;

    /**
     * 考试开始时间
     */
    private String limitStartTimeStr;

    /**
     * 报名结束时间
     */
    private String applyEndTimeStr;

    /**
     * 报名时间
     */
    private String applyDateTimeStr;

    /**
     * 报名id
     */
    private Integer applyId;

    private Integer status;

    private String statusStr;

}
