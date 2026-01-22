package com.mindskip.wdd.viewmodel.answer;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 答卷分页过滤
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class PaperAnswerPageRequestVM extends BasePage {
    private Long id;
    /**
     * 答卷状态
     */
    private Integer status;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 部门
     */
    private List<Integer> departmentIdList;
    /**
     * 试卷id
     */
    private Long examPaperId;
    /**
     * 试卷构建规则
     */
    private Long examPaperBuildId;
    /**
     * 最小分数
     */
    private Integer minScore;
    private String minScoreStr;
    /**
     * 最大分数
     */
    private Integer maxScore;
    private String maxScoreStr;
}
