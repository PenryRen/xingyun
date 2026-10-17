package com.mindskip.wdd.viewmodel.exam.build;

import com.mindskip.wdd.domain.frame.ExamPaperBuildConfig;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperTitleItemVM;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @version 7.0.0
 * @description: 组卷规则
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/18 14:45
 */
@Data
public class RandomQuestionCheckVM {

    /**
     * 题型
     */
    @NotNull(message = "题型不能为空")
    private Integer questionType;
    /**
     * 题目分类
     */
    @NotNull(message = "分类不能为空")
    private Integer questionArchiveId;
    /**
     * 题目分类
     */
    private Integer questionArchiveStr;
    /**
     * 数量
     */
    @NotNull(message = "题数不能为空")
    private Integer number;
    /**
     * 分数
     */
    @NotBlank(message = "分数不能为空")
    private String score;
    /**
     * 难度
     */
    @NotNull(message = "难度不能为空")
    @Min(value = 1, message = "难度不能为空")
    private Integer difficult;

}
