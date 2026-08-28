package com.mindskip.wdd.viewmodel.announcement;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 公告已读人员列表
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class AnnouncementUserPageResponseVM {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 部门层级
     */
    private String departmentLevel;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 部门
     */
    private Integer departmentId;
    /**
     * 工号
     */
    private String workNo;
    /**
     * 身份证
     */
    private String idCard;
    /**
     * 职位
     */
    private String jobTitle;
}
