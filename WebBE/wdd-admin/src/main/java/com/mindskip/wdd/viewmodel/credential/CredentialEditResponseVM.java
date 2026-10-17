package com.mindskip.wdd.viewmodel.credential;

import lombok.Data;

import java.util.List;


/**
 * @version 1.7.0
 * @description: 证书模板编辑返回
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class CredentialEditResponseVM {

    private Integer id;
    /**
     * 模板名称
     */
    private String name;

    /**
     * 发证机构
     */
    private String company;

    /**
     * 证书模板图片地址
     */
    private String templateImagePath;

    /**
     * 证书节点
     */
    private List<CredentialItemVM> credentialItemVMList;
}
