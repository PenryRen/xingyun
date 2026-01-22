package com.mindskip.wdd.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.7.0
 * @description: 用户token
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class UserToken implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;

    /**
     * 登录token
     */
    private String token;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 创建者部门
     */
    private Integer createDepartmentId;

}