package com.mindskip.wdd.domain.frame;

import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 随机组卷题目配置
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class ExamPaperBuildRandom {
    /**
     * 题型
     */
    private Integer questionType;
    /**
     * 题目分类
     */
    private Integer questionArchiveId;
    /**
     * 题目分类
     */
    private String questionArchiveStr;
    /**
     * 题目数量
     */
    private Integer number;
    /**
     * 题目分数
     */
    private String score;
    /**
     * 题目难度
     */
    private Integer difficult;
}
