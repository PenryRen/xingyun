package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.ExamPaperBuild;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildEditRequestVM;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildPageRequestVM;

/**
 * @version 1.7.0
 * @description: 组卷规则
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface ExamPaperBuildService extends IService<ExamPaperBuild> {

    /**
     * 组卷规则分页
     *
     * @param requestVM
     * @return
     */
    PageInfo<ExamPaperBuild> page(ExamPaperBuildPageRequestVM requestVM);

    /**
     * 插入组卷规则
     *
     * @param examPaperBuildEditRequestVM
     * @param createUser
     */
    void insertExamPaperBuild(ExamPaperBuildEditRequestVM examPaperBuildEditRequestVM, User createUser);

    /**
     * 更新组卷规则
     *
     * @param examPaperBuildEditRequestVM
     * @param userId
     */
    void updateExamPaperBuild(ExamPaperBuildEditRequestVM examPaperBuildEditRequestVM, Integer userId);

    /**
     * 试卷发布
     *
     * @param examPaperBuild
     * @param createUser
     */
    void publishExamPaper(ExamPaperBuild examPaperBuild, User createUser);

    /**
     * 随机组卷发布
     *
     * @param examPaperBuild
     * @param createUser
     */
    void publishRandomExamPaper(ExamPaperBuild examPaperBuild, User createUser);
}
