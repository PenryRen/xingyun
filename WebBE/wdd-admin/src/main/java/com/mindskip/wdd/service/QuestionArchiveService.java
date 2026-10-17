package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.QuestionArchive;
import com.mindskip.wdd.viewmodel.question.QuestionArchiveMoveRequestVM;

import java.util.List;


/**
 * @version 1.7.0
 * @description: 题目分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public interface QuestionArchiveService extends IService<QuestionArchive> {

    /**
     * 获取根节点题目分类
     *
     * @return the root question archive
     */
    List<QuestionArchive> getRootQuestionArchive();

    /**
     * 根据父节点id获取题目分类
     *
     * @param id the id
     * @return the question archive by parent id
     */
    List<QuestionArchive> getQuestionArchiveByParentId(Integer id);

    /**
     * 更新题目分类层级
     *
     * @param originalLevel the original level
     * @param targetLevel   the target level
     * @return the int
     */
    int updateLevel(String originalLevel, String targetLevel);

    /**
     * 根据层级获取题目分类
     *
     * @param level the level
     * @return the by level
     */
    QuestionArchive getByLevel(String level);

    /**
     * 删除题目分类，根据层级
     *
     * @param level the level
     * @return the int
     */
    int deleteByLevel(String level);

    /**
     * 题目分类位置移动
     *
     * @param questionArchiveMoveRequestVM
     * @return {@link RestResponse}
     */
    RestResponse move(QuestionArchiveMoveRequestVM questionArchiveMoveRequestVM);
}
