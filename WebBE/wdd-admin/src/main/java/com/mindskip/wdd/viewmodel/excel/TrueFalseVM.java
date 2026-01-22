package com.mindskip.wdd.viewmodel.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @version 1.7.0
 * @description: 判断题导入
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
@ColumnWidth(20)
public class TrueFalseVM implements Serializable {

    /**
     * 题目分类
     */
    @ExcelProperty(value = "分类", index = 0)
    private String level;


    /**
     * 题干
     */
    @ExcelProperty(value = "题干", index = 1)
    @NotBlank(message = "题干不能为空")
    private String title;

    /**
     * 选项a
     */
    @ExcelProperty(value = "选项A", index = 2)
    private String a;

    /**
     * 选项b
     */
    @ExcelProperty(value = "选项B", index = 3)
    private String b;


    /**
     * 标答
     */
    @ExcelProperty(value = "标答", index = 4)
    @NotBlank(message = "标答不能为空")
    private String correct;


    /**
     * 解析
     */
    @ExcelProperty(value = "解析", index = 5)
    private String analyze;


    /**
     * 分数
     */
    @ExcelProperty(value = "分数", index = 6)
    @NumberFormat("#.#")
    @NotBlank(message = "分数不能为空")
    private String score;


    /**
     * 难度
     */
    @ExcelProperty(value = "难度", index = 7)
    @NotNull(message = "难度不能为空")
    @Range(min = 1, max = 3, message = "难度范围在 1-3 之间")
    private Integer difficult;


    /**
     * 导入结果
     */
    @ExcelProperty(value = "导入结果", index = 8)
    private String result;

}
