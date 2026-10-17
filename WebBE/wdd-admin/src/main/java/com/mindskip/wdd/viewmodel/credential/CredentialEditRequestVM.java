package com.mindskip.wdd.viewmodel.credential;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @version 7.1.0
 * @description: 证书模板
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/28 10:45
 */
@Data
public class CredentialEditRequestVM {

    private Integer id;

    /**
     * 模板名称
     */
    @NotBlank(message = "模板名称不能为空")
    private String name;

    /**
     * 发证机构
     */
    @NotBlank(message = "发证机构不能为空")
    private String company;

    /**
     * 证书模板图片地址
     */
    @NotBlank(message = "证书模板不能为空")
    private String templateImagePath;

    /**
     * 证书节点
     */
    @Valid
    @Size(min = 1, message = "请添加证书节点")
    private List<CredentialItemVM> credentialItemVMList;

}
