package com.mindskip.wdd.viewmodel.train.exam.paper;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训试卷分页请求
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/16 10:45
 */
@Data
public class TrainExamPaperPageRequestVM extends BasePage {
    /**
     * 试卷名称
     */
    private String name;

    /**
     * 试卷分类
     */
    private List<Integer> examPaperArchiveIdList;

    /**
     * 选中Id
     */
    private List<Integer> selectIdList;
}
