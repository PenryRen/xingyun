package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.PracticeBuild;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.5.0
 * @description: 模拟练习考试规则构建
 * Copyright (C), 2025, 麟航团队
 * @date 2025/09/11 10:45
 */
@Mapper
public interface PracticeBuildMapper extends BaseMapper<PracticeBuild> {

    /**
     * 模拟考试规则分页
     *
     * @param requestVM
     * @return {@link List}<{@link PracticeBuild}>
     */
    List<PracticeBuild> page(ExamPaperPageRequestVM requestVM);
}
