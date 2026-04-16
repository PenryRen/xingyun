package com.mindskip.wdd.viewmodel.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 7.5.0
 * @description: 分类树形对象
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/28 10:45
 */
@Data
public class ArchiveVM {
    /**
     * 分类Id
     */
    private Integer id;

    /**
     * 分类Id
     */
    private Integer value;

    /**
     * 分类名称
     */
    private String label;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 子节点
     */
    private List<ArchiveVM> children = new ArrayList<>();
}