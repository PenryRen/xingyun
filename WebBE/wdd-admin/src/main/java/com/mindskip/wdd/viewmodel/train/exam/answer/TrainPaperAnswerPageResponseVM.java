package com.mindskip.wdd.viewmodel.train.exam.answer;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 9.0.0
 * @description: 培训答卷分页返回
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/26 10:45
 */
@Data
@ColumnWidth(15)
public class TrainPaperAnswerPageResponseVM implements Serializable {


    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 工号
     */
    private String workNo;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 职位
     */
    private String jobTitle;

    /**
     * 部门
     */
    private String departmentLevel;

    private Integer departmentId;


    private String userScoreStr;

    /**
     * 最终得分
     */
    private Integer userScore;

    private String paperScoreStr;

    /**
     * 试卷分数
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

    private String doTimeStr;

    /**
     * 状态
     */
    private Integer status;

    private String statusStr;

    /**
     * 是否合格
     */
    private Boolean passed;

    private String passStr;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    private String createTimeStr;

    /**
     * 预览地址
     */
    private String previewFilePath;
}
