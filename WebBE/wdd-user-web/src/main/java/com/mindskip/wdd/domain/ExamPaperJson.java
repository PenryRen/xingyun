package com.mindskip.wdd.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mindskip.wdd.configuration.mybaties.ExamPaperFrameJsonTypeHandler;
import com.mindskip.wdd.domain.frame.ExamPaperFrame;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @version 1.7.0
 * @description: 试卷内容Json
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Getter
@Setter
@TableName(value = "t_exam_paper_json", autoResultMap = true)
public class ExamPaperJson {

    public ExamPaperJson() {

    }

    public ExamPaperJson(ExamPaperFrame examPaperFrame) {
        this.id = UUID.randomUUID().toString();
        examPaperFrame.setId(this.id);
        this.content = examPaperFrame;
    }

    private String id;

    /**
     * 试卷内容详情
     */
    @TableField(typeHandler = ExamPaperFrameJsonTypeHandler.class)
    private ExamPaperFrame content;

}
