package com.mindskip.wdd.viewmodel.train.exam.answer;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训答卷分页请求
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/26 10:45
 */
@Data
public class TrainPaperAnswerPageRequestVM extends BasePage {
    @NotNull
    private Integer trainId;
    /**
     * 答卷状态
     */
    private Integer status;
    /**
     * 部门
     */
    private List<Integer> departmentIdList;

    private String minScoreStr;
    /**
     * 最小分数
     */
    private Integer minScore;

    private String maxScoreStr;

    /**
     * 最大分数
     */
    private Integer maxScore;

    /**
     * 用户名
     */
    private String userName;
}
