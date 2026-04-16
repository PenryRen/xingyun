package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 9.0.0
 * @description: 培训列表
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Getter
@Setter
public class TrainItem {
    private Long id;

    /**
     * 培训Id
     */
    private Integer trainId;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 培训内容Id
     */
    private Integer targetId;

    /**
     * 培训内容类型
     */
    private Integer targetType;

    /**
     * 合格分数线
     */
    private Integer passNumber;

    /**
     * 最大分数线
     */
    private Integer maxNumber;

    /**
     * 排序
     */
    private Integer itemOrder;

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
     * 考试次数
     */
    private Integer allowCount;

    /**
     * 文件类型
     */
    private Integer fileType;

}