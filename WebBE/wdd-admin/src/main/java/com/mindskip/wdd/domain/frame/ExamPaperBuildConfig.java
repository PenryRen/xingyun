package com.mindskip.wdd.domain.frame;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 组卷规则
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class ExamPaperBuildConfig implements Serializable {

    private static final long serialVersionUID = -1645306905012150484L;
    /**
     * 是否题目打乱
     */
    private Boolean questionItemMess;
    /**
     * 选项打乱
     */
    private Boolean questionMess;
    /**
     * 防作弊
     */
    private Boolean cheat;

    /**
     * 最大防作弊次数
     */
    private Integer maxCheatCount;

    /**
     * 抓拍
     */
    private Boolean capture;

    /**
     * 人脸识别
     */
    private Boolean faceCheck;

    /**
     * 允许查看试卷
     */
    private Boolean watch;

    /**
     * 发布部门
     */
    private List<Integer> departmentIdList;
    /**
     * 发布员工
     */
    private List<ExamPaperUserSelect> examPaperUserSelectList;
    /**
     * 发布报名
     */
    private ExamPaperApplySelect examPaperApplySelect;

    /**
     * 试卷内题目信息
     */
    private List<ExamPaperBuildTitle> examPaperBuildTitleList;

    /**
     * 抽题组卷
     */
    private ExamPaperBuildExtract extract;


    /**
     * 题目数
     */
    private Integer questionCount;
    /**
     * 随机组卷
     */
    private List<ExamPaperBuildRandom> examPaperBuildRandomList;
}
