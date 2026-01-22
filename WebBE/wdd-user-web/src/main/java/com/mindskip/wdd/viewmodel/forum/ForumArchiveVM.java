package com.mindskip.wdd.viewmodel.forum;

import lombok.Data;

import java.util.List;


/**
 * @version 1.9.0
 * @description: 文章分类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/7/1 10:45
 */
@Data
public class ForumArchiveVM {

    /**
     * 分类id
     */
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 子节点
     */
    private List<ForumArchiveVM> child;
}

