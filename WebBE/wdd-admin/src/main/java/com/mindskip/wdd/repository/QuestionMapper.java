package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.Question;
import com.mindskip.wdd.domain.other.QuestionRandomItem;
import com.mindskip.wdd.viewmodel.question.QuestionLike;
import com.mindskip.wdd.viewmodel.question.QuestionPageRequestVM;
import com.mindskip.wdd.viewmodel.question.QuestionRandom;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 题目基本信息
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 题目分页
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<Question> page(QuestionPageRequestVM requestVM);

    /**
     * 随机抽题
     *
     * @param questionRandom the question random
     * @return the list
     */
    List<Long> randomQuestion(QuestionRandom questionRandom);

    /**
     * 随机抽题
     *
     * @param questionRandom the question random
     * @return the list
     */
    List<QuestionRandomItem> questionRandom(QuestionRandom questionRandom);

    /**
     * 可供抽题数量统计
     *
     * @param questionRandom the question random
     * @return the long
     */
    Long randomQuestionCount(QuestionRandom questionRandom);

    /**
     * 题干查重
     *
     * @param questionLike
     * @return {@link Long}
     */
    Long checkLikeQuestion(QuestionLike questionLike);
}
