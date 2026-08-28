package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 10.0.0
 * @description: 课件分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/12/16 10:45
 */
@Getter
@Setter
public class CourseWareArchive {
    private Integer id;

    /**
     * 分类名称
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
     * 上级节点id
     */
    private Integer parentId;

    /**
     * 层级
     */
    private String level;

    /**
     * 创建人部门
     */
    private Integer createDepartmentId;

    /**
     * 排序
     */
    private Integer itemOrder;

}