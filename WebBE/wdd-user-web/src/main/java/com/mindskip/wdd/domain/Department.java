package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 证书模板
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Getter
@Setter
public class Department {
    private Integer id;

    /**
     * 部门名称
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
     * 父节点id
     */
    private Integer parentId;

    /**
     * 部门层级
     */
    private String level;

    /**
     * 创建者部门id
     */
    private Integer createDepartmentId;

    /**
     * 排序
     */
    private Integer itemOrder;

}