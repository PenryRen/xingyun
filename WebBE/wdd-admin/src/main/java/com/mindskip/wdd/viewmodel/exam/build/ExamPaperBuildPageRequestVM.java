package com.mindskip.wdd.viewmodel.exam.build;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 组卷规则分页过滤
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ExamPaperBuildPageRequestVM extends BasePage {
    /**
     * 试卷名称
     */
    private String name;
    /**
     * 试卷分类
     */
    private List<Integer> examPaperArchiveIdList;
    /**
     * 试卷状态
     */
    private Integer status;
}
