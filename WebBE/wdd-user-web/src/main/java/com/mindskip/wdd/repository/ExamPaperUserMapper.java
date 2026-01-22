package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ExamPaperUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @version 4.1.0
 * @description: 试卷用户
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/25 10:45
 */
@Mapper
public interface ExamPaperUserMapper extends BaseMapper<ExamPaperUser> {
    /**
     * 获取补考用户
     *
     * @param examPaperId
     * @param userId
     * @return {@link ExamPaperUser}
     */
    ExamPaperUser getResitExamPaperUser(@Param("examPaperId") Long examPaperId, @Param("userId") Integer userId);
}