package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 9.0.0
 * @description: 用户证书
 * Copyright (C), 2025, 麟航团队
 * @date 2025/10/26 10:45
 */
@Getter
@Setter
public class UserCredential {
    private Long id;

    /**
     * 证书模板Id
     */
    private Integer credentialTemplateId;

    /**
     * 用户Id
     */
    private Integer userId;

    /**
     * 答卷Id
     */
    private Long examPaperAnswerId;

    /**
     * 真实姓名
     */
    private String userRealName;

    /**
     * 试卷名称
     */
    private String examPaperName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 证书图片
     */
    private String credentialImagePath;

    /**
     * 证书创建时间
     */
    private Date credentialBuildTime;

    /**
     * 试卷Id
     */
    private Long examPaperId;

    /**
     * 试卷规则Id
     */
    private Long examPaperBuildId;

    /**
     * 创建者部门
     */
    private Integer createDepartmentId;

    /**
     * 证书编号
     */
    private String credentialNo;

    /**
     * 培训Id
     */
    private Integer trainId;

    /**
     * 培训名称
     */
    private String trainName;

    /**
     * 用户培训id
     */
    private Long trainUserId;

}