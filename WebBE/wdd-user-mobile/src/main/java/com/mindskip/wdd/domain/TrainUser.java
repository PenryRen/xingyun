package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 9.0.0
 * @description: 用户培训
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/26 10:45
 */
@Getter
@Setter
public class TrainUser {
    private Long id;

    /**
     * 培训Id
     */
    private Integer trainId;

    /**
     * 通过数量
     */
    private Integer passCount;

    /**
     * 培训列表数
     */
    private Integer itemCount;

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
     * 创建部门
     */
    private Integer createDepartmentId;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 完成时间
     */
    private Date completeTime;

}