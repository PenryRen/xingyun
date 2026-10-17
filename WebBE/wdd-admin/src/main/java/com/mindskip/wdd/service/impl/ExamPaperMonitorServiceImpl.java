package com.mindskip.wdd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.ExamPaperAnswerMonitor;
import com.mindskip.wdd.repository.ExamPaperAnswerMonitorMapper;
import com.mindskip.wdd.service.ExamPaperMonitorService;
import com.mindskip.wdd.viewmodel.monitor.PaperMonitorPageRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @version 9.0.0
 * @description: 考试监考
 * Copyright (C), 2025, 麟航团队
 * @date 2025/10/26 10:45
 */
@Service
@AllArgsConstructor
public class ExamPaperMonitorServiceImpl implements ExamPaperMonitorService {

    private final ExamPaperAnswerMonitorMapper examPaperAnswerMonitorMapper;


    @Override
    public ExamPaperAnswerMonitor monitorSelect(Long id) {
        return examPaperAnswerMonitorMapper.selectById(id);
    }

    @Override
    public PageInfo<ExamPaperAnswerMonitor> monitorPage(PaperMonitorPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                examPaperAnswerMonitorMapper.page(requestVM)
        );
    }


}
