package com.mindskip.wdd.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * @version 9.5.0
 * @description: 模拟练习试卷
 * Copyright (C), 2024, 麒技团队
 * @date 2024/09/11 10:45
 */
@Getter
@Setter
@TableName("t_practice_exam_paper")
public class PracticeExamPaper {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 试卷名称
     */
    private String name;

    /**
     * 试卷类型(人工组卷、抽题组卷、随机组卷)
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
     * 练习试卷结构信息表Id
     */
    private String practicePaperFrameId;

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
     * 试卷分类
     */
    private Integer examPaperArchiveId;

    /**
     * 合格分
     */
    private Integer passScore;

    /**
     * 练习构建Id
     */
    private Long practiceBuildId;

    /**
     * 创建人部门
     */
    private Integer createDepartmentId;
}
