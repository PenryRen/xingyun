package com.mindskip.wdd.viewmodel.question;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 题目分页查询
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class QuestionPageRequestVM extends BasePage {
    private Long id;
    /**
     * 题型
     */
    private Integer questionType;
    /**
     * 题干
     */
    private String title;
    /**
     * 实训环境
     */
    private String vmType;
    /**
     * 是否排除实训题
     */
    private Boolean exclude;
    /**
     * 题目已选id
     */
    private List<Long> selectIdList;
    /**
     * 题目分类
     */
    private List<Integer> questionArchiveIdList;
}
