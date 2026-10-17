package com.mindskip.wdd.viewmodel.user;

import lombok.Data;

/**
 * @version 9.5.0
 * @description: 意见反馈分页返回
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/10 10:45
 */
@Data
public class FeedbackPageResponseVM {

    private Long id;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 反馈内容
     */
    private String content;


    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 部门
     */
    private String departmentLevel;

}

