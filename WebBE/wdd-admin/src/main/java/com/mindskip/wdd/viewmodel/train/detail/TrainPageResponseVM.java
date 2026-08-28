package com.mindskip.wdd.viewmodel.train.detail;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 9.0.0
 * @description: 培训详情分页返回
 * Copyright (C), 2025, 麟航团队
 * @date 2025/10/26 10:45
 */
@Data
@ColumnWidth(15)
public class TrainPageResponseVM implements Serializable {

    /**
     * Id
     */
    @ExcelProperty(value = "Id", index = 0)
    private Long id;

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
     * 工号
     */
    @ExcelProperty(value = "工号", index = 3)
    private String workNo;

    /**
     * 身份证号
     */
    @ExcelProperty(value = "身份证号", index = 4)
    private String idCard;

    /**
     * 职位
     */
    @ExcelProperty(value = "职位", index = 5)
    private String jobTitle;

    /**
     * 部门
     */
    @ExcelProperty(value = "班级", index = 6)
    private String departmentLevel;

    /**
     * 部门
     */
    @ExcelIgnore
    private Integer departmentId;


    /**
     * 状态
     */
    @ExcelIgnore
    private Integer status;


    @ExcelProperty(value = "培训状态", index = 7)
    private String statusStr;


    /**
     * 创建人
     */
    @ExcelIgnore
    private Integer createUser;

    /**
     * 创建时间
     */
    @ExcelIgnore
    private Date createTime;

    /**
     * 开始时间
     */
    @ExcelProperty(value = "开始时间", index = 8)
    @ColumnWidth(20)
    private String createTimeStr;


    @ExcelIgnore
    private Date completeTime;

    /**
     * 完成时间
     */
    @ExcelProperty(value = "完成时间", index = 9)
    @ColumnWidth(20)
    private String completeTimeStr;


}
