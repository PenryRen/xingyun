package com.mindskip.wdd.service;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.ExamPaperAnswerMonitor;
import com.mindskip.wdd.viewmodel.monitor.PaperMonitorPageRequestVM;

/**
 * @version 9.0.0
 * @description: 考试监考
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/26 10:45
 */
public interface ExamPaperMonitorService {
    /**
     * 监控查询
     *
     * @param id the id
     * @return the exam paper answer monitor
     */
    ExamPaperAnswerMonitor monitorSelect(Long id);

    /**
     * 监考分页
     *
     * @param requestVM the request vm
     * @return the page info
     */
    PageInfo<ExamPaperAnswerMonitor> monitorPage(PaperMonitorPageRequestVM requestVM);

}
