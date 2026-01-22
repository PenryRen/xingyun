package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @version 1.7.0
 * @description: 系统状态
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Getter
@Setter
public class SystemStatus {
    /**
     * 系统属性名称
     */
    private String variableName;
    /**
     * 系统属性值
     */
    private String value;
}
