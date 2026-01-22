package com.mindskip.wdd.viewmodel.course.ware;

import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import lombok.Data;

/**
 * @version 8.5.0
 * @description: 课件题目
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/7 10:45
 */
@Data
public class CourseWareQuestionVM {
    /**
     * 题目弹出时间，秒
     */
    private Integer anchorSecond;

    /**
     * 题目内容
     */
    private QuestionFrame questionFrame;

    /**
     * 答题内容
     */
    private QuestionAnswerFrame questionAnswerFrame;
}