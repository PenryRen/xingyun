package com.mindskip.wdd.domain;

import com.mindskip.wdd.domain.frame.ExamPaperFrame;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训试卷缓存
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/26 10:45
 */
@Data
public class TrainExamPaperCache implements Serializable {

    private static final long serialVersionUID = 8500117351031991482L;

    /**
     * 培训试卷
     */
    private TrainExamPaper trainExamPaper;
    /**
     * 试卷内容
     */
    private ExamPaperFrame examPaperFrame;
    /**
     * 试卷题目列表
     */
    private List<QuestionFrame> questionFrameList;
}
