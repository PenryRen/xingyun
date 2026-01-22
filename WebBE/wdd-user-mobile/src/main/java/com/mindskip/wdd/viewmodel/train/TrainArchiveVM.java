package com.mindskip.wdd.viewmodel.train;

import lombok.Data;

import java.util.List;


/**
 * @version 9.0.0
 * @description: 培训分类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/16 10:45
 */
@Data
public class TrainArchiveVM {

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
    private List<TrainArchiveVM> child;
}

