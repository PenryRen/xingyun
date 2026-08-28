package com.mindskip.wdd.viewmodel.credential;

import lombok.Data;

/**
 * @version 9.0.0
 * @description: 证书分页返回
 * Copyright (C), 2025, 麟航团队
 * @date 2025/10/26 10:45
 */
@Data
public class CredentialUserPageResponseVM {

    private Long id;

    /**
     * 证书地址
     */
    private String credentialImagePath;
    /**
     * 证书模板图片地址
     */
    private String templateImagePath;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 证书名称
     */
    private String credentialName;
    /**
     * 发证机构
     */
    private String company;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 工号
     */
    private String workNo;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 证书编号
     */
    private String no;


}
