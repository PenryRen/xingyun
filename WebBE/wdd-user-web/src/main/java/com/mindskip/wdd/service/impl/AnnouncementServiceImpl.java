package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Announcement;
import com.mindskip.wdd.domain.AnnouncementArchive;
import com.mindskip.wdd.domain.AnnouncementRead;
import com.mindskip.wdd.repository.AnnouncementArchiveMapper;
import com.mindskip.wdd.repository.AnnouncementMapper;
import com.mindskip.wdd.repository.AnnouncementReadMapper;
import com.mindskip.wdd.service.AnnouncementService;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementPageRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 公告
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Service
@AllArgsConstructor
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    private final AnnouncementMapper announcementMapper;
    private final AnnouncementReadMapper announcementReadMapper;
    private final AnnouncementArchiveMapper announcementArchiveMapper;

    @Override
    public List<AnnouncementArchive> selectRootTree() {
        return announcementArchiveMapper.selectRootTree();
    }

    @Override
    public List<AnnouncementArchive> getByParentId(Integer parentId) {
        return announcementArchiveMapper.getByParentId(parentId);
    }

    @Override
    public AnnouncementRead getAnnouncementReadByIds(Integer announcementId, Integer userId) {
        return announcementReadMapper.getByIds(announcementId, userId);
    }

    @Override
    public int insertAnnouncementRead(AnnouncementRead announcementRead) {
        return announcementReadMapper.insert(announcementRead);
    }

    @Override
    public PageInfo<Announcement> page(AnnouncementPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "overhead desc,importanted desc,id desc").doSelectPageInfo(() ->
                announcementMapper.page(requestVM)
        );
    }


}
