package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ExamPaperBuild;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 组卷规则
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper
public interface ExamPaperBuildMapper extends BaseMapper<ExamPaperBuild> {

    /**
     * 组卷规则分页
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<ExamPaperBuild> page(ExamPaperBuildPageRequestVM requestVM);
}