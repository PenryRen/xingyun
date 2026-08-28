package com.mindskip.wdd.domain.other;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 键值对
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class KeyValue {
    /**
     * 键名称
     */
    private String name;
    /**
     * 值
     */
    private Integer value;
    /**
     * 备用键名
     */
    private String nameSecond;
}
