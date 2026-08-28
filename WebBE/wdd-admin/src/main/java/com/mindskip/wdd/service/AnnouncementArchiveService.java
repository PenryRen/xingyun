package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.AnnouncementArchive;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementArchiveMoveRequestVM;

import java.util.List;


/**
 * @version 1.7.0
 * @description: 通知公告分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public interface AnnouncementArchiveService extends IService<AnnouncementArchive> {

    /**
     * 获取公告分类根节点
     *
     * @return
     */
    List<AnnouncementArchive> getRootAnnouncementArchive();

    /**
     * 通过id 获取通知公告分类
     *
     * @param id
     * @return
     */
    List<AnnouncementArchive> getAnnouncementArchiveByParentId(Integer id);

    /**
     * 更新通知公告分类层级
     *
     * @param originalLevel 原始层级
     * @param targetLevel   目标层级
     * @return
     */
    int updateLevel(String originalLevel, String targetLevel);

    /**
     * 根据层级获取通知公告
     *
     * @param level
     * @return
     */
    AnnouncementArchive getByLevel(String level);

    /**
     * 根据层级删除通知公告
     *
     * @param level
     * @return
     */
    int deleteByLevel(String level);

    RestResponse move(AnnouncementArchiveMoveRequestVM announcementArchiveMoveRequestVM);
}
