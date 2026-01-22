package com.mindskip.wdd.domain.frame;

import lombok.Data;

import java.io.Serializable;


/**
 * @version 7.1.0
 * @description: 证书模板
 * Copyright (C), 2024, 麒技团队
 * @date 2024/7/29 10:45
 */
@Data
public class CredentialItemFrame implements Serializable {

    private static final long serialVersionUID = 6279754155142759566L;
    private String key;

    private Integer type;

    private String text;

    private Integer validityMonth;

    private Integer fontSize;

    private Integer fontWeight;

    private String fontColor;

    private Integer elementX;

    private Integer elementY;

    private String elementLeft;

    private String elementTop;
}
