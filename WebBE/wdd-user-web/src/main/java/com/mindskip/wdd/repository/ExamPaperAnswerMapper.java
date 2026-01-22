package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ExamPaperAnswer;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerPageRequestVM;
import com.mindskip.wdd.viewmodel.exam.answer.SelectByUserVM;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 答卷基本信息
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface ExamPaperAnswerMapper extends BaseMapper<ExamPaperAnswer> {
    /**
     * 获取答卷状态
     *
     * @param paperAnswerId the paper answer id
     * @return the status
     */
    int getStatus(@Param("paperAnswerId") Long paperAnswerId);

    /**
     * 获取答卷
     *
     * @param selectByUserVM the select by user vm
     * @return the exam paper answer
     */
    ExamPaperAnswer selectByPaperUserId(SelectByUserVM selectByUserVM);

    /**
     * 答卷分页查询
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<ExamPaperAnswer> page(ExamPaperAnswerPageRequestVM requestVM);

    List<ExamPaperAnswer> selectExamPaperAnswerList(ExamPaperAnswer examPaperAnswer);
}
