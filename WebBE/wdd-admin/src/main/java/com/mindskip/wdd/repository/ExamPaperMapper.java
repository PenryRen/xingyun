package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ExamPaper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @version 1.7.0
 * @description: 试卷基本信息
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper
public interface ExamPaperMapper extends BaseMapper<ExamPaper> {

    /**
     * 根据试卷规则删除试卷
     *
     * @param examPaperBuildId the exam paper build id
     * @return the int
     */
    int deleteByExamPaperBuildId(Long examPaperBuildId);

    /**
     * 统计参加考试人数
     *
     * @param examPaperId the exam paper id
     * @return the integer
     */
    Integer paperAllUserCount(@Param("examPaperId") Long examPaperId);
}