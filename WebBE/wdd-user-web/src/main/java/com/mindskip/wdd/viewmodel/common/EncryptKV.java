package com.mindskip.wdd.viewmodel.common;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 加密键值对
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class EncryptKV {
    /**
     * 密钥
     */
    private String key;
    /**
     * 加密内容
     */
    private String value;
}
