package com.mindskip.wdd.viewmodel.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @version 5.9.0
 * @description: 部门列表
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/7 2:42
 */
@Data
public class DepartmentVM implements Serializable {

    private static final long serialVersionUID = 5699556594925163730L;

    private Integer id;

    /**
     * 部门名称
     */
    private String name;

    private String level;


    /**
     * 子部门
     */
    private List<DepartmentVM> child;
}
