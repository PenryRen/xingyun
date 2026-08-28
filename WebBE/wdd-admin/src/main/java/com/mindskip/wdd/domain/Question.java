package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 题目
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Getter
@Setter
public class Question {
    private Long id;

    /**
     * 题目类型
     */
    private Integer questionType;

    /**
     * 题目分类id
     */
    private Integer questionArchiveId;

    /**
     * 题目分数
     */
    private Integer score;

    /**
     * 题目难度
     */
    private Integer difficult;

    /**
     * 题目内容
     */
    private String questionFrameId;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 状态
     */
    private Integer status;

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

}