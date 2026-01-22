package com.mindskip.wdd.viewmodel.train.course;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训列表
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/26 10:45
 */
@Data
public class TrainEditItem {
    private Long id;

    /**
     * 培训id
     */
    private Integer trainId;

    /**
     * 名称
     */
    @NotBlank
    private String name;

    /**
     * 培训内容Id
     */
    @NotNull
    private Integer targetId;

    /**
     * 培训内容类型
     */
    @NotNull
    private Integer targetType;

    /**
     * 合格分数线
     */
    private Integer passNumber;

    private String passNumberStr;

    /**
     * 最大分数线
     */
    private Integer maxNumber;

    private String maxNumberStr;

    /**
     * 排序
     */
    private Integer itemOrder;

    /**
     * 考试次数
     */
    private Integer allowCount;

    /**
     * 预览地址
     */
    private String previewPath;

    /**
     * 证书模板地址
     */
    private String templateImagePath;

    /**
     * 课件类型
     */
    private Integer fileType;
}
