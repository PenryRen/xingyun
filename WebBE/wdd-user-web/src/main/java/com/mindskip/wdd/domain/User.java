package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.7.0
 * @description: 用户
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Getter
@Setter
public class User implements Serializable {

    private static final long serialVersionUID = 8308822592705609078L;
    private Integer id;

    /**
     * 用户uuid
     */
    private String userUuid;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户真实名称
     */
    private String realName;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 生日
     */
    private Date birthDay;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 系统角色
     */
    private Integer systemRole;

    /**
     * 用户状态
     */
    private Integer status;

    /**
     * 头像地址
     */
    private String imagePath;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 最后活动时间
     */
    private Date lastActiveTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 管理员角色
     */
    private Integer roleId;

    /**
     * 部门
     */
    private Integer departmentId;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 工号
     */
    private String workNo;

    /**
     * 职位
     */
    private String jobTitle;

    /**
     * 创建者
     */
    private Integer createUser;

}