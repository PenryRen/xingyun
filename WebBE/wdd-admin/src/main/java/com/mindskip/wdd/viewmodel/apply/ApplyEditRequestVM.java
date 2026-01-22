package com.mindskip.wdd.viewmodel.apply;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 报名编辑
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class ApplyEditRequestVM {

    private Integer id;

    /**
     * 报名名称
     */
    @NotBlank(message = "报名名称不能为空")
    private String name;

    /**
     * 报名截止时间
     */
    @NotBlank(message = "报名截止时间不能为空")
    private String applyEndTime;

    /**
     * 是否限制人数
     */
    @NotNull(message = "是否限制人数不能为空")
    private Boolean limited;

    /**
     * 限制人数数量
     */
    private Integer count;

    /**
     * 考试时间限制
     */
    @Size(min = 2, max = 2)
    private List<String> limitDateTime;


    /**
     * 考试部门
     */
    @NotNull(message = "部门不能为空")
    private List<Integer> departmentIdList;

    /**
     * 报名分类
     */
    private Integer applyArchiveId;

    /**
     * 是否需要审核
     */
    @NotNull(message = "是否需要审核不能为空")
    private Boolean needAudit;
}
