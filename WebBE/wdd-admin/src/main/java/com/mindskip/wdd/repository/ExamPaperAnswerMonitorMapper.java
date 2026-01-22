package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ExamPaperAnswerMonitor;
import com.mindskip.wdd.viewmodel.monitor.PaperMonitorPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 6.5.0
 * @description: 试卷监考
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/25 10:45
 */
@Mapper
public interface ExamPaperAnswerMonitorMapper extends BaseMapper<ExamPaperAnswerMonitor> {

    /**
     * 试卷监考分页
     *
     * @param requestVM
     * @return {@link List}<{@link ExamPaperAnswerMonitor}>
     */
    List<ExamPaperAnswerMonitor> page(PaperMonitorPageRequestVM requestVM);
}