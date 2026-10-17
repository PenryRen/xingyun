package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Announcement;
import com.mindskip.wdd.domain.AnnouncementDepartment;
import com.mindskip.wdd.domain.other.KeyValue;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementPageRequestVM;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementUserPageRequestVM;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementUserPageResponseVM;

import java.util.List;


/**
 * @version 1.7.0
 * @description: 通知公告
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public interface AnnouncementService extends IService<Announcement> {

    /**
     * 获取公告发布部门
     *
     * @param announcementId
     * @return
     */
    List<KeyValue> getDepartmentByAnnouncementId(Integer announcementId);

    /**
     * 删除公告发布部门
     *
     * @param announcementId
     */
    void deleteAnnouncementDepartmentByAnnouncementId(Integer announcementId);

    /**
     * 插入公告部门
     *
     * @param announcementDepartment
     */
    void insertAnnouncementDepartment(AnnouncementDepartment announcementDepartment);

    /**
     * 更新通知公告部门
     *
     * @param announcementDepartment
     */
    void updateAnnouncementDepartment(AnnouncementDepartment announcementDepartment);

    /**
     * 通知公告部门分页查询
     *
     * @param requestVM
     * @return
     */
    PageInfo<Announcement> page(AnnouncementPageRequestVM requestVM);

    /**
     * 公告已读人员分页查询
     *
     * @param requestVM
     * @return
     */
    PageInfo<AnnouncementUserPageResponseVM> userAnnouncementPage(AnnouncementUserPageRequestVM requestVM);
}
