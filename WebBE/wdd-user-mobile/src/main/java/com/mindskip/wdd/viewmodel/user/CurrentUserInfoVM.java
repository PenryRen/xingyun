package com.mindskip.wdd.viewmodel.user;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 当前用户信息
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class CurrentUserInfoVM {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
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
     * 出生日期
     */
    private String birthDay;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 图片地址
     */
    private String imagePath;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 部门
     */
    private String departmentStr;

    /**
     * 身份证号
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
}
