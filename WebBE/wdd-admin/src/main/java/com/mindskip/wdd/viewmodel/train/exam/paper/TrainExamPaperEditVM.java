package com.mindskip.wdd.viewmodel.train.exam.paper;

import com.mindskip.wdd.domain.frame.ExamPaperBuildConfig;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperTitleItemVM;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训试卷编辑
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Data
public class TrainExamPaperEditVM {
    private Integer id;

    /**
     * 试卷分类id
     */
    private Integer examPaperArchiveId;


    /**
     * 试卷名称
     */
    @NotBlank(message = "试卷名称不能为空")
    @Length(max = 255, message = "时间名称长度最大为255个字符")
    private String name;


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
     * 试卷总分
     */
    @NotBlank(message = "试卷总分不能为空")
    private String sumScore;

    /**
     * 试卷题目总数
     */
    private Integer questionCount;

    /**
     * 题目打乱
     */
    private Boolean questionMess;

    /**
     * 选项打乱
     */
    private Boolean questionItemMess;

    /**
     * 防作弊
     */
    private Boolean cheat;

    /**
     * 防作弊次数
     */
    private Integer maxCheatCount;

    /**
     * 分数
     */
    private String score;
}
