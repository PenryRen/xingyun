package com.mindskip.wdd.viewmodel.exam.answer;

import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoPaperVM;
import lombok.Data;

/**
 * @version 1.7.0
 * @description: 答卷信息
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class ExamPaperAnswerEditResponseVM {
    /**
     * 试卷信息
     */
    private ExamPaperDoPaperVM paper;
    /**
     * 答卷信息
     */
    private ExamPaperAnswerInfoResponseVM answer;
    /**
     * 提交人用户名
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
     * 部门层级
     */
    private String departmentLevel;


    /**
     * 正确题数
     */
    private Integer questionCorrect;

    /**
     * 题目总数
     */
    private Integer questionCount;

    /**
     * 是否可以提交
     */
    private Boolean canSubmit;

    /**
     * 批改人
     */
    private String judgeUser;
}
