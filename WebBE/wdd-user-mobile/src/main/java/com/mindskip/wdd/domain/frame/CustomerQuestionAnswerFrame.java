package com.mindskip.wdd.domain.frame;

import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 用户题目答题信息
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class CustomerQuestionAnswerFrame {

    private String id;

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 题目内容id
     */
    private String questionFrameId;

    /**
     * 是否正确
     */
    private Boolean doRight;

    /**
     * 选项key值排序
     */
    private List<Integer> questionItemKeyOrder;

    /**
     * 题目序号
     */
    private Integer itemOrder;

    /**
     * 用户得分
     */
    private Integer customerScore;

    /**
     * 题目最终得分
     */
    private String customerScoreVM;


    /**
     * 题目批改得分
     */
    private String judgeScoreVM;

    /**
     * 是否完成
     */
    private Boolean completed;

}
