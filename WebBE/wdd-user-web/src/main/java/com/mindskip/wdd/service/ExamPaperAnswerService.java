package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.ExamPaperAnswer;
import com.mindskip.wdd.domain.ExamPaperCache;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.wdd.domain.frame.ExamPaperAnswerFrame;
import com.mindskip.wdd.domain.frame.ExamPaperItemQuestionFrame;
import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerEditResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerPageRequestVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerResult;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 答卷
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface ExamPaperAnswerService extends IService<ExamPaperAnswer> {

    /**
     * 获取答卷状态
     *
     * @param paperAnswerId the paper answer id
     * @return the status
     */
    ExamPaperAnswerStatusEnum getStatus(Long paperAnswerId);

    /**
     * 试卷提交
     *
     * @param examPaperCache       the exam paper cache
     * @param examPaperAnswerFrame the exam paper answer frame
     * @param createUser           the create user
     * @return the exam paper answer result
     */
    ExamPaperAnswerResult submit(ExamPaperCache examPaperCache, ExamPaperAnswerFrame examPaperAnswerFrame, User createUser);

    /**
     * 答卷记录分页
     *
     * @param requestVM the request vm
     * @return the page info
     */
    PageInfo<ExamPaperAnswer> page(ExamPaperAnswerPageRequestVM requestVM);

    /**
     * 当前用户的全部正式考试答卷记录（不按 paperType 过滤）。
     * paperType 表示组卷方式，并不代表是否为正式考试。
     *
     * @param userId 当前登录用户 ID
     * @return 按业务层用于学习分析的答卷记录
     */
    List<ExamPaperAnswer> getLearningRecords(Integer userId);


    /**
     * 批改试卷页面展示
     *
     * @param examPaperCache  the exam paper cache
     * @param examPaperAnswer the exam paper answer
     * @return exam paper answer edit response vm
     */
    ExamPaperAnswerEditResponseVM toExamPaperAnswerEditResponseVM(ExamPaperCache examPaperCache, ExamPaperAnswer examPaperAnswer);


    /**
     * 判断是否需要批改
     *
     * @param questionFrame the question frame
     * @return the boolean
     */
    Boolean needJudge(QuestionFrame questionFrame);

    /**
     * 判断是否需要核验
     *
     * @param questionFrame the question frame
     * @return the boolean
     */
    Boolean needCheck(QuestionFrame questionFrame);


    /**
     * 题目分数计算
     *
     * @param questionAnswerFrame the question answer frame
     * @param questionFrame       the question frame
     * @return the integer
     */
    Integer questionAnswerJudge(QuestionAnswerFrame questionAnswerFrame, QuestionFrame questionFrame);


    /**
     * xss标签清理
     *
     * @param questionAnswerFrame
     */
    void xssClear(QuestionAnswerFrame questionAnswerFrame);


    /**
     * 还原题目顺序、答案顺序、自定义分数
     *
     * @param examPaperItemQuestionFrame
     * @param questionFrame
     * @param questionAnswerFrame
     * @param questionItemMess
     * @param itemOrder
     */
    void questionFrameAndAnswer(ExamPaperItemQuestionFrame examPaperItemQuestionFrame, QuestionFrame questionFrame, QuestionAnswerFrame questionAnswerFrame, Boolean questionItemMess, Integer itemOrder);


}
