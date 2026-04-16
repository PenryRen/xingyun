package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.7.0
 * @description: 答卷
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Getter
@Setter
public class ExamPaperAnswer implements Serializable {
    private static final long serialVersionUID = 6529624229549833230L;
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
     * 自动批改得分
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
     * 题目正确数
     */
    private Integer questionCorrect;

    /**
     * 总题数
     */
    private Integer questionCount;

    /**
     * 试卷耗时
     */
    private Integer doTime;

    /**
     * 答卷状态
     */
    private Integer status;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 批改人
     */
    private Integer judgeUser;

    /**
     * 答卷内容id
     */
    private String answerFrameId;

    /**
     * 试卷分类
     */
    private Integer examPaperArchiveId;

    /**
     * 考试是否合格
     */
    private Boolean passed;

    /**
     * 考试合格分
     */
    private Integer passScore;

    /**
     * 组卷规则id
     */
    private Long examPaperBuildId;

    /**
     * 证书模板
     */
    private Integer credentialTemplateId;

    /**
     * 考试开始时间
     */
    private Date limitStartTime;

    /**
     * 考试结束时间
     */
    private Date limitEndTime;

    /**
     * 创建者部门
     */
    private Integer createDepartmentId;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 是否允许看答卷
     */
    private Boolean watch;

    /**
     * 答卷导出
     */
    private String previewFilePath;

    /**
     * 子试卷id
     */
    private Long examPaperChildId;
}