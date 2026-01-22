package com.mindskip.wdd.base;

/**
 * @version 1.7.0
 * @description: 接口返回编码
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public enum SystemCode {
    /**
     * OK
     */
    OK(1, "成功"),
    /**
     * AccessTokenError
     */
    AccessTokenError(400, "用户登录令牌失效"),
    /**
     * UNAUTHORIZED
     */
    UNAUTHORIZED(401, "用户未登录"),
    /**
     * UNAUTHORIZED
     */
    AuthError(402, "用户名或密码错误"),
    /**
     * AuthExpiration
     */
    AuthExpiration(403, "系统授权过期，请联系供应商"),
    /**
     * InnerError
     */
    InnerError(500, "系统内部错误"),
    /**
     * ParameterValidError
     */
    ParameterValidError(501, "参数验证错误"),

    /**
     * AccessDenied
     */
    AccessDenied(502, "该角色没有权限"),

    /**
     * UploadLimit
     */
    UploadLimit(503, "文件大小超过最大限制");

    /**
     * The Code.
     */
    int code;
    /**
     * The Message.
     */
    String message;

    SystemCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
