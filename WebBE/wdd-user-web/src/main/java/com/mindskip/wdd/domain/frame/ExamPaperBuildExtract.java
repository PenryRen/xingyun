package com.mindskip.wdd.domain.frame;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 抽题组卷配置
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ExamPaperBuildExtract {
    /**
     * 题目分类
     */
    private Integer questionArchiveId;
    /**
     * 单选题数量
     */
    private Integer singleChoice;
    /**
     * 多选题数量
     */
    private Integer multipleChoice;
    /**
     * 不定项选择题数量
     */
    private Integer uncertainMultipleChoice;
    /**
     * 判断题数量
     */
    private Integer trueFalse;
    /**
     * 填空题数量
     */
    private Integer gapFilling;
    /**
     * 解答题数量
     */
    private Integer shortAnswer;
    /**
     * 难度
     */
    private Integer difficult;
}
