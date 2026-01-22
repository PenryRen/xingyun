package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ExamPaperUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 试卷人员接收
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface ExamPaperUserMapper extends BaseMapper<ExamPaperUser> {

    /**
     * 批量插入试卷发布人员
     *
     * @param examPaperId        the exam paper id
     * @param userIdList         the user id list
     * @param createUserId       the creation user id
     * @param createDepartmentId the creation department id
     * @return the int
     */
    int insertList(@Param("examPaperId") Long examPaperId, @Param("userIdList") List<Integer> userIdList, @Param("createUserId") Integer createUserId, @Param("createDepartmentId") Integer createDepartmentId);


    /**
     * 清理历史补考
     *
     * @param examPaperId
     * @param userId
     * @return int
     */
    int clearPaperResit(@Param("examPaperId") Long examPaperId, @Param("userId") Integer userId);
}