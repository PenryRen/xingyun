package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ExamPaperDepartment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 试卷发布部门
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper
public interface ExamPaperDepartmentMapper extends BaseMapper<ExamPaperDepartment> {
    /**
     * 批量插入试卷发布部门
     *
     * @param examPaperId        the exam paper id
     * @param departmentIdList   the department id list
     * @param createUserId       the creation user id
     * @param createDepartmentId the creation department id
     * @return the int
     */
    int insertList(@Param("examPaperId") Long examPaperId, @Param("departmentIdList") List<Integer> departmentIdList, @Param("createUserId") Integer createUserId, @Param("createDepartmentId") Integer createDepartmentId);
}