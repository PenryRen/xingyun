package com.mindskip.wdd.service;


import com.mindskip.wdd.domain.User;

import java.io.IOException;
import java.io.InputStream;

/**
 * @version 1.7.0
 * @description: 题目导入
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public interface QuestionImportService {

    /**
     * excel导入题目
     *
     * @param createUser  the create user
     * @param inputStream the input stream
     * @return the string
     * @throws IOException the io exception
     */
    String fromExcel(User createUser, InputStream inputStream) throws IOException;

    /**
     * word导入题目
     *
     * @param createUser  the create user
     * @param inputStream the input stream
     * @return the string
     * @throws IOException the io exception
     */
    String fromWord(User createUser, InputStream inputStream) throws IOException;
}
