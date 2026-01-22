package com.mindskip.wdd.viewmodel.exam.answer;

import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 答卷基本信息
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class ExamPaperAnswerInfoResponseVM {
    private Long id;
    /**
     * 最终得分
     */
    private String userScore;
    /**
     * 试卷总分
     */
    private String paperScore;
    /**
     * 耗时
     */
    private Integer doTime;
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
     * 答题列表
     */
    private List<QuestionAnswerFrame> questionAnswerFrameList;
}
