package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.PracticeExamPaperAnswer;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.5.0
 * @description: 模拟练习答卷表
 * Copyright (C), 2025, 麟航团队
 * @date 2025/09/11 10:45
 */
@Mapper
public interface PracticeExamPaperAnswerMapper extends BaseMapper<PracticeExamPaperAnswer> {

    /**
     * 模拟练习答卷分页
     *
     * @param requestVM
     * @return {@link List}<{@link PracticeExamPaperAnswer}>
     */
    List<PracticeExamPaperAnswer> page(ExamPaperAnswerPageRequestVM requestVM);

}
