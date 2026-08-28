package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.ExamPaperCache;
import com.mindskip.wdd.domain.TrainExamPaper;
import com.mindskip.wdd.domain.TrainExamPaperAnswer;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.frame.ExamPaperFrame;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerEditResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.train.exam.answer.NextAnswerRequestVM;
import com.mindskip.wdd.viewmodel.train.exam.answer.TrainPaperAnswerPageRequestVM;
import com.mindskip.wdd.viewmodel.train.exam.paper.TrainExamPaperEditVM;
import com.mindskip.wdd.viewmodel.train.exam.paper.TrainExamPaperPageRequestVM;

/**
 * @version 9.0.0
 * @description: 培训试卷
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
public interface TrainExamPaperService extends IService<TrainExamPaper> {

    /**
     * 培训试卷分页
     *
     * @param requestVM
     * @return
     */
    PageInfo<TrainExamPaper> page(TrainExamPaperPageRequestVM requestVM);


    /**
     * 获取试卷内容
     *
     * @param id
     * @return {@link ExamPaperFrame}
     */
    ExamPaperFrame getExamPaperFrame(String id);


    /**
     * 插入试卷
     *
     * @param createUser           the creation user
     * @param trainExamPaperEditVM
     */
    void insertTrainExamPaper(TrainExamPaperEditVM trainExamPaperEditVM, User createUser);

    /**
     * 更新试卷
     *
     * @param createUser           the creation user
     * @param trainExamPaperEditVM
     */
    void updateTrainExamPaper(TrainExamPaperEditVM trainExamPaperEditVM, User createUser);

    /**
     * 获取培训试卷数量
     *
     * @param trainId
     * @param status
     * @return int
     */
    int trainExamPaperCount(Integer trainId, Integer status);

    /**
     * 培训答卷分页
     *
     * @param requestVM
     * @return {@link PageInfo}<{@link TrainExamPaperAnswer}>
     */
    PageInfo<TrainExamPaperAnswer> answerPage(TrainPaperAnswerPageRequestVM requestVM);


    /**
     * 获取培训答卷
     *
     * @param id
     * @return {@link TrainExamPaperAnswer}
     */
    TrainExamPaperAnswer trainExamPaperAnswerById(Long id);

    /**
     * 获取培训试卷缓存
     *
     * @param trainPaperId
     * @return {@link ExamPaperCache}
     */
    ExamPaperCache getExamPaperCache(Integer trainPaperId);

    /**
     * 培训答卷转化为前端模型
     *
     * @param examPaperCache
     * @param trainExamPaperAnswer
     * @return {@link ExamPaperAnswerEditResponseVM}
     */
    ExamPaperAnswerEditResponseVM toExamPaperAnswerEditResponseVM(ExamPaperCache examPaperCache, TrainExamPaperAnswer trainExamPaperAnswer);

    /**
     * 批改培训试卷
     *
     * @param examPaperAnswerRequestVM
     * @return {@link Integer}
     */
    Integer judge(ExamPaperAnswerInfoResponseVM examPaperAnswerRequestVM);

    /**
     * 获取下一张批改试卷
     *
     * @param nextAnswerRequestVM
     * @return {@link Long}
     */
    Long nextJudgeAnswerId(NextAnswerRequestVM nextAnswerRequestVM);
}
