package com.mindskip.wdd.viewmodel.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.7.0
 * @description: 令牌
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class WddToken implements Serializable {
    private static final long serialVersionUID = 8514815269319642593L;
    /**
     * 令牌
     */
    private String token;
    /**
     * 有效期
     */
    private Date endTime;

    public WddToken() {
    }

    public WddToken(String token, Date endTime) {
        this.token = token;
        this.endTime = endTime;
    }
}
