package com.mindskip.wdd.viewmodel.credential;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 证书分页返回
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class CredentialPageResponseVM {
    private Long id;

    /**
     * 试卷名称
     */
    private String examPaperName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 证书图片路径
     */
    private String credentialImagePath;

    /**
     * 证书生成时间
     */
    private String credentialBuildTime;
}
