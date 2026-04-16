package com.mindskip.wdd.base;


import com.mindskip.wdd.context.ApiContext;
import com.mindskip.wdd.domain.User;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @version 1.7.0
 * @description: 控制器基础类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public class BaseApiController {
    /**
     * 默认分页数量
     */
    protected final static String DEFAULT_PAGE_SIZE = "10";

    /**
     * The Api context.
     */
    @Autowired
    protected ApiContext apiContext;

    /**
     * 获取当前用户
     *
     * @return the current user
     */
    protected User getCurrentUser() {
        return apiContext.getCurrentUser();
    }
}
