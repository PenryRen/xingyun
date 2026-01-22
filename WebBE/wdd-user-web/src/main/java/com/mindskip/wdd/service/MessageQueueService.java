package com.mindskip.wdd.service;

import com.mindskip.wdd.domain.ExamPaperCache;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.UserEventLog;
import com.mindskip.wdd.domain.frame.ExamPaperAnswerFrame;
import com.mindskip.wdd.viewmodel.exam.answer.AnswerMQ;

/**
 * @version 1.7.0
 * @description: 消息队列
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface MessageQueueService {

    /**
     * 用户答案入库
     *
     * @param answerMQ the answer mq
     */
    void answerSend(AnswerMQ answerMQ);


    /**
     * 试卷监考
     *
     * @param examPaperCache       the exam paper cache
     * @param examPaperAnswerFrame the exam paper answer frame
     * @param createUser           the create user
     */
    void monitor(ExamPaperCache examPaperCache, ExamPaperAnswerFrame examPaperAnswerFrame, User createUser);

    /**
     * 用户动态入库
     *
     * @param userEventLog the user event log
     */
    void userEventSend(UserEventLog userEventLog);
}
