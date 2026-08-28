package com.mindskip.wdd.viewmodel.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @version 1.7.0
 * @description: 用户更新信息
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class UpdateRequestVM {

    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    @Length(max = 10, message = "长度在 10 个字符之下")
    private String realName;

    /**
     * 手机号
     */
    @NotBlank
    private String phone;

    /**
     * 邮箱
     */
    @NotBlank
    @Email
    private String email;

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
    private String birthDay;

    /**
     * 图片地址
     */
    private String imagePath;

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
