package com.mindskip.wdd.domain.frame;

import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 题目答案
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class QuestionAnswerFrame {

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
     * 题目类型
     */
    private Integer questionType;

    /**
     * 题目是否正确
     */
    private Boolean doRight;

    /**
     * 实训题是否核验
     */
    private Boolean doCheck;

    /**
     * 单选、多选、判断 用户选择的key
     */
    private Integer contentKey;


    /**
     * 单选、多选、判断  用户选择的key数组
     */
    private List<Integer> contentArrayKey;


    /**
     * 用户答题内容，填空题
     */
    private List<String> contentArray;


    /**
     * 用户答题内容，解答题
     */
    private String content;

    /**
     * 题目的正确答案(题目选项会扰乱，保存一下)
     */
    private String correctPrefix;

    /**
     * 判断题 正确答案 content 显示
     */
    private String correctContent;

    /**
     * 选择题中的选项key值排序
     */
    private List<Integer> questionItemKeyOrder;

    /**
     * 题目排序
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
     * 题目分数
     */
    private Integer questionScore;

    /**
     * 题目分数
     */
    private String questionScoreVM;

    /**
     * 是否完成
     */
    private Boolean completed;

}
