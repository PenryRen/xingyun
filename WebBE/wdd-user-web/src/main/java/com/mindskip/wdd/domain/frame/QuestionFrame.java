package com.mindskip.wdd.domain.frame;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 题目内容
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class QuestionFrame implements Serializable {


    private static final long serialVersionUID = 8050000328894761083L;
    private String id;

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 题目分类id
     */
    private Integer questionArchiveId;

    /**
     * 实训环境
     */
    private String vmType;

    /**
     * 题目类型
     */
    private Integer questionType;

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目解析
     */
    private String analyze;


    /**
     * 题目选项、填空题的空
     */
    private List<QuestionItemFrame> questionItemFrames;


    /**
     * 选择题 正确答案 Key
     */
    private Integer correctKey;


    /**
     * 多选   正确答案 Key 数组
     */
    private List<Integer> correctArrayKey;

    /**
     * 选择题 正确答案 prefix 显示 , 多选用空格
     */
    private String correctPrefix;


    /**
     * 判断题 正确答案 content 显示
     */
    private String correctContent;

    /**
     * 简答题正确答题
     */
    private String correct;

    /**
     * 实训题 规则类型
     */
    private String commandType;

    /**
     * 实训题 执行命令
     */
    private String command;

    /**
     * 题目难度
     */
    private Integer difficult;

    /**
     * 题目分数
     */
    private Integer score;

    /**
     * 题目自定义分数
     */
    private String trickScore;

    /**
     * 题目排序
     */
    private Integer itemOrder;

}
