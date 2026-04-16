package com.mindskip.wdd.viewmodel.profile;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 个人信息
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ProfileInfoVM {
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
    private String sexStr;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 图片地址
     */
    private String imagePath;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 最后活动时间
     */
    private String lastActiveTime;

    /**
     * 角色
     */
    private String roleStr;

    /**
     * 邮箱
     */
    private String email;


    /**
     * 部门
     */
    private String departmentStr;
}
