package com.mindskip.wdd.repository;

import com.mindskip.wdd.domain.SystemStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 系统状态
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface SystemStatusMapper {

    /**
     * 获得mysql版本
     *
     * @return {@link String}
     */
    String getVersion();

    /**
     * 获取系统状态
     *
     * @param key the key
     * @return the status
     */
    List<SystemStatus> getStatus(@Param("key") String key);

}
