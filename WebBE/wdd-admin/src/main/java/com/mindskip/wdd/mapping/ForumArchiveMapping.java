package com.mindskip.wdd.mapping;


import com.mindskip.wdd.domain.ForumArchive;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import com.mindskip.wdd.viewmodel.forum.ForumArchiveEditRequestVM;
import org.mapstruct.*;

import java.util.List;


/**
 * @version 1.9.0
 * @description: ForumArchiveMapping
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface ForumArchiveMapping {


    /**
     * To forum archive forum archive.
     *
     * @param forumArchiveEditRequestVM the forum archive edit request vm
     * @return the forum archive
     */
    ForumArchive toForumArchive(ForumArchiveEditRequestVM forumArchiveEditRequestVM);

    /**
     * Map forum archive.
     *
     * @param forumArchiveEditRequestVM the forum archive edit request vm
     * @param forumArchive              the forum archive
     */
    @InheritConfiguration
    void mapForumArchive(ForumArchiveEditRequestVM forumArchiveEditRequestVM, @MappingTarget ForumArchive forumArchive);

    /**
     * To forum archive edit request vm forum archive edit request vm.
     *
     * @param forumArchive the forum archive
     * @return the forum archive edit request vm
     */
    ForumArchiveEditRequestVM toForumArchiveEditRequestVM(ForumArchive forumArchive);


    @Mappings({
            @Mapping(target = "value", source = "id"),
            @Mapping(target = "label", source = "name"),
    })
    ArchiveVM toArchiveVM(ForumArchive forumArchive);

}
