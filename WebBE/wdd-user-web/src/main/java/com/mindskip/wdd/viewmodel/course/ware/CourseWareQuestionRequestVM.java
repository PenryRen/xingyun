package com.mindskip.wdd.viewmodel.course.ware;

import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import lombok.Data;

import java.util.List;

/**
 * @version 8.5.0
 * @description: 课件题目
 * Copyright (C), 2025, 麟航团队
 * @date 2025/10/7 10:45
 */
@Data
public class CourseWareQuestionRequestVM {
    /**
     * 题目id
     */
    private Integer id;

    /**
     * 答题内容
     */
    private QuestionAnswerFrame answer;

}