package com.mindskip.wdd.viewmodel.user.event;

import lombok.Data;

/**
 * @version 4.2.0
 * @description: 用户动态返回
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
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
