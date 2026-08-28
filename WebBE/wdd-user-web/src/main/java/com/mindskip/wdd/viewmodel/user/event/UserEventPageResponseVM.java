package com.mindskip.wdd.viewmodel.user.event;

import lombok.Data;

/**
 * @version 4.5.0
 * @description: 用户动态返回
 * Copyright (C), 2025, 麟航团队
 * @date 2025/11/10 10:45
 */
@Data
public class UserEventPageResponseVM {
    private Integer id;
    /**
     * 动态内容
     */
    private String content;
    /**
     * 提交时间
     */
    private String createTime;
}
