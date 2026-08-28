package com.mindskip.wdd.viewmodel.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @version 1.7.0
 * @description: 用户编辑
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class UserEditRequestVM {

    private Integer id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]{5,24}$", message = "用户名由5至24位字母和数字组成")
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    @NotBlank
    @Length(max = 10, message = "真实姓名长度最大为10个字符")
    private String realName;

    /**
     * 邮箱
     */
    @Email
    @Length(max = 255, message = "邮箱长度最大为255个字符")
    private String email;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 状态
     */
    private Integer status;

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
    @Length(max = 255, message = "手机号长度最大为255个字符")
    private String phone;

    /**
     * 系统角色
     */
    @NotNull
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
     * 创建人
     */
    private Integer createUser;

    /**
     * 头像
     */
    private String imagePath;

}
