package com.mindskip.wdd.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mindskip.wdd.configuration.mybaties.ExamPaperAnswerFrameJsonTypeHandler;
import com.mindskip.wdd.domain.frame.ExamPaperAnswerFrame;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @version 9.5.0
 * @description: 模拟练习答卷内容
 * Copyright (C), 2024, 麒技团队
 * @date 2024/09/11 10:45
 */
@Getter
@Setter
@TableName(value = "t_practice_exam_paper_answer_json", autoResultMap = true)
public class PracticeExamPaperAnswerJson {

    public PracticeExamPaperAnswerJson() {

    }

    public PracticeExamPaperAnswerJson(ExamPaperAnswerFrame examPaperAnswerFrame) {
        this.id = UUID.randomUUID().toString();
        examPaperAnswerFrame.setId(this.id);
        this.content = examPaperAnswerFrame;
    }

    private String id;

    /**
     * 答卷内容
     */
    @TableField(typeHandler = ExamPaperAnswerFrameJsonTypeHandler.class)
    private ExamPaperAnswerFrame content;
}
