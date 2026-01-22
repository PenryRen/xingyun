package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 试卷发布给个人
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Getter
@Setter
public class ExamPaperUser {
    private Long id;

    /**
     * 试卷id
     */
    private Long examPaperId;

    /**
     * 接收人id
     */
    private Integer userId;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 创建人
     */
    private Integer createUserId;

    /**
     * 创建人部门
     */
    private Integer createDepartmentId;

    /**
     * 答卷id
     */
    private Long examPaperAnswerId;

    /**
     * 考试开始时间
     */
    private Date limitStartTime;

    /**
     * 考试结束时间
     */
    private Date limitEndTime;

}