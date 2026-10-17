package com.mindskip.wdd.viewmodel.train;

import lombok.Data;

import java.util.List;


/**
 * @version 9.0.0
 * @description: 培训列表
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Data
public class TrainDetailItem {

    /**
     * 用户培训项id
     */
    private Long trainUserItemId;

    /**
     * 名称
     */
    private String name;

    /**
     * 培训内容id
     */
    private Integer targetId;

    /**
     * 当前分数
     */
    private Integer currentNumber;

    private String currentNumberStr;

    /**
     * 最大分数
     */
    private Integer maxNumber;

    private String maxNumberStr;

    /**
     * 合格分数线
     */
    private Integer passNumber;

    private String passNumberStr;

    /**
     * 文件类型
     */
    private Integer fileType;

    /**
     * 实训环境
     */
    private String vmType;

    /**
     * 排序
     */
    private Integer itemOrder;

    /**
     * 考试次数
     */
    private Integer allowCount;

    /**
     * 题目数
     */
    private Integer questionCount;

    /**
     * 考试时长
     */
    private String suggestTimeStr;

    /**
     * 状态
     */
    private Integer status;

    private String statusStr;

    /**
     * 完成进度百分比
     */
    private Integer percentage;

    /**
     * 证书模板地址
     */
    private String credentialImagePath;

    /**
     * 完成时间
     */
    private String completeTime;

    /**
     * 培训答卷列表
     */
    private List<TrainExamPaperAnswerVM> answerList;
}
