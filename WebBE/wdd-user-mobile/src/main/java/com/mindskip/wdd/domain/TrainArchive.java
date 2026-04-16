package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 9.0.0
 * @description: 课程分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Getter
@Setter
public class TrainArchive {
    private Integer id;

    /**
     * 课程分类名称
     */
    private String name;

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
     * 上级节点Id
     */
    private Integer parentId;

    /**
     * 节点层级
     */
    private String level;

    /**
     * 创建人部门Id
     */
    private Integer createDepartmentId;

    /**
     * 排序
     */
    private Integer itemOrder;

}