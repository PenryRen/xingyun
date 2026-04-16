package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Announcement;
import com.mindskip.wdd.domain.AnnouncementDepartment;
import com.mindskip.wdd.domain.other.KeyValue;
import com.mindskip.wdd.repository.AnnouncementDepartmentMapper;
import com.mindskip.wdd.repository.AnnouncementMapper;
import com.mindskip.wdd.repository.AnnouncementReadMapper;
import com.mindskip.wdd.service.AnnouncementService;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementPageRequestVM;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementUserPageRequestVM;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementUserPageResponseVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 通知公告
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@AllArgsConstructor
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    private final AnnouncementMapper announcementMapper;
    private final AnnouncementDepartmentMapper announcementDepartmentMapper;
    private final AnnouncementReadMapper announcementReadMapper;


    @Override
    public List<KeyValue> getDepartmentByAnnouncementId(Integer announcementId) {
        return announcementDepartmentMapper.getDepartmentByAnnouncementId(announcementId);
    }

    @Override
    public void deleteAnnouncementDepartmentByAnnouncementId(Integer announcementId) {
        announcementDepartmentMapper.updateDeleteByAnnouncementId(announcementId);
    }

    @Override
    public void insertAnnouncementDepartment(AnnouncementDepartment announcementDepartment) {
        announcementDepartmentMapper.insert(announcementDepartment);
    }

    @Override
    public void updateAnnouncementDepartment(AnnouncementDepartment announcementDepartment) {
        announcementDepartmentMapper.updateById(announcementDepartment);
    }

    @Override
    public PageInfo<Announcement> page(AnnouncementPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                announcementMapper.page(requestVM)
        );
    }

    @Override
    public PageInfo<AnnouncementUserPageResponseVM> userAnnouncementPage(AnnouncementUserPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "createTime desc").doSelectPageInfo(() ->
                announcementReadMapper.userAnnouncementPage(requestVM)
        );
    }


}
