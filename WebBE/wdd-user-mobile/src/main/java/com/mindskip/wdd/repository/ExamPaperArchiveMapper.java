package com.mindskip.wdd.repository;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ExamPaperArchive;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 试卷分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Mapper
public interface ExamPaperArchiveMapper extends BaseMapper<ExamPaperArchive> {
    /**
     * 查询一级节点分页
     *
     * @return the list
     */
    List<ExamPaperArchive> selectRootTree();


    /**
     * 根据层级获取试卷分类
     *
     * @param level
     * @return {@link List}<{@link ExamPaperArchive}>
     */
    List<ExamPaperArchive> getExamPaperArchiveByLevel(String level);
}