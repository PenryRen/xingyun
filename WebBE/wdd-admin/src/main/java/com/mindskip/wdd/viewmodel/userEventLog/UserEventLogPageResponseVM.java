package com.mindskip.wdd.viewmodel.userEventLog;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 用户动态分页返回
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class UserEventLogPageResponseVM {

    private Long id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 部门
     */
    private String departmentLevel;

}

