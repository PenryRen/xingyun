package com.mindskip.wdd.domain.frame;

import lombok.Data;

import java.io.Serializable;


/**
 * @version 7.1.0
 * @description: 证书模板
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/29 10:45
 */
@Data
public class CredentialItemFrame implements Serializable {

    private static final long serialVersionUID = 6279754155142759566L;
    /**
     * 节点key
     */
    private String key;
    /**
     * 节点类型
     */
    private Integer type;
    /**
     * 节点文本
     */
    private String text;
    /**
     * 有效期
     */
    private Integer validityMonth;
    /**
     * 字体大小
     */
    private Integer fontSize;
    /**
     * 字体粗细
     */
    private Integer fontWeight;
    /**
     * 字体颜色
     */
    private String fontColor;
    /**
     * 位置x
     */
    private Integer elementX;
    /**
     * 位置y
     */
    private Integer elementY;
    /**
     * 距离左侧
     */
    private String elementLeft;
    /**
     * 距离顶部
     */
    private String elementTop;
}
