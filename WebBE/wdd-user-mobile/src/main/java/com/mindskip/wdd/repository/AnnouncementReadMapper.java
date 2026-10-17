package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.AnnouncementRead;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @version 1.7.0
 * @description: 公告已读列表
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper
public interface AnnouncementReadMapper extends BaseMapper<AnnouncementRead> {
    /**
     * 查询公告是否已读
     *
     * @param announcementId the announcement id
     * @param userId         the user id
     * @return the by ids
     */
    AnnouncementRead getByIds(@Param("announcementId") Integer announcementId, @Param("userId") Integer userId);
}