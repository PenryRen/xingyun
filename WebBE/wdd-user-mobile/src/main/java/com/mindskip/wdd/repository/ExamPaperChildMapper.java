package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ExamPaperChild;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version 9.5.0
 * @description: 子试卷，用于随机组卷
 * Copyright (C), 2025, 麟航团队
 * @date 2025/10/8 10:45
 */
@Mapper
public interface ExamPaperChildMapper extends BaseMapper<ExamPaperChild> {

    /**
     * 获取随机组卷的子试卷
     *
     * @param parentExamPaperId
     * @param userId
     * @return {@link ExamPaperChild}
     */
    ExamPaperChild selectChildExamPaper(Long parentExamPaperId, Integer userId);

}
