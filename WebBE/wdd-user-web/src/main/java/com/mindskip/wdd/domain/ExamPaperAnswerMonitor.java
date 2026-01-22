package com.mindskip.wdd.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mindskip.wdd.configuration.mybaties.ExamPaperAnswerFrameJsonTypeHandler;
import com.mindskip.wdd.domain.frame.ExamPaperAnswerFrame;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 6.5.0
 * @description: 试卷监考
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/25 10:45
 */
@Getter
@Setter
@TableName(value = "t_exam_paper_answer_monitor", autoResultMap = true)
public class ExamPaperAnswerMonitor {
    private Long id;

    /**
     * 试卷id
     */
    private Long examPaperId;

    /**
     * 试卷名称
     */
    private String paperName;

    /**
     * 试卷类型
     */
    private Integer paperType;

    /**
     * 系统判分
     */
    private Integer systemScore;

    /**
     * 最终得分
     */
    private Integer userScore;

    /**
     * 试卷总分
     */
    private Integer paperScore;

    /**
     * 正确题数
     */
    private Integer questionCorrect;

    /**
     * 题目总数
     */
    private Integer questionCount;

    /**
     * 作弊次数
     */
    private Integer cheatCount;

    /**
     * 耗时
     */
    private Integer doTime;

    /**
     * 创建用户
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 试卷分类id
     */
    private Integer examPaperArchiveId;

    /**
     * 试卷构建id
     */
    private Long examPaperBuildId;

    /**
     * 创建人部门id
     */
    private Integer createDepartmentId;

    /**
     * 答卷详情
     */
    @TableField(typeHandler = ExamPaperAnswerFrameJsonTypeHandler.class)
    private ExamPaperAnswerFrame answerFrameContent;

    /**
     * 子试卷id
     */
    private Long examPaperChildId;
}