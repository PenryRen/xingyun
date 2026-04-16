package com.mindskip.wdd.service;

import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.ueit.ExamPaperAnswerError;
import com.mindskip.wdd.domain.ueit.TrainItemUserQuestion;
import org.springframework.scheduling.annotation.Async;

/**
 * 异步方法 Service接口
 *
 * @author libl
 * @date 2025-04-10
 */
public interface AsyncService {

    /**
     * 异步核验实训答案
     *
     * @param error 试卷核验对象
     */
    @Async
    public void checkAnswer(ExamPaperAnswerError error);

    /**
     * 异步核验课程培训检查点
     *
     * @param query       用户课件功能点
     * @param currentUser 当前用户
     */
    @Async
    public void checkTrain(TrainItemUserQuestion query, User currentUser);
}
