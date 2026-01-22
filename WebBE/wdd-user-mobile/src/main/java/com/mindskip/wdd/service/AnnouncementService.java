package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Announcement;
import com.mindskip.wdd.domain.AnnouncementArchive;
import com.mindskip.wdd.domain.AnnouncementRead;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementPageRequestVM;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 公告
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface AnnouncementService extends IService<Announcement> {

    /**
     * 查询公告是否已读
     *
     * @param announcementId the announcement id
     * @param userId         the user id
     * @return the announcement read by ids
     */
    AnnouncementRead getAnnouncementReadByIds(Integer announcementId, Integer userId);

    /**
     * 插入公告读取记录
     *
     * @param announcementRead the announcement read
     * @return the int
     */
    int insertAnnouncementRead(AnnouncementRead announcementRead);

    /**
     * 公告分页
     *
     * @param requestVM the request vm
     * @return the page info
     */
    PageInfo<Announcement> page(AnnouncementPageRequestVM requestVM);

    /**
     * 获取一级分类
     *
     * @return {@link List}<{@link AnnouncementArchive}>
     */
    List<AnnouncementArchive> selectRootTree();

    /**
     * 根据id获取分类
     *
     * @param id
     * @return {@link AnnouncementArchive}
     */
    AnnouncementArchive getAnnouncementArchiveById(Integer id);

    /**
     * 根据层级获取分类
     *
     * @param level
     * @return {@link List}<{@link AnnouncementArchive}>
     */
    List<AnnouncementArchive> getAnnouncementArchiveByLevel(String level);

}
