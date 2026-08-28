package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 9.0.0
 * @description: 培训试卷
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Getter
@Setter
public class TrainExamPaper {
    private Integer id;

    /**
     * 试卷分类Id
     */
    private Integer examPaperArchiveId;

    /**
     * 试卷名称
     */
    private String name;

    /**
     * 试卷总分
     */
    private Integer score;

    /**
     * 题目总数
     */
    private Integer questionCount;

    /**
     * 考试时长
     */
    private Integer suggestTime;

    /**
     * 试卷内容
     */
    private String paperFrameId;

    /**
     * 选项打乱
     */
    private Boolean questionItemMess;

    /**
     * 题目打乱
     */
    private Boolean questionMess;

    /**
     * 防作弊
     */
    private Boolean cheat;

    /**
     * 作弊次数
     */
    private Integer maxCheatCount;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建部门
     */
    private Integer createDepartmentId;

    /**
     * 创建时长
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

}