package com.mindskip.wdd.service;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.ExamPaperCache;
import com.mindskip.wdd.domain.PracticeBuild;
import com.mindskip.wdd.domain.PracticeExamPaperAnswer;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.viewmodel.answer.PaperAnswerPageRequestVM;
import com.mindskip.wdd.viewmodel.answer.PaperAnswerUserPageResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerEditResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerRequest;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildEditRequestVM;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildPageRequestVM;

/**
 * @version 9.5.0
 * @description: 模拟练习考试
 * Copyright (C), 2024, 麒技团队
 * @date 2024/09/11 10:45
 */
public interface PracticeService {

    /**
     * 组卷规则分页
     *
     * @param requestVM
     * @return
     */
    PageInfo<PracticeBuild> page(ExamPaperBuildPageRequestVM requestVM);


    /**
     * 根据id获取模拟练习
     *
     * @param id
     * @return {@link PracticeBuild}
     */
    PracticeBuild getById(Long id);

    /**
     * 插入模拟练习
     *
     * @param requestVM
     * @param user
     */
    void insertBuild(ExamPaperBuildEditRequestVM requestVM, User user);


    /**
     * 更新模拟练习
     *
     * @param requestVM
     * @param user
     */
    void updateBuild(ExamPaperBuildEditRequestVM requestVM, User user);


    /**
     * 删除模拟练习
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 得到答卷数量信息
     *
     * @param examPaperAnswerRequest 试卷答案请求
     * @return {@link Integer}
     */
    Integer getAnswerCount(ExamPaperAnswerRequest examPaperAnswerRequest);


    /**
     * 参加练习人员答卷
     *
     * @param requestVM
     * @return {@link PageInfo}<{@link PaperAnswerUserPageResponseVM}>
     */
    PageInfo<PaperAnswerUserPageResponseVM> answerPage(PaperAnswerPageRequestVM requestVM);


    /**
     * 获取模拟练习试卷缓存
     *
     * @param paperId the paper id
     * @return the exam paper cache
     */
    ExamPaperCache getExamPaperCache(Long paperId);


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
