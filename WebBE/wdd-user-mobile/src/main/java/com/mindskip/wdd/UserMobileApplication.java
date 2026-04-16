package com.mindskip.wdd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @version 1.7.0
 * @description: 移动端启动类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
public class UserMobileApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserMobileApplication.class, args);
    }
}
