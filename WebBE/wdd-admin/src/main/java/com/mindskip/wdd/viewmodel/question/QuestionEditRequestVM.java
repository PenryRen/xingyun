package com.mindskip.wdd.viewmodel.question;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 题目编辑
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class QuestionEditRequestVM {

    private Long id;
    /**
     * 题目类型
     */
    @NotNull
    private Integer questionType;

    /**
     * 题目分类
     */
    private Integer questionArchiveId;

    /**
     * 实训环境
     */
    private String vmType;

    /**
     * 题干
     */
    @NotBlank(message = "题干不能为空")
    private String title;

    /**
     * 题目详情
     */
    private String questionFrameId;

    /**
     * 题目选项
     */
    @Valid
    private List<QuestionEditItemVM> items;

    /**
     * 解析
     */
    private String analyze;

    /**
     * 正确答案
     */
    private List<String> correctArray;

    /**
     * 实训题 规则类型
     */
    private String commandType;

    /**
     * 实训题 执行命令
     */
    private String command;

    /**
     * 标答
     */
    private String correct;

    /**
     * 分数
     */
    private String score;

    /**
     * 难度
     */
    @Range(min = 1, max = 3, message = "请选择题目难度")
    private Integer difficult;

    /**
     * 排除
     */
    private Integer itemOrder;

}
