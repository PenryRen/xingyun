package com.mindskip.wdd.viewmodel.answer;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

/**
 * @version 1.7.0
 * @description: 试卷证书过滤
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class PaperCredentialPageRequestVM extends BasePage {
    /**
     * 试卷id
     */
    private Long id;
    /**
     * 证书id
     */
    private Long credentialId;

    /**
     * 用户名
     */
    private String userName;
}
