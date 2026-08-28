package com.mindskip.wdd.viewmodel.dashboard;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 系统信息展示
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class SystemInfoVM {
    /**
     * 服务器名称
     */
    private String serverName;
    /**
     * 内存
     */
    private String physicalMemory;
    /**
     * 内存占比
     */
    private String physicalMemoryPercent;
    /**
     * 虚拟内存
     */
    private String virtualMemory;
    /**
     * cpu信息
     */
    private Integer cpuCore;
    /**
     * cpu负载
     */
    private String cpuLoad;
    /**
     * 硬盘存储
     */
    private String fileSystem;
    /**
     * 硬盘存储占比
     */
    private String fileSystemPercent;

    /**
     * jvm运行时长
     */
    private String jvmRunTime;
    /**
     * jvm内存
     */
    private String jvmMemory;
    /**
     * jvm内存占比
     */
    private String jvmPercent;
    /**
     * jvm名称
     */
    private String jvmName;
    /**
     * jvm版本
     */
    private String jvmVersion;
    /**
     * jvm启动时间
     */
    private String jvmStartTime;
    /**
     * jvm路径
     */
    private String jvmLocation;

    /**
     * redis运行时间
     */
    private String redisRunTime;
    /**
     * redis版本
     */
    private String redisVersion;
    /**
     * redis内存
     */
    private String redisMemory;
    /**
     * redis内存占比
     */
    private String redisPercent;
    /**
     * redis连接总数
     */
    private String redisConnectionCount;
    /**
     * redis的key总数
     */
    private String redisKeyCount;
    /**
     * redis的过期Key
     */
    private String redisExpiredKey;

    /**
     * mysql线程连接数
     */
    private String mysqlThreadsConnected;
    /**
     * mysql线程创建数
     */
    private String mysqlThreadsCreated;
    /**
     * mysql线程运行数
     */
    private String mysqlThreadsRunning;
    /**
     * mysql表锁立即释放数
     */
    private String mysqlTableLocksImmediate;
    /**
     * mysql表锁等待释放数
     */
    private String mysqlTableLocksWaited;
    /**
     * mysql打开的表数
     */
    private String mysqlOpenTables;
    /**
     * mysql已打开的表数
     */
    private String mysqlOpenedTables;

    /**
     * mysql版本
     */
    private String mysqlVersion;
}
