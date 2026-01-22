package com.mindskip.wdd.viewmodel.question;

import com.mindskip.wdd.base.BaseFilter;
import lombok.Data;

import java.util.List;

/**
 * 题目随机抽取过滤
 *
 * @version 1.7.0
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class QuestionRandom extends BaseFilter {

    /**
     * 题目分类
     */
    private List<Integer> questionArchiveIdList;
    /**
     * 题型
     */
    private Integer questionType;
    /**
     * 数量
     */
    private Integer limit;
    /**
     * 难度
     */
    private Integer difficult;

    /**
     * Instantiates a new Question random.
     */
    public QuestionRandom() {

    }

    /**
     * Instantiates a new Question random.
     *
     * @param questionArchiveIdList the question archive id list
     * @param questionType          the question type
     * @param difficult             the difficult
     */
    public QuestionRandom(List<Integer> questionArchiveIdList, Integer questionType, Integer difficult) {
        this.questionArchiveIdList = questionArchiveIdList;
        this.questionType = questionType;
        this.difficult = difficult;
    }


    /**
     * Instantiates a new Question random.
     *
     * @param questionArchiveIdList the question archive id list
     * @param questionType          the question type
     * @param limit                 the limit
     * @param difficult             the difficult
     */
    public QuestionRandom(List<Integer> questionArchiveIdList, Integer questionType, Integer limit, Integer difficult) {
        this.questionArchiveIdList = questionArchiveIdList;
        this.questionType = questionType;
        this.limit = limit;
        this.difficult = difficult;
    }

}
