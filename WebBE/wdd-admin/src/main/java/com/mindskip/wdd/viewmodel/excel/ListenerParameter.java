package com.mindskip.wdd.viewmodel.excel;

import com.mindskip.wdd.configuration.utility.BeanValidator;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.mapping.QuestionMapping;
import com.mindskip.wdd.service.QuestionArchiveService;
import com.mindskip.wdd.service.QuestionService;
import lombok.Data;


/**
 * @version 1.7.0
 * @description: 导入参数
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ListenerParameter {

    /**
     * 创建人
     */
    private User createUser;
    /**
     * 参数验证
     */
    private BeanValidator beanValidator;
    /**
     * 题目服务
     */
    private QuestionService questionService;
    /**
     * 题目分类服务
     */
    private QuestionArchiveService questionArchiveService;
    /**
     * 题目对象转换
     */
    private QuestionMapping questionMapping;

    /**
     * Instantiates a new Listener parameter.
     *
     * @param createUser             the create user
     * @param beanValidator          the bean validator
     * @param questionService        the question service
     * @param questionArchiveService the question archive service
     * @param questionMapping        the question mapping
     */
    public ListenerParameter(User createUser, BeanValidator beanValidator, QuestionService questionService, QuestionArchiveService questionArchiveService, QuestionMapping questionMapping) {
        this.createUser = createUser;
        this.beanValidator = beanValidator;
        this.questionService = questionService;
        this.questionArchiveService = questionArchiveService;
        this.questionMapping = questionMapping;
    }


}
