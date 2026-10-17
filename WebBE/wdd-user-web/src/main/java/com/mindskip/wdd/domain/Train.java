package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 9.0.0
 * @description: 培训
 * Copyright (C), 2025, 麟航团队
 * @date 2025/10/26 10:45
 */
@Getter
@Setter
public class Train {
    private Integer id;

    /**
     * 课程分类
     */
    private Integer trainArchiveId;

    /**
     * 课程名称
     */
    private String name;

    /**
     * 课程描述
     */
    private String description;

    /**
     * 封面
     */
    private String coverPath;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 积分
     */
    private Integer creditPoint;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人部门
     */
    private Integer createDepartmentId;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 节点数
     */
    private Integer itemCount;

    /**
     * 学习时长
     */
    private Integer studyTime;

}