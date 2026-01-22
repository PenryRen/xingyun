package com.mindskip.wdd.service;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.frame.ExamPaperAnswerFrame;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerEditResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerPageRequestVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperPageRequestVM;

/**
 * @version 9.5.0
 * @description: 模拟练习考试
 * Copyright (C), 2024, 麒技团队
 * @date 2024/09/11 10:45
 */
public interface PracticeService {

    /**
     * 模拟练习分页
     *
     * @param examPaperPageRequestVM
     * @return {@link PageInfo}<{@link PracticeBuild}>
     */
    PageInfo<PracticeBuild> page(ExamPaperPageRequestVM examPaperPageRequestVM);


    /**
     * 模拟练习答卷分页
     *
     * @param requestVM the request vm
     * @return the page info
     */
    PageInfo<PracticeExamPaperAnswer> answerPage(ExamPaperAnswerPageRequestVM requestVM);


    /**
     * 获取模拟练习试卷缓存
     *
     * @param paperId the paper id
     * @return the exam paper cache
     */
    ExamPaperCache getExamPaperCache(Long paperId);


    /**
     * 获取未开始的试卷
     *
     * @param practiceBuildId
     * @param userId
     * @return {@link PracticeExamPaper}
     */
    PracticeExamPaper getWaiteAnswer(Long practiceBuildId, Integer userId);


    /**
     * 生成练习卷
     *
     * @param practiceBuildId
     * @param user
     * @return {@link PracticeExamPaper}
     */
    PracticeExamPaper randomBuild(Long practiceBuildId, User user);


    /**
     * 模拟试卷提交
     *
     * @param examPaperCache
     * @param examPaperAnswerFrame
     * @param user
     * @return {@link RestResponse}
     */
    RestResponse submit(ExamPaperCache examPaperCache, ExamPaperAnswerFrame examPaperAnswerFrame, User user);

    /**
     * 获取模拟练习答卷
     *
     * @param id
     * @return {@link PracticeExamPaperAnswer}
     */
    PracticeExamPaperAnswer getPracticeExamPaperAnswer(Long id);


    /**
     * 模拟练习答卷对象转化
     *
     * @param examPaperCache
     * @param practiceExamPaperAnswer
     * @return {@link ExamPaperAnswerEditResponseVM}
     */
    ExamPaperAnswerEditResponseVM toExamPaperAnswerEditResponseVM(ExamPaperCache examPaperCache, PracticeExamPaperAnswer practiceExamPaperAnswer);


    /**
     * 模拟试卷批改
     *
     * @param examPaperAnswerRequestVM
     * @param user
     * @return {@link Integer}
     */
    Integer judge(ExamPaperAnswerInfoResponseVM examPaperAnswerRequestVM, User user);

}
