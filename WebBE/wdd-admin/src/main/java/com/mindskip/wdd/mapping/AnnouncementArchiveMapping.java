package com.mindskip.wdd.mapping;


import com.mindskip.wdd.domain.AnnouncementArchive;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementArchiveEditRequestVM;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import org.mapstruct.*;


/**
 * @version 1.7.0
 * @description: AnnouncementArchiveMapping
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface AnnouncementArchiveMapping {


    /**
     * To announcement archive announcement archive.
     *
     * @param announcementArchiveEditRequestVM the announcement archive edit request vm
     * @return the announcement archive
     */
    AnnouncementArchive toAnnouncementArchive(AnnouncementArchiveEditRequestVM announcementArchiveEditRequestVM);

    /**
     * Map announcement archive.
     *
     * @param announcementArchiveEditRequestVM the announcement archive edit request vm
     * @param announcementArchive              the announcement archive
     */
    @InheritConfiguration
    void mapAnnouncementArchive(AnnouncementArchiveEditRequestVM announcementArchiveEditRequestVM, @MappingTarget AnnouncementArchive announcementArchive);

    /**
     * To announcement archive edit request vm announcement archive edit request vm.
     *
     * @param announcementArchive the announcement archive
     * @return the announcement archive edit request vm
     */
    AnnouncementArchiveEditRequestVM toAnnouncementArchiveEditRequestVM(AnnouncementArchive announcementArchive);

    @Mappings({
            @Mapping(target = "value", source = "id"),
            @Mapping(target = "label", source = "name"),
    })
    ArchiveVM toArchiveVM(AnnouncementArchive forumArchive);

}
