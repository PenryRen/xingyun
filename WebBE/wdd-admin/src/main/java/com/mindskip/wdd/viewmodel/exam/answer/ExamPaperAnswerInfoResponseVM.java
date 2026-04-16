package com.mindskip.wdd.viewmodel.exam.answer;

import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 答卷信息
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ExamPaperAnswerInfoResponseVM {
    private Long id;
    /**
     * 最终得分
     */
    private String userScore;
    /**
     * 试卷分数
     */
    private String paperScore;
    /**
     * 耗时
     */
    private Integer doTime;
    /**
     * 耗时
     */
    private String doTimeStr;
    /**
     * 试卷id
     */
    private Long examPaperId;
    /**
     * 试卷类型
     */
    private Integer paperType;
    /**
     * 创建人
     */
    private Integer createUser;
    /**
     * 批改人
     */
    private Integer judgeUser;
    /**
     * 批改人用户名
     */
    private String judgeUserName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否合格
     */
    private Boolean passed;

    /**
     * 合格分
     */
    private String passScore;

    /**
     * 答卷状态
     */
    private Integer status;

    /**
     * 试卷答题信息
     */
    private List<QuestionAnswerFrame> questionAnswerFrameList;
}
