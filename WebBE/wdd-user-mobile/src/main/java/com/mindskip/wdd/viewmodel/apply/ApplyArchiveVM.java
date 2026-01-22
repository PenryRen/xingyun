package com.mindskip.wdd.viewmodel.apply;

import lombok.Data;

import java.util.List;


/**
 * @version 6.0.0
 * @description: 报名分类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/15 10:28
 */
@Data
public class ApplyArchiveVM {

    /**
     * 分类id
     */
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 层级
     */
    private String level;

    /**
     * 子节点
     */
    private List<ApplyArchiveVM> child;
}

