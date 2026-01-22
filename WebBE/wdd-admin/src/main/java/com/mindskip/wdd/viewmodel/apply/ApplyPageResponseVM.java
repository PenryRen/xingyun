package com.mindskip.wdd.viewmodel.apply;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 报名分页返回
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class ApplyPageResponseVM {

    private Integer id;

    /**
     * 报名名称
     */
    private String name;

    /**
     * 报名截止时间
     */
    private String applyEndTime;

    /**
     * 考试结束时间
     */
    private String limitEndTime;

    /**
     * 考试开始时间
     */
    private String limitStartTime;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 是否限制人数
     */
    private Boolean limited;

    /**
     * 是否限制人数
     */
    private String limitedStr;

    /**
     * 限制人数数量
     */
    private Integer count;

    /**
     * 报名状态
     */
    private Integer status;

    /**
     * 报名状态
     */
    private String statusStr;

    /**
     * 报名发布部门
     */
    private String departmentNameList;

    /**
     * 报名数量
     */
    private String applyCount;

    /**
     * 报名分类
     */
    private String applyArchive;
}
