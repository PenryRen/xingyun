package com.mindskip.wdd.viewmodel.apply;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 报名基本信息
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ApplyInfoVM {

    private Integer id;

    /**
     * 报名名称
     */
    private String name;

    /**
     * 报名结束时间
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
    private String createUser;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 报名状态
     */
    private String statusStr;

    /**
     * 报名发布名称
     */
    private String departmentNameList;

    /**
     * 报名人数
     */
    private String applyCount;

    /**
     * 部门用户数量
     */
    private Integer departmentUserCount;

    /**
     * 是否限制人数
     */
    private String limitedStr;

    /**
     * 报名分类
     */
    private String applyArchive;

}

