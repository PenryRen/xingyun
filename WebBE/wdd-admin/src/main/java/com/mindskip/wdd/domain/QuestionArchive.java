package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 题目分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Getter
@Setter
public class QuestionArchive {
    private Integer id;

    /**
     * 题目分类名称
     */
    private String name;

    /**
     * 创建者
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
     * 上级节点分类
     */
    private Integer parentId;

    /**
     * 分类层级
     */
    private String level;

    /**
     * 创建人部门id
     */
    private Integer createDepartmentId;

    /**
     * 排序
     */
    private Integer itemOrder;

}