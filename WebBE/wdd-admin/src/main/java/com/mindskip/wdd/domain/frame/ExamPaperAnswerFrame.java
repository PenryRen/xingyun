package com.mindskip.wdd.domain.frame;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 答卷信息
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ExamPaperAnswerFrame implements Serializable {

    private static final long serialVersionUID = 383070726265294620L;
    private String id;

    /**
     * 试卷id
     */
    private Long paperId;

    /**
     * 子试卷id
     */
    private Long childPaperId;

    /**
     * 耗时
     */
    private Integer doTime;

    /**
     * 分数
     */
    private String score;

    /**
     * 虚拟机UUID
     */
    private String vmGuid;

    /**
     * 用户答案
     */
    private List<QuestionAnswerFrame> questionAnswerFrameList;
}
