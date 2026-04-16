package com.mindskip.wdd.viewmodel.answer;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 试卷基本信息
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class PaperInfoVM {

    private Long id;

    /**
     * 试卷分类
     */
    private Integer examPaperArchiveId;

    /**
     * 试卷分类
     */
    private String examPaperArchiveStr;

    /**
     * 试卷名称
     */
    private String name;

    /**
     * 试卷类型
     */
    private Integer buildType;

    /**
     * 试卷类型
     */
    private String buildTypeStr;

    /**
     * 考试合格分
     */
    private String passScore;

    /**
     * 考试总分
     */
    private String score;

    /**
     * 题目总数量
     */
    private Integer questionCount;

    /**
     * 考试时长
     */
    private Integer suggestTime;

    /**
     * 考试时长
     */
    private String suggestTimeStr;

    /**
     * 考试开始时间
     */
    private String limitStartTime;

    /**
     * 考试结束时间
     */
    private String limitEndTime;

    /**
     * 时间发布时间
     */
    private String publishTime;

    /**
     * 创建人
     */
    private String createUserStr;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 批改数量
     */
    private Integer judgeCount;

    /**
     * 完成数量
     */
    private Integer completeCount;

    /**
     * 所有数量
     */
    private Integer allCount;

    /**
     * 是否抓拍
     */
    private Boolean capture;

    /**
     * 通过数
     */
    private Integer passCount;

    /**
     * 通过百分比
     */
    private String passPercent = "0%";

    /**
     * 正确百分比
     */
    private String correctPercent = "0%";

    /**
     * 得分百分比
     */
    private String scorePercent = "0%";


    /**
     * 分数段饼状图
     */
    private List<PieKeyValue> pieList = new ArrayList<>();

    /**
     * 分数段x轴
     */
    private List<String> x = new ArrayList<>();

    /**
     * 分数段y轴
     */
    private List<Object> y = new ArrayList<>();


}

