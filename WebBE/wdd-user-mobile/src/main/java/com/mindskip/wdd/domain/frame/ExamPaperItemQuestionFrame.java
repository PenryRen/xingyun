package com.mindskip.wdd.domain.frame;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.7.0
 * @description: 试卷内的题目
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ExamPaperItemQuestionFrame implements Serializable {

    private static final long serialVersionUID = 6009321643380279220L;
    /**
     * 题目id
     */
    private Long id;

    /**
     * 题目内容id
     */
    private String questionFrameId;

    /**
     * 自定义题目分数
     */
    private String trickScore;

    /**
     * 题目序号
     */
    private Integer itemOrder;

}
