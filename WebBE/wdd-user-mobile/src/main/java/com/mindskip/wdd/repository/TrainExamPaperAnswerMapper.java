package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.TrainExamPaperAnswer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训答卷
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/16 10:45
 */
@Mapper
public interface TrainExamPaperAnswerMapper extends BaseMapper<TrainExamPaperAnswer> {
    /**
     * 获取试卷做的次数
     *
     * @param trainItemUserId
     * @return int
     */
    int paperDoCount(Long trainItemUserId);

    /**
     * 获取培训答卷
     *
     * @param trainItemUserId
     * @return {@link List}<{@link TrainExamPaperAnswer}>
     */
    List<TrainExamPaperAnswer> getPaperAnswer(Long trainItemUserId);
}