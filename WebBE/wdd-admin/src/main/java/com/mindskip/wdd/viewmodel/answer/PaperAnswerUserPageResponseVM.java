package com.mindskip.wdd.viewmodel.answer;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.7.0
 * @description: 答卷用户
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
@ColumnWidth(15)
public class PaperAnswerUserPageResponseVM implements Serializable {

    /**
     * 答卷Id
     */
    @ExcelProperty(value = "答卷Id", index = 0)
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
     * 得分
     */
    @ExcelProperty(value = "得分", index = 7)
    private String userScoreStr;

    /**
     * 最终得分
     */
    @ExcelIgnore
    private Integer userScore;

    /**
     * 总分
     */
    @ExcelProperty(value = "总分", index = 8)
    private String paperScoreStr;

    /**
     * 试卷总分
     */
    @ExcelIgnore
    private Integer paperScore;


    /**
     * 正确题数
     */
    @ExcelProperty(value = "正确题数", index = 9)
    private Integer questionCorrect;

    /**
     * 总题数
     */
    @ExcelProperty(value = "总题数", index = 10)
    private Integer questionCount;

    /**
     * 耗时
     */
    @ExcelIgnore
    private Integer doTime;

    /**
     * 耗时
     */
    @ExcelProperty(value = "耗时", index = 11)
    private String doTimeStr;

    /**
     * 答卷状态
     */
    @ExcelIgnore
    private Integer status;

    /**
     * 答卷状态
     */
    @ExcelProperty(value = "答卷状态", index = 12)
    private String statusStr;

    /**
     * 是否合格
     */
    @ExcelIgnore
    private Boolean passed;

    /**
     * 是否合格
     */
    @ExcelProperty(value = "是否合格", index = 13)
    private String passStr;

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
     * 提交时间
     */
    @ExcelProperty(value = "提交时间", index = 14)
    @ColumnWidth(20)
    private String createTimeStr;

    /**
     * 答卷文件
     */
    @ExcelIgnore
    private String previewFilePath;

}
