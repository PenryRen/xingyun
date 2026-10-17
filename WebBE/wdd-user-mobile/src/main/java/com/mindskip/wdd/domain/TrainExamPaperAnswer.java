package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 9.0.0
 * @description: 培训答卷
 * Copyright (C), 2025, 麟航团队
 * @date 2025/10/26 10:45
 */
@Getter
@Setter
public class TrainExamPaperAnswer {
    private Long id;

    /**
     * 培训试卷Id
     */
    private Integer trainExamPaperId;

    /**
     * 培训子项用户Id
     */
    private Long trainItemUserId;

    /**
     * 试卷名称
     */
    private String paperName;

    /**
     * 客观题批改得分
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
     * 正确题目数
     */
    private Integer questionCorrect;

    /**
     * 题目总数
     */
    private Integer questionCount;

    /**
     * 耗时
     */
    private Integer doTime;

    /**
     * 试卷状态
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
     * 答卷内容
     */
    private String answerFrameId;

    /**
     * 试卷分类Id
     */
    private Integer examPaperArchiveId;

    /**
     * 是否合格
     */
    private Boolean passed;

    /**
     * 合格分数线
     */
    private Integer passScore;

    /**
     * 创建人部门
     */
    private Integer createDepartmentId;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 运行查看答卷
     */
    private Boolean watch;

    /**
     * 答卷预览文件
     */
    private String previewFilePath;

    /**
     * 培训子项Id
     */
    private Long trainItemId;

    /**
     * 培训Id
     */
    private Integer trainId;

}