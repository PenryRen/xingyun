package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.PracticeExamPaperAnswer;
import com.mindskip.wdd.viewmodel.answer.PaperAnswerPageRequestVM;
import com.mindskip.wdd.viewmodel.answer.PaperAnswerUserPageResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerRequest;
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
     * 练习答案数量
     *
     * @param examPaperAnswerRequest 试卷答案请求
     * @return {@link Integer}
     */
    Integer getAnswerCount(ExamPaperAnswerRequest examPaperAnswerRequest);


    /**
     * 练习用户分页
     *
     * @param paperAnswerPageRequestVM
     * @return {@link List}<{@link PaperAnswerUserPageResponseVM}>
     */
    List<PaperAnswerUserPageResponseVM> page(PaperAnswerPageRequestVM paperAnswerPageRequestVM);

}
