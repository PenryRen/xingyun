package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.TrainExamPaper;
import com.mindskip.wdd.viewmodel.train.exam.paper.TrainExamPaperPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训试卷
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/16 10:45
 */
@Mapper
public interface TrainExamPaperMapper extends BaseMapper<TrainExamPaper> {
    /**
     * 培训试卷分页
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<TrainExamPaper> page(TrainExamPaperPageRequestVM requestVM);
}