package com.mindskip.wdd.viewmodel.user;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 登录返回
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class LoginResponseVM {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 令牌
     */
    private String token;
}
