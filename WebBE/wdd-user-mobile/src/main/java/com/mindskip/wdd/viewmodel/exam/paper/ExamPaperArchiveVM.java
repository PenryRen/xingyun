package com.mindskip.wdd.viewmodel.exam.paper;

import lombok.Data;

import java.util.List;


/**
 * @version 6.0.0
 * @description: 试卷分类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/6 2:45
 */
@Data
public class ExamPaperArchiveVM {

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
    private List<ExamPaperArchiveVM> child;
}

