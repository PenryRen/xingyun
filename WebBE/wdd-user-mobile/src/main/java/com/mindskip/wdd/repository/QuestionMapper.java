package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.Question;
import com.mindskip.wdd.domain.other.QuestionRandomItem;
import com.mindskip.wdd.viewmodel.question.QuestionRandom;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 题目基础信息
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

    /**
     * 随机抽题
     *
     * @param questionRandom the question random
     * @return the list
     */
    List<QuestionRandomItem> questionRandom(QuestionRandom questionRandom);

}
