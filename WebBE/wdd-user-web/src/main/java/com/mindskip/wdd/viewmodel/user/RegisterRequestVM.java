package com.mindskip.wdd.viewmodel.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @version 1.7.0
 * @description: 用户注册信息
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class RegisterRequestVM {

    /**
     * 部门
     */
    @NotNull(message = "部门不能为空")
    private Integer departmentId;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]{5,24}$", message = "用户名由5至24位字母和数字组成")
    private String userName;

    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    @Length(max = 10, message = "长度在 10 个字符之下")
    private String realName;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Length(min = 5, max = 24, message = "密码长度在5到24个字符之间")
    private String password;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    @Length(min = 5, max = 24, message = "确认密码长度在5到24个字符之间")
    private String confirmPassword;

    /**
     * 是否同意协议
     */
    @NotNull
    private Boolean agree;
}
