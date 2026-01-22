package com.mindskip.wdd.viewmodel.user;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @version 1.7.0
 * @description: 用户分页返回
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
@ColumnWidth(20)
public class UserPageResponseVM {

    @ExcelProperty(value = "Id", index = 0)
    private Integer id;
    /**
     * UUID
     */
    @ExcelIgnore
    private String userUuid;
    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名", index = 1)
    private String userName;
    /**
     * 真实姓名
     */
    @ExcelProperty(value = "真实姓名", index = 2)
    private String realName;
    /**
     * 年龄
     */
    @ExcelIgnore
    private Integer age;
    /**
     * 角色
     */
    @ExcelIgnore
    private Integer role;
    /**
     * 性别
     */
    @ExcelIgnore
    private Integer sex;

    @ExcelIgnore
    private String sexStr;
    /**
     * 生日
     */
    @ExcelIgnore
    private String birthDay;
    /**
     * 手机号
     */
    @ExcelIgnore
    private String phone;

    /**
     * 最后活动时间
     */
    @ExcelIgnore
    private String lastActiveTime;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间", index = 7)
    private String createTime;

    /**
     * 修改时间
     */
    @ExcelIgnore
    private String modifyTime;
    /**
     * 状态
     */
    @ExcelIgnore
    private Integer status;

    @ExcelProperty(value = "状态", index = 8)
    private String statusStr;

    /**
     * 部门
     */
    @ColumnWidth(30)
    @ExcelProperty(value = "班级", index = 3)
    private String departmentLevel;
    /**
     * 邮箱
     */
    @ExcelIgnore
    private String email;

    /**
     * 角色
     */
    @ExcelIgnore
    private String roleStr;
    /**
     * 身份证
     */
    @ExcelProperty(value = "身份证", index = 6)
    private String idCard;
    /**
     * 工号
     */
    @ExcelProperty(value = "工号", index = 5)
    private String workNo;
    /**
     * 职位
     */
    @ExcelProperty(value = "职位", index = 4)
    private String jobTitle;
    /**
     * 部门
     */
    @ExcelIgnore
    private Integer departmentId;


    @ExcelIgnore
    private String imagePath;

}
