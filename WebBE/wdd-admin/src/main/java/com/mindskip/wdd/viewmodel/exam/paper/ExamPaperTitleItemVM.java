package com.mindskip.wdd.viewmodel.exam.paper;

import com.mindskip.wdd.viewmodel.question.QuestionEditRequestVM;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 试卷标题
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class ExamPaperTitleItemVM {

    /**
     * 标题内容
     */
    @NotBlank(message = "标题内容不能为空")
    private String name;

    /**
     * 题目列表
     */
    @Size(min = 1, message = "请添加题目")
    @Valid
    private List<QuestionEditRequestVM> questionItems;
}
