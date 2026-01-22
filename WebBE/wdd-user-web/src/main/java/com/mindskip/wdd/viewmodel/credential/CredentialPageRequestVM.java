package com.mindskip.wdd.viewmodel.credential;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

/**
 * @version 1.7.0
 * @description: 证书模板过滤
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class CredentialPageRequestVM extends BasePage {
    /**
     * 用户id
     */
    private Integer userId;
}
