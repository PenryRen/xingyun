package com.mindskip.wdd.viewmodel.exam.paper;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 试卷分页返回
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ExamPaperPageResponseVM {
    private Long id;
    /**
     * 试卷名称
     */
    private String name;
    /**
     * 合格分
     */
    private String passScore;
    /**
     * 试卷总分
     */
    private String score;
    /**
     * 考试时长
     */
    private Integer suggestTime;
    private String suggestTimeStr;

    /**
     * 试卷类型
     */
    private Integer buildType;

    /**
     * 试卷类型(人工组卷、抽题组卷、随机组卷、实训组卷)
     */
    private Integer paperType;

    /**
     * 题目总数
     */
    private Integer questionCount;
    /**
     * 发布时间
     */
    private String publishTime;
    /**
     * 考试开始时间
     */
    private String limitStartTime;
    /**
     * 考试结束时间
     */
    private String limitEndTime;
    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 人脸识别
     */
    private Boolean faceCheck;

    /**
     * 模拟练习试卷
     */
    private Long practiceExamPaperId;

    /**
     * 实训环境
     */
    private String vmType;
}
