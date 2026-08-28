package com.mindskip.wdd.viewmodel.profile;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @version 8.2.0
 * @description: 修改密码
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/21 10:45
 */
@Data
public class ChangePasswordVM {
    /**
     * 旧密码
     */
    @NotBlank
    @Length(min = 5, max = 24, message = "旧密码长度在5到24个字符之间")
    private String oldPassword;
    /**
     * 新密码
     */
    @NotBlank
    @Length(min = 5, max = 24, message = "新旧密码长度在5到24个字符之间")
    private String newPassword;
    /**
     * 确认密码
     */
    @NotBlank
    @Length(min = 5, max = 24, message = "确认密码长度在5到24个字符之间")
    private String confirmPassword;
}
