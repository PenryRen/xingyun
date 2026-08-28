package com.mindskip.wdd.viewmodel.exam.build;

import com.mindskip.wdd.domain.frame.ExamPaperBuildConfig;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperTitleItemVM;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 组卷规则
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ExamPaperBuildEditRequestVM {
    private Long id;

    /**
     * 试卷分类id
     */
    private Integer examPaperArchiveId;

    /**
     * 试卷类型
     */
    @NotNull
    private Integer buildType;

    /**
     * 试卷名称
     */
    @NotBlank(message = "试卷名称不能为空")
    @Length(max = 255, message = "时间名称长度最大为255个字符")
    private String name;

    /**
     * 考试范围
     */
    @NotNull(message = "考试范围不能为空")
    private Integer rangeType;

    /**
     * 组卷规则配置
     */
    private ExamPaperBuildConfig buildConfig;

    /**
     * 试卷结构
     */
    private List<ExamPaperTitleItemVM> titleItems;

    /**
     * 考试时长
     */
    @NotNull
    private Integer suggestTime;

    /**
     * 考试时间
     */
    private List<String> limitDateTime;

    /**
     * 及格分
     */
    @NotBlank(message = "及格分不能为空")
    private String passScore;

    /**
     * 试卷总分
     */
    @NotBlank(message = "试卷总分不能为空")
    private String sumScore;

    /**
     * 试卷题目总数
     */
    private Integer questionCount;

    /**
     * 试卷发布状态
     */
    private Integer buildStatus;

    /**
     * 合格证书模板
     */
    private Integer credentialTemplateId;

    /**
     * 实训环境
     */
    private String vmType;

}
