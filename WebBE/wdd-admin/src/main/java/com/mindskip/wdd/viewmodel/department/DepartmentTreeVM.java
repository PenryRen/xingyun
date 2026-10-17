package com.mindskip.wdd.viewmodel.department;

import lombok.Data;

import java.util.List;

/**
 * @version 4.4
 * @description: 部门树节点
 * Copyright (C), 2025, 麟航团队
 * @date 2025-10-23 10:13
 */
@Data
public class DepartmentTreeVM {

    /**
     * 部门id
     */
    private Integer id;

    /**
     * 部门id
     */
    private Integer value;

    /**
     * 部门名称
     */
    private String label;

    /**
     * 部门名称
     */
    private String name;

    private Boolean disabled;

    /**
     * 子部门
     */
    private List<DepartmentTreeVM> children;
}
