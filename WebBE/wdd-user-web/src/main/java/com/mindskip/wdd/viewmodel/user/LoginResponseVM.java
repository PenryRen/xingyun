package com.mindskip.wdd.viewmodel.user;

import lombok.Data;

import java.util.Date;

/**
 * @version 9.0.0
 * @description: 登录返回
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/16 10:45
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
    /**
     * 有效期
     */
    private Date endTime;
}
