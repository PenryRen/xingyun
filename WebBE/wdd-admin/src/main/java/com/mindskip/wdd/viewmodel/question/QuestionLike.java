package com.mindskip.wdd.viewmodel.question;

import com.mindskip.wdd.domain.enums.QuestionTypeEnum;
import lombok.Data;

/**
 * 问题
 *
 * @author mindskip
 * @version 7.5.0
 * @description: 题目查重
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/28 10:45
 */
@Data
public class QuestionLike {
    /**
     * id
     */
    private Long id;
    /**
     * 题目分类
     */
    private Integer questionArchiveId;
    /**
     * 题型
     */
    private Integer questionType;
    /**
     * 题干
     */
    private String title;

    public QuestionLike(Long id, Integer questionArchiveId, Integer questionType, String title) {
        this.id = id;
        this.questionArchiveId = questionArchiveId;
        this.questionType = questionType;
        this.title = title;
    }
}
