package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.AnnouncementRead;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementUserPageRequestVM;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementUserPageResponseVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 公告已读人员
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface AnnouncementReadMapper extends BaseMapper<AnnouncementRead> {

    /**
     * 公告已读分页
     *
     * @param announcementUserPageRequestVM the announcement user page request vm
     * @return the list
     */
    List<AnnouncementUserPageResponseVM> userAnnouncementPage(AnnouncementUserPageRequestVM announcementUserPageRequestVM);
}