package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.frame.ExamPaperAnswerFrame;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerResult;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训试卷
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/16 10:45
 */
public interface TrainExamPaperService extends IService<TrainExamPaper> {

    /**
     * 获取培训试卷缓存
     *
     * @param trainPaperId
     * @return {@link ExamPaperCache}
     */
    ExamPaperCache getExamPaperCache(Integer trainPaperId);


    /**
     * 培训答卷提交
     *
     * @param examPaperCache
     * @param examPaperAnswerFrame
     * @param createUser
     * @param trainItemUser
     * @return {@link ExamPaperAnswerResult}
     */
    ExamPaperAnswerResult submit(ExamPaperCache examPaperCache, ExamPaperAnswerFrame examPaperAnswerFrame, User createUser, TrainItemUser trainItemUser);

    /**
     * 获取培训答卷列表
     *
     * @param trainItemUserId
     * @return {@link List}<{@link TrainExamPaperAnswer}>
     */
    List<TrainExamPaperAnswer> getPaperAnswer(Long trainItemUserId);


    /**
     * 参加考试次数
     *
     * @param trainItemUserId
     * @return {@link Integer}
     */
    Integer paperDoCount(Long trainItemUserId);
}
