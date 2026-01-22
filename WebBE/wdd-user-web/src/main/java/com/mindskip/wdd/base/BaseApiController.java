package com.mindskip.wdd.base;


import com.mindskip.wdd.context.WebContext;
import com.mindskip.wdd.domain.User;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @version 1.7.0
 * @description: 控制器基础类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public class BaseApiController {
    /**
     * 默认分页数量
     */
    protected final static String DEFAULT_PAGE_SIZE = "10";

    /**
     * The Web context.
     */
    @Autowired
    protected WebContext webContext;

    /**
     * 获取当前用户
     *
     * @return the current user
     */
    protected User getCurrentUser() {
        return webContext.getCurrentUser();
    }
}
