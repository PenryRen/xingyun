package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.Announcement;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 通知公告
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {
    /**
     * 公告分页
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<Announcement> page(AnnouncementPageRequestVM requestVM);
}