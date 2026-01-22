package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.frame.ExamPaperFrame;

/**
 * @version 1.7.0
 * @description: 试卷
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface ExamPaperService extends IService<ExamPaper> {

    /**
     * 插入试卷
     *
     * @param examPaperBuild
     * @param examPaperFrame
     * @param createUser
     * @return {@link ExamPaper}
     */
    ExamPaper insertExamPaper(ExamPaperBuild examPaperBuild, ExamPaperFrame examPaperFrame, User createUser);


    /**
     * 插入子试卷
     *
     * @param examPaperChild
     * @param examPaperFrame
     */
    void insertExamPaperChild(ExamPaperChild examPaperChild, ExamPaperFrame examPaperFrame);

    /**
     * 删除试卷
     *
     * @param examPaperBuildId
     */
    void deleteByExamPaperBuildId(Long examPaperBuildId);


    /**
     * 删除子试卷
     *
     * @param examPaperBuildId
     */
    void deleteChildByExamPaperBuildId(Long examPaperBuildId);


    /**
     * 获取试卷缓存
     *
     * @param paperId
     * @param childPaperId
     * @return {@link ExamPaperCache}
     */
    ExamPaperCache getExamPaperCache(Long paperId, Long childPaperId);

    /**
     * 获取试卷发布总人数
     *
     * @param examPaperId
     * @return
     */
    Integer paperAllUserCount(Long examPaperId);


    /**
     * 清理历史补考
     *
     * @param examPaperId
     * @param userId
     * @return
     */
    void clearPaperResit(Long examPaperId, Integer userId);

    /**
     * 插入补考信息
     *
     * @param examPaperUser
     * @return
     */
    int insertExamPaperUser(ExamPaperUser examPaperUser);

}
