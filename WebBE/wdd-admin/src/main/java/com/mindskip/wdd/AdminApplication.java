package com.mindskip.wdd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @version 1.7.0
 * @description: 管理系统启动类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@EnableScheduling
@EnableAsync
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}
