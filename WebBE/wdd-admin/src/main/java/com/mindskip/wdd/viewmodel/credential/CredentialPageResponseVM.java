package com.mindskip.wdd.viewmodel.credential;

import lombok.Data;


@Data
public class CredentialPageResponseVM {

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
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private String createTime;

}
