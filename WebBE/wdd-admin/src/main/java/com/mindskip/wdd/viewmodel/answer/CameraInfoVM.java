package com.mindskip.wdd.viewmodel.answer;

import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 抓拍信息
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class CameraInfoVM {

    private Long id;

    /**
     * 试卷分类
     */
    private Integer examPaperArchiveId;

    /**
     * 试卷分类名称
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
     * 试卷类型名称
     */
    private String buildTypeStr;

    /**
     * 合格分
     */
    private String passScore;

    /**
     * 试卷总分
     */
    private String score;

    /**
     * 题目总数
     */
    private Integer questionCount;

    /**
     * 考试时长
     */
    private Integer suggestTime;

    /**
     * 考试时长格式化
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
     * 考试发布时间
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
     * 答卷人用户名
     */
    private String answerUserName;

    /**
     * 自动批改分数
     */
    private String systemScore;

    /**
     * 最终得分
     */
    private String userScore;

    /**
     * 耗时
     */
    private String doTime;

    /**
     * 试卷状态
     */
    private String statusStr;

    /**
     * 是否通过
     */
    private Boolean passed;

    /**
     * 试卷提交时间
     */
    private String submitTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 作弊次数
     */
    private Integer cheatCount;

    /**
     * 抓拍头像
     */
    private List<String> imageList;


}

