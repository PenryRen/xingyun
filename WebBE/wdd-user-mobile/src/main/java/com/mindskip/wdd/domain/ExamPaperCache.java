package com.mindskip.wdd.domain;


import com.mindskip.wdd.domain.frame.ExamPaperFrame;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 试卷缓存
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Getter
@Setter
public class ExamPaperCache implements Serializable {

    private static final long serialVersionUID = 8796347995026807772L;

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
     * 试卷分数
     */
    private Integer score;

    /**
     * 题目数量
     */
    private Integer questionCount;

    /**
     * 考试时长
     */
    private Integer suggestTime;

    /**
     * 考试开始时间
     */
    private Date limitStartTime;

    /**
     * 考试截止时间
     */
    private Date limitEndTime;

    /**
     * 试卷内容结构
     */
    private String paperFrameId;

    /**
     * 创建者
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 题目选项是否打乱
     */
    private Boolean questionItemMess;

    /**
     * 题目是否打乱
     */
    private Boolean questionMess;

    /**
     * 防作弊
     */
    private Boolean cheat;

    /**
     * 试卷分类
     */
    private Integer examPaperArchiveId;

    /**
     * 考试合格分数
     */
    private Integer passScore;

    /**
     * 组卷规则id
     */
    private Long examPaperBuildId;

    /**
     * 考试合格证书模板
     */
    private Integer credentialTemplateId;

    /**
     * 抓拍
     */
    private Boolean capture;

    /**
     * 创建者部门
     */
    private Integer createDepartmentId;

    /**
     * 考试抓拍
     */
    private Boolean watch;

    /**
     * 最大防作弊次数
     */
    private Integer maxCheatCount;

    /**
     * 人脸识别
     */
    private Boolean faceCheck;

    /**
     * 试卷内容
     */
    private ExamPaperFrame examPaperFrame;
    /**
     * 试卷题目列表
     */
    private List<QuestionFrame> questionFrameList;

    /**
     * 是否缓存
     */
    private Boolean allowCache = true;


    /**
     * 练习构建Id
     */
    private Long practiceBuildId;


    /**
     * 子试卷id,用于随机组卷
     */
    private Long childExamPaperId;
}
