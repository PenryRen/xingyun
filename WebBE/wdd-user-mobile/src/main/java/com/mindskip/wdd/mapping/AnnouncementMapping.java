package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Announcement;
import com.mindskip.wdd.domain.AnnouncementArchive;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementArchiveVM;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementDetailRequestVM;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementPageResponseVM;
import org.mapstruct.*;

import java.util.List;

/**
 * @version 1.7.0
 * @description: The interface Announcement mapping.
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface AnnouncementMapping {

    /**
     * To announcement response vm announcement page response vm.
     *
     * @param announcement the announcement
     * @return the announcement page response vm
     */
    @Mappings({@Mapping(target = "createTime", expression = "java(DateTimeUtil.dateFormat(announcement.getCreateTime()))")})
    AnnouncementPageResponseVM toAnnouncementResponseVM(Announcement announcement);

    /**
     * To announcement announcement.
     *
     * @param announcementDetailRequestVM the announcement detail request vm
     * @return the announcement
     */
    Announcement toAnnouncement(AnnouncementDetailRequestVM announcementDetailRequestVM);

    /**
     * Map announcement.
     *
     * @param announcementDetailRequestVM the announcement detail request vm
     * @param announcement                the announcement
     */
    @InheritConfiguration
    void mapAnnouncement(AnnouncementDetailRequestVM announcementDetailRequestVM, @MappingTarget Announcement announcement);

    /**
     * To announcement edit request vm announcement detail request vm.
     *
     * @param announcement the announcement
     * @return the announcement detail request vm
     */
    @Mappings({@Mapping(target = "createTime", expression = "java(DateTimeUtil.dateFormat(announcement.getCreateTime()))")})
    AnnouncementDetailRequestVM toAnnouncementEditRequestVM(Announcement announcement);


    AnnouncementArchiveVM toAnnouncementArchiveVM(AnnouncementArchive announcementArchive);

    List<AnnouncementArchiveVM> toAnnouncementArchiveVMList(List<AnnouncementArchive> announcementArchiveList);
}
