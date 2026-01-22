package com.mindskip.wdd.service;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.ExamPaperAnswer;
import com.mindskip.wdd.domain.ExamPaperCache;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.wdd.domain.frame.ExamPaperAnswerFrame;
import com.mindskip.wdd.domain.frame.ExamPaperItemQuestionFrame;
import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import com.mindskip.wdd.domain.other.KeyValue;
import com.mindskip.wdd.viewmodel.answer.PaperAnswerPageRequestVM;
import com.mindskip.wdd.viewmodel.answer.PaperAnswerUserPageResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerEditResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerRequest;

import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 答卷
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface ExamPaperAnswerService {
    /**
     * 根据id获取答卷
     *
     * @param id
     * @return
     */
    ExamPaperAnswer getById(Long id);

    /**
     * 答卷更新
     *
     * @param examPaperAnswer
     * @return int
     */
    int updateExamPaperAnswer(ExamPaperAnswer examPaperAnswer);

    /**
     * 根据答卷id获取答卷状态
     *
     * @param paperAnswerId
     * @return
     */
    ExamPaperAnswerStatusEnum getStatus(Long paperAnswerId);

    /**
     * 答卷对象组装
     *
     * @param examPaperCache
     * @param examPaperAnswer
     * @return
     */
    ExamPaperAnswerEditResponseVM toExamPaperAnswerEditResponseVM(ExamPaperCache examPaperCache, ExamPaperAnswer examPaperAnswer);


    /**
     * 答卷对象组装
     *
     * @param examPaperAnswerInfoResponseVM the exam paper answer info response vm
     * @param examPaperCache                the exam paper cache
     * @param examPaperAnswerFrame          the exam paper answer frame
     * @return the exam paper answer edit response vm
     */
    ExamPaperAnswerEditResponseVM toExamPaperAnswerEditResponseVM(ExamPaperAnswerInfoResponseVM examPaperAnswerInfoResponseVM, ExamPaperCache examPaperCache, ExamPaperAnswerFrame examPaperAnswerFrame);

    /**
     * 试卷批改
     *
     * @param examPaperAnswerRequestVM
     * @return
     */
    Integer judge(ExamPaperAnswerInfoResponseVM examPaperAnswerRequestVM);


    /**
     * 获取下一张批改试卷id
     *
     * @param paperAnswerPageRequestVM 纸回答页面请求vm
     * @return {@link Long}
     */
    Long getNextJudgeId(PaperAnswerPageRequestVM paperAnswerPageRequestVM);

    /**
     * 试卷提交
     *
     * @param examPaperCache       the exam paper cache
     * @param examPaperAnswerFrame the exam paper answer frame
     * @param user                 the user
     */
    void submit(ExamPaperCache examPaperCache, ExamPaperAnswerFrame examPaperAnswerFrame, User user);


    /**
     * 得到答卷数量信息
     *
     * @param examPaperAnswerRequest 试卷答案请求
     * @return {@link Integer}
     */
    Integer getAnswerCount(ExamPaperAnswerRequest examPaperAnswerRequest);


    /**
     * 获取试卷最总得分
     *
     * @param examPaperAnswerRequest
     * @return {@link List}<{@link Integer}>
     */
    List<Integer> getAnswerScore(ExamPaperAnswerRequest examPaperAnswerRequest);

    /**
     * 用户成绩分页
     *
     * @param requestVM
     * @return
     */
    PageInfo<PaperAnswerUserPageResponseVM> userAnswerPage(PaperAnswerPageRequestVM requestVM);


    /**
     * 获取答卷提交月数量
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<KeyValue> selectMothCount(Date startTime, Date endTime);

    /**
     * 获取用户抓拍
     *
     * @param userId
     * @param examPaperId
     * @return
     */
    List<String> examPaperUserCamera(Integer userId, Long examPaperId);


    /**
     * 获取提交试卷
     *
     * @param userId      the user id
     * @param examPaperId the exam paper id
     * @return the user answer
     */
    ExamPaperAnswer getUserAnswer(Integer userId, Long examPaperId);


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
