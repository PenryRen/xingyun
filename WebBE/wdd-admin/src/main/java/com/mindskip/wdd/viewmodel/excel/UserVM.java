package com.mindskip.wdd.viewmodel.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @version 1.7.0
 * @description: 用户导入
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
@ColumnWidth(20)
public class UserVM implements Serializable {

    /**
     * 用户id
     */
    @ExcelIgnore
    private Integer id;

    /**
     * 部门id
     */
    @ExcelIgnore
    private Integer departmentId;

    /**
     * 是否导入成功
     */
    @ExcelIgnore
    private Boolean success;

    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名", index = 0)
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]{5,24}$", message = "用户名由5至24位字母和数字组成")
    private String userName;

    /**
     * 密码
     */
    @ExcelProperty(value = "密码", index = 1)
    @NotBlank(message = "密码不能为空")
    @Length(min = 5, max = 24, message = "密码长度在5到24个字符之间")
    private String password;

    /**
     * 真实姓名
     */
    @ExcelProperty(value = "真实姓名", index = 2)
    @NotBlank(message = "真实姓名不能为空")
    @Length(max = 10, message = "真实姓名长度在 10 个字符之下")
    private String realName;

    /**
     * 部门
     */
    @ExcelProperty(value = "班级", index = 3)
    private String departmentLevel;

    /**
     * 职位
     */
    @ExcelProperty(value = "职位", index = 4)
    private String jobTitle;

    /**
     * 工号
     */
    @ExcelProperty(value = "工号", index = 5)
    private String workNo;

    /**
     * 身份证号
     */
    @ExcelProperty(value = "身份证号", index = 6)
    private String idCard;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号", index = 7)
    private String phone;

    /**
     * 邮箱
     */
    @ExcelProperty(value = "邮箱", index = 8)
    private String email;

    /**
     * 导入结果
     */
    @ExcelProperty(value = "导入结果", index = 9)
    private String result;
}
