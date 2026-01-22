package com.mindskip.wdd.viewmodel.user;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 用户编辑返回
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class UserEditResponseVM {

    private Integer id;

    /**
     * UUID
     */
    private String userUuid;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 角色
     */
    private Integer role;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 生日
     */
    private String birthDay;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 系统角色
     */
    private Integer systemRole;

    /**
     * 部门
     */
    private Integer departmentId;

    /**
     * 管理员角色
     */
    private Integer roleId;

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
     * 头像
     */
    private String imagePath;
}
