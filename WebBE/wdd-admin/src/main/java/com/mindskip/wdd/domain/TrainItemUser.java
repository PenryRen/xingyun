package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 9.0.0
 * @description: 用户培训列表
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/26 10:45
 */
@Getter
@Setter
public class TrainItemUser {
    private Long id;

    /**
     * 用户培训Id
     */
    private Long trainUserId;

    /**
     * 培训id
     */
    private Integer trainId;

    /**
     * 培训列表Id
     */
    private Long trainItemId;

    /**
     * 培训内容Id
     */
    private Integer targetId;

    /**
     * 培训内容类型
     */
    private Integer targetType;

    /**
     * 当前分线
     */
    private Integer currentNumber;

    /**
     * 合格分数
     */
    private Integer passNumber;

    /**
     * 最大分数
     */
    private Integer maxNumber;

    /**
     * 排序
     */
    private Integer itemOrder;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 创建人部门
     */
    private Integer createDepartmentId;

    /**
     * 用户培训合格内容Id
     */
    private Long userTargetId;

    /**
     * 完成时间
     */
    private Date completeTime;

    /**
     * 考试次数
     */
    private Integer allowCount;

}