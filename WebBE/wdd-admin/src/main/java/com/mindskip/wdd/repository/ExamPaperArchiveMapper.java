package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ExamPaperArchive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 试卷分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper
public interface ExamPaperArchiveMapper extends BaseMapper<ExamPaperArchive> {


    /**
     * 获取根节点试卷分类
     *
     * @return the root exam paper archive
     */
    List<ExamPaperArchive> getRootExamPaperArchive();

    /**
     * 根据父节点获取试卷分类
     *
     * @param id the id
     * @return the exam paper archive by parent id
     */
    List<ExamPaperArchive> getExamPaperArchiveByParentId(Integer id);

    /**
     * 更新层级
     *
     * @param regexLevel  the regex level
     * @param targetLevel the target level
     * @return the int
     */
    int updateLevel(@Param("regexLevel") String regexLevel, @Param("targetLevel") String targetLevel);

    /**
     * 获取分类，根据层级
     *
     * @param level the level
     * @return the by level
     */
    ExamPaperArchive getByLevel(String level);

    /**
     * 根据层级删除分类
     *
     * @param level the level
     * @return the int
     */
    int deleteByLevel(String level);
}