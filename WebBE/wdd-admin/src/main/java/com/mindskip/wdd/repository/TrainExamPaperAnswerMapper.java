package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.TrainExamPaperAnswer;
import com.mindskip.wdd.viewmodel.train.exam.answer.NextAnswerRequestVM;
import com.mindskip.wdd.viewmodel.train.exam.answer.TrainPaperAnswerPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训答卷
 * Copyright (C), 2025, 麟航团队
 * @date 2025/10/26 10:45
 */
@Mapper
public interface TrainExamPaperAnswerMapper extends BaseMapper<TrainExamPaperAnswer> {

    /**
     * 获取培训答卷数量
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
     * @return {@link List}<{@link TrainExamPaperAnswer}>
     */
    List<TrainExamPaperAnswer> page(TrainPaperAnswerPageRequestVM requestVM);

    /**
     * 获取培训试卷做的次数
     *
     * @param trainItemUserId
     * @return int
     */
    int paperDoCount(Long trainItemUserId);

    /**
     * 获取下一张待批改数
     *
     * @param nextAnswerRequestVM
     * @return {@link Long}
     */
    Long nextJudgeAnswerId(NextAnswerRequestVM nextAnswerRequestVM);
}