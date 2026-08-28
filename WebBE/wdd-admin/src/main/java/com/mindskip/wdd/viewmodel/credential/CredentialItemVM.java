package com.mindskip.wdd.viewmodel.credential;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @version 7.1.0
 * @description: 证书模板
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/29 10:45
 */
@Data
public class CredentialItemVM {
    /**
     * 节点key
     */
    @NotBlank
    private String key;
    /**
     * 节点类型
     */
    @NotNull
    private Integer type;
    /**
     * 节点文本
     */
    @NotBlank
    private String text;
    /**
     * 有效期
     */
    private Integer validityMonth;
    /**
     * 字体大小
     */
    @NotNull
    private Integer fontSize;
    /**
     * 字体粗细
     */
    @NotNull
    private Integer fontWeight;
    /**
     * 字体颜色
     */
    @NotBlank
    private String fontColor;
    /**
     * 位置x
     */
    @NotNull
    private Integer elementX;
    /**
     * 位置y
     */
    @NotNull
    private Integer elementY;
    /**
     * 距离左侧
     */
    @NotBlank
    private String elementLeft;
    /**
     * 距离顶部
     */
    @NotBlank
    private String elementTop;
}
