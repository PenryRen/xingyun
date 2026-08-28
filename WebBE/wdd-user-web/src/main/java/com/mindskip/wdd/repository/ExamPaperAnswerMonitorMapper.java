package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ExamPaperAnswer;
import com.mindskip.wdd.domain.ExamPaperAnswerMonitor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 6.5.0
 * @description: 试卷监考
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/25 10:45
 */
@Mapper
public interface ExamPaperAnswerMonitorMapper extends BaseMapper<ExamPaperAnswerMonitor> {

    /**
     * 获取答卷监考
     *
     * @param paperId
     * @param userId
     * @return {@link ExamPaperAnswerMonitor}
     */
    ExamPaperAnswerMonitor getMonitor(Long paperId, Integer userId);

    /**
     * 更新答卷监考
     *
     * @param examPaperAnswerMonitor
     * @return int
     */
    int updateMonitor(ExamPaperAnswerMonitor examPaperAnswerMonitor);

    List<ExamPaperAnswerMonitor> selectMonitorList(ExamPaperAnswerMonitor selectExam);
}