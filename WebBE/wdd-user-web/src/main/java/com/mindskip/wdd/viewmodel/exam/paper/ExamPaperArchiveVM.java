package com.mindskip.wdd.viewmodel.exam.paper;

import lombok.Data;

import java.util.List;


/**
 * @version 1.9.0
 * @description: 试卷分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/02 9:45
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
     * 子节点
     */
    private List<ExamPaperArchiveVM> child;
}

