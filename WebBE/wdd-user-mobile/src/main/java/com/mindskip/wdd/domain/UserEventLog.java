package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 用户日志
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Getter
@Setter
public class UserEventLog {


    public UserEventLog() {

    }

    public UserEventLog(String content, User user) {
        this(user.getId(), user.getUserName(), content, new Date(), user.getDepartmentId());
    }

    public UserEventLog(User user, String content) {
        this.userId = user.getId();
        this.departmentId = user.getDepartmentId();
        this.userName = user.getUserName();
        this.createTime = new Date();
        this.content = content;
    }

    public UserEventLog(Integer userId, String userName, String content, Date createTime, Integer departmentId) {
        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.createTime = createTime;
        this.departmentId = departmentId;
    }

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
     * 日志内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 部门id
     */
    private Integer departmentId;

}