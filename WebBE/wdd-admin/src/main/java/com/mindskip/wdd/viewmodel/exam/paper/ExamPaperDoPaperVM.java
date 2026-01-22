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
     * 试卷分数
     */
    private String score;

    /**
     * 考试时长
     */
    private Integer suggestTime;

    /**
     * 考试时长
     */
    private String suggestTimeStr;

    /**
     * 题目是否打乱
     */
    private Boolean questionMess;

    /**
     * 是否防作弊
     */
    private Boolean cheat;

    /**
     * 最大防作弊次数
     */
    private Integer maxCheatCount;

    /**
     * 是否抓拍
     */
    private Boolean capture;

    /**
     * 人脸识别
     */
    private Boolean faceCheck;

    /**
     * 试卷标题
     */
    private List<ExamPaperDoTitle> examPaperDoTitleList;
}
