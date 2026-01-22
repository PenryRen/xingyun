package com.mindskip.wdd.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mindskip.wdd.configuration.mybaties.CredentialItemFrameJsonTypeHandler;
import com.mindskip.wdd.domain.frame.CredentialItemFrame;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 证书模板
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Getter
@Setter
@TableName(value = "t_credential_template", autoResultMap = true)
public class CredentialTemplate {
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
     * 模板图片地址
     */
    private String templateImagePath;

    /**
     * 创建者
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 创建人部门
     */
    private Integer createDepartmentId;

    /**
     * 配置
     */
    @TableField(typeHandler = CredentialItemFrameJsonTypeHandler.class)
    private List<CredentialItemFrame> configuration;

}