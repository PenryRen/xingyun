package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ExamPaper;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperPageRequestVM;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 试卷基本信息
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface ExamPaperMapper extends BaseMapper<ExamPaper> {

    /**
     * 试卷分页
     *
     * @param examPaperPageRequestVM the exam paper page request vm
     * @return the list
     */
    List<ExamPaper> page(ExamPaperPageRequestVM examPaperPageRequestVM);


    /**
     * 补考试卷分页
     *
     * @param examPaperPageRequestVM
     * @return
     */
    List<ExamPaper> resitPage(ExamPaperPageRequestVM examPaperPageRequestVM);

    /**
     * 查询试卷id
     *
     * @param paperId      the paper id
     * @param userId       the user id
     * @param departmentId the department id
     * @return the long
     */
    Long selectExamPaperById(@Param("paperId") Long paperId, @Param("userId") Integer userId, @Param("departmentId") Integer departmentId);

}
