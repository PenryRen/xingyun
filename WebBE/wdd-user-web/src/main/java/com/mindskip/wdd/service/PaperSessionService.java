package com.mindskip.wdd.service;

import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.other.PaperSession;
import com.mindskip.wdd.service.enums.SessionEnum;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoResponseVM;

/**
 * @version 1.7.0
 * @description: 试卷缓存信息
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface PaperSessionService {

    /**
     * 初始化剩余时间和防作弊，存redis中
     *
     * @param examPaperDoResponseVM the exam paper do response vm
     * @param user                  the user
     */
    void initPaperSession(ExamPaperDoResponseVM examPaperDoResponseVM, User user, SessionEnum sessionEnum);


    /**
     * 清理 PaperSession Redis缓存
     *
     * @param examPaperId the exam paper id
     * @param user        the user
     */
    void clearPaperSession(Long examPaperId, User user, SessionEnum sessionEnum);

    /**
     * 作弊 + 1
     *
     * @param examPaperId the exam paper id
     * @param user        the user
     */
    void incrementCheat(Long examPaperId, User user, SessionEnum sessionEnum);

    /**
     * Gets paper session.
     *
     * @param examPaperId the exam paper id
     * @param user        the user
     * @return the paper session
     */
    PaperSession getPaperSession(Long examPaperId, User user, SessionEnum sessionEnum);


    /**
     * 试卷人脸识别成功，存入redis
     *
     * @param examPaperId
     * @param userId
     */
    void paperFace(Long examPaperId, Integer userId);

    /**
     * 获取人脸识别结果
     *
     * @param examPaperId
     * @param userId
     * @return {@link Boolean}
     */
    Boolean getPaperFace(Long examPaperId, Integer userId);
}

