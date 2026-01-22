package com.mindskip.wdd.context;

import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.UserToken;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @version 1.7.0
 * @description: Web上下文
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Component
public class ApiContext {
    private static final String USER_ATTRIBUTES = "USER_ATTRIBUTES";
    private static final String USER_TOKEN_ATTRIBUTES = "USER_TOKEN_ATTRIBUTES";

    /**
     * 设置用户和token
     *
     * @param user      the user
     * @param userToken the user token
     */
    public void setContext(User user, UserToken userToken) {
        getRequestAttributes().setAttribute(USER_ATTRIBUTES, user, RequestAttributes.SCOPE_REQUEST);
        getRequestAttributes().setAttribute(USER_TOKEN_ATTRIBUTES, userToken, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 获取当前用户
     *
     * @return the current user
     */
    public User getCurrentUser() {
        return (User) getRequestAttributes().getAttribute(USER_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 获取用户token
     *
     * @return the current user token
     */
    public UserToken getCurrentUserToken() {
        return (UserToken) getRequestAttributes().getAttribute(USER_TOKEN_ATTRIBUTES, RequestAttributes.SCOPE_REQUEST);
    }

    private RequestAttributes getRequestAttributes() {
        return RequestContextHolder.currentRequestAttributes();
    }
}
