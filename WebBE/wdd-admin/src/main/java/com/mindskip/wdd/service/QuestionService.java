package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Question;
import com.mindskip.wdd.domain.QuestionJson;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.other.QuestionRandomItem;
import com.mindskip.wdd.viewmodel.question.QuestionEditRequestVM;
import com.mindskip.wdd.viewmodel.question.QuestionLike;
import com.mindskip.wdd.viewmodel.question.QuestionPageRequestVM;
import com.mindskip.wdd.viewmodel.question.QuestionRandom;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 题目
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public interface QuestionService extends IService<Question> {

    /**
     * 分页查询题目
     *
     * @param requestVM the request vm
     * @return the page info
     */
    PageInfo<Question> page(QuestionPageRequestVM requestVM);

    /**
     * 插入题目
     *
     * @param questionEditRequestVM the question edit request vm
     * @param createUser            the create user
     * @return the question
     */
    Question insertQuestion(QuestionEditRequestVM questionEditRequestVM, User createUser);

    /**
     * 更新题目
     *
     * @param questionEditRequestVM the question edit request vm
     * @return the question
     */
    Question updateQuestion(QuestionEditRequestVM questionEditRequestVM);

    /**
     * 组装题目对象
     *
     * @param id the id
     * @return the question edit request vm
     */
    QuestionEditRequestVM selectQuestionEditRequestVM(Long id);

    /**
     * 随机抽取题目
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
     * 查询随机抽题数量
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

    /**
     * 获取题目内容
     *
     * @param id
     * @return {@link QuestionJson}
     */
    QuestionJson getQuestionJsonById(String id);
}
