package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ExamPaperChild;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version 9.5.0
 * @description: 子试卷，用于随机组卷
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/8 10:45
 */
@Mapper
public interface ExamPaperChildMapper extends BaseMapper<ExamPaperChild> {

    /**
     * 根据examPaperBuildId删除试卷
     *
     * @param examPaperBuildId
     * @return int
     */
    int deleteByExamPaperBuildId(Long examPaperBuildId);

}
