package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Announcement;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementEditRequestVM;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementInfoVM;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementPageResponseVM;
import org.mapstruct.*;


/**
 * @version 1.7.0
 * @description: AnnouncementMapping
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
    @Mappings({@Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(announcement.getCreateTime()))")})
    AnnouncementPageResponseVM toAnnouncementResponseVM(Announcement announcement);

    /**
     * To announcement announcement.
     *
     * @param announcementEditRequestVM the announcement edit request vm
     * @return the announcement
     */
    Announcement toAnnouncement(AnnouncementEditRequestVM announcementEditRequestVM);

    /**
     * Map announcement.
     *
     * @param announcementEditRequestVM the announcement edit request vm
     * @param announcement              the announcement
     */
    @InheritConfiguration
    void mapAnnouncement(AnnouncementEditRequestVM announcementEditRequestVM, @MappingTarget Announcement announcement);

    /**
     * To announcement edit request vm announcement edit request vm.
     *
     * @param announcement the announcement
     * @return the announcement edit request vm
     */
    AnnouncementEditRequestVM toAnnouncementEditRequestVM(Announcement announcement);

    /**
     * To announcement info vm announcement info vm.
     *
     * @param announcement the announcement
     * @return the announcement info vm
     */
    @Mappings({@Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(announcement.getCreateTime()))")})
    AnnouncementInfoVM toAnnouncementInfoVM(Announcement announcement);
}
