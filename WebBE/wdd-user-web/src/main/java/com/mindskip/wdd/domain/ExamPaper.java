package com.mindskip.wdd.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.7.0
 * @description: 试卷
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Getter
@Setter
@TableName("t_exam_paper")
public class ExamPaper implements Serializable {
    private static final long serialVersionUID = 3496372158584781496L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 试卷名称
     */
    private String name;

    /**
     * 试卷类型(人工组卷、抽题组卷、随机组卷、实训组卷)
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
     * 考试结束时间
     */
    private Date limitEndTime;

    /**
     * 试卷结构信息表Id
     */
    private String paperFrameId;

    /**
     * 创建人
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
     * 选项打乱
     */
    private Boolean questionItemMess;

    /**
     * 题目打乱
     */
    private Boolean questionMess;

    /**
     * 是否防作弊
     */
    private Boolean cheat;

    /**
     * 试卷分类
     */
    private Integer examPaperArchiveId;

    /**
     * 合格分
     */
    private Integer passScore;

    /**
     * 试卷构建Id
     */
    private Long examPaperBuildId;

    /**
     * 证书模板
     */
    private Integer credentialTemplateId;

    /**
     * 考试抓拍
     */
    private Boolean capture;

    /**
     * 创建人部门
     */
    private Integer createDepartmentId;

    /**
     * 是否允许查看答卷
     */
    private Boolean watch;

    /**
     * 最大作弊次数
     */
    private Integer maxCheatCount;

    /**
     * 人脸识别
     */
    private Boolean faceCheck;

    /**
     * 实训环境
     */
    private String vmType;
}
