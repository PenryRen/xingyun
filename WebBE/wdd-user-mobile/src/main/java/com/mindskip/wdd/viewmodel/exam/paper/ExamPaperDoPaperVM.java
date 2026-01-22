package com.mindskip.wdd.viewmodel.exam.paper;

import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 试卷基本信息
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class ExamPaperDoPaperVM {
    private Long id;

    /**
     * 试卷id
     */
    private Long paperId;

    /**
     * 试卷名称
     */
    private String name;

    /**
     * 试卷类型
     */
    private Integer paperType;

    /**
     * 分数
     */
    private String score;

    /**
     * 建议时长
     */
    private Integer suggestTime;

    private String suggestTimeStr;

    /**
     * 是否打乱题目
     */
    private Boolean questionMess;

    /**
     * 是否防作弊
     */
    private Boolean cheat;

    private Integer maxCheatCount;

    /**
     * 抓拍
     */
    private Boolean capture;

    /**
     * 实训环境
     */
    private String vmType;

    /**
     * 试卷标题
     */
    private List<ExamPaperDoTitle> examPaperDoTitleList;
}
