package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ExamPaperAnswer;
import com.mindskip.wdd.domain.other.KeyValue;
import com.mindskip.wdd.viewmodel.answer.PaperAnswerPageRequestVM;
import com.mindskip.wdd.viewmodel.answer.PaperAnswerUserPageResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 答卷基本信息
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper
public interface ExamPaperAnswerMapper extends BaseMapper<ExamPaperAnswer> {

    /**
     * 获取答卷状态
     *
     * @param paperAnswerId the paper answer id
     * @return the status
     */
    int getStatus(@Param("paperAnswerId") Long paperAnswerId);


    /**
     * 得到答案数量
     *
     * @param examPaperAnswerRequest 试卷答案请求
     * @return {@link Integer}
     */
    Integer getAnswerCount(ExamPaperAnswerRequest examPaperAnswerRequest);


    List<Integer> getAnswerScore(ExamPaperAnswerRequest examPaperAnswerRequest);

    /**
     * 用户答卷成绩
     *
     * @param paperAnswerPageRequestVM the paper answer page request vm
     * @return the list
     */
    List<PaperAnswerUserPageResponseVM> userAnswerPage(PaperAnswerPageRequestVM paperAnswerPageRequestVM);


    /**
     * 答卷统计，按月
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @return the list
     */
    List<KeyValue> selectMothCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime);


    /**
     * 获取提交试卷
     *
     * @param userId      the user id
     * @param examPaperId the exam paper id
     * @return the user answer
     */
    ExamPaperAnswer getUserAnswer(Integer userId, Long examPaperId);


    /**
     * 获取下一张待批改试卷
     *
     * @param paperAnswerPageRequestVM
     * @return {@link Long}
     */
    Long getNextJudgeId(PaperAnswerPageRequestVM paperAnswerPageRequestVM);
}
