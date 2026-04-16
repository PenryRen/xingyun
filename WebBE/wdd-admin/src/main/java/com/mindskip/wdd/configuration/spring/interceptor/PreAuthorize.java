package com.mindskip.wdd.configuration.spring.interceptor;

import java.lang.annotation.*;

/**
 * @version 1.7.0
 * @description: 权限注解
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PreAuthorize {
    /**
     * 权限数组
     *
     * @return the string [ ]
     */
    String[] value();
}

