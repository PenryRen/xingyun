package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.QuestionArchive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 题目分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper
public interface QuestionArchiveMapper extends BaseMapper<QuestionArchive> {

    /**
     * 获取根节点分类
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
     * 更新层级
     *
     * @param regexLevel  the regex level
     * @param targetLevel the target level
     * @return the int
     */
    int updateLevel(@Param("regexLevel") String regexLevel, @Param("targetLevel") String targetLevel);

    /**
     * 根据层级获取题目分类
     *
     * @param level the level
     * @return the by level
     */
    QuestionArchive getByLevel(String level);

    /**
     * 层级层级删除题目分类
     *
     * @param level the level
     * @return the int
     */
    int deleteByLevel(String level);
}
