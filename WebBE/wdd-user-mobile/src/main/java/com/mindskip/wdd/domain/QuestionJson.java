package com.mindskip.wdd.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mindskip.wdd.configuration.mybaties.QuestionFrameJsonTypeHandler;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @version 1.7.0
 * @description: 题目内容
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Getter
@Setter
@TableName(value = "t_question_json", autoResultMap = true)
public class QuestionJson {

    public QuestionJson() {

    }

    public QuestionJson(QuestionFrame questionFrame) {
        this.id = UUID.randomUUID().toString();
        questionFrame.setId(this.id);
        this.content = questionFrame;
    }

    private String id;

    /**
     * 题目内容
     */
    @TableField(typeHandler = QuestionFrameJsonTypeHandler.class)
    private QuestionFrame content;

}
