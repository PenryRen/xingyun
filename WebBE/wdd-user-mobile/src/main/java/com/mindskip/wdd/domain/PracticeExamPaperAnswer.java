package com.mindskip.wdd.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 9.5.0
 * @description: 模拟练习答卷表
 * Copyright (C), 2024, 麒技团队
 * @date 2024/09/11 10:45
 */
@Getter
@Setter
@TableName("t_practice_exam_paper_answer")
public class PracticeExamPaperAnswer {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 模拟练习试卷Id
     */
    private Long practiceExamPaperId;

    /**
     * 试卷名称
     */
    private String paperName;

    /**
     * 试卷类型(人工组卷、抽题组卷、随机组卷)
     */
    private Integer paperType;

    /**
     * 系统判分
     */
    private Integer systemScore;

    /**
     * 用户得分
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
     * 耗时
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
     * 答卷内容Id
     */
    private String practiceAnswerFrameId;

    /**
     * 试卷分类Id
     */
    private Integer examPaperArchiveId;

    /**
     * 是否合格
     */
    private Boolean passed;

    /**
     * 合格分
     */
    private Integer passScore;

    /**
     * 模拟练习构建Id
     */
    private Long practiceBuildId;

    /**
     * 创建人部门
     */
    private Integer createDepartmentId;

    /**
     * 是否删除
     */
    private Boolean deleted;
}
