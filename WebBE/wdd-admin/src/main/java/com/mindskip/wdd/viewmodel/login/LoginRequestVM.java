package com.mindskip.wdd.viewmodel.login;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @version 1.7.0
 * @description: 登录
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class LoginRequestVM {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]{5,24}$", message = "用户名由5至24位字母和数字组成")
    private String userName;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Length(min = 5, message = "密码长度在5到24个字符之间")
    private String password;

}
