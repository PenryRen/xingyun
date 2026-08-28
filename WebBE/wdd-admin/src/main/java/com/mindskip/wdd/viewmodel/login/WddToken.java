package com.mindskip.wdd.viewmodel.login;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.7.0
 * @description: 令牌
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class WddToken implements Serializable {
    private static final long serialVersionUID = 8526278457023445586L;
    /**
     * 令牌
     */
    private String token;
    /**
     * 令牌有效期
     */
    private Date endTime;

    /**
     * Instantiates a new Wdd token.
     */
    public WddToken() {
    }

    /**
     * Instantiates a new Wdd token.
     *
     * @param token   the token
     * @param endTime the end time
     */
    public WddToken(String token, Date endTime) {
        this.token = token;
        this.endTime = endTime;
    }
}
