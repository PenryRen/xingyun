package com.mindskip.wdd.mapping;


import com.mindskip.wdd.domain.ApplyArchive;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.apply.ApplyArchiveEditRequestVM;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import org.mapstruct.*;


/**
 * @version 1.7.0
 * @description: ApplyArchiveMapping
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface ApplyArchiveMapping {


    /**
     * To apply archive apply archive.
     *
     * @param applyArchiveEditRequestVM the apply archive edit request vm
     * @return the apply archive
     */
    ApplyArchive toApplyArchive(ApplyArchiveEditRequestVM applyArchiveEditRequestVM);

    /**
     * Map apply archive.
     *
     * @param applyArchiveEditRequestVM the apply archive edit request vm
     * @param applyArchive              the apply archive
     */
    @InheritConfiguration
    void mapApplyArchive(ApplyArchiveEditRequestVM applyArchiveEditRequestVM, @MappingTarget ApplyArchive applyArchive);

    /**
     * To apply archive edit request vm apply archive edit request vm.
     *
     * @param applyArchive the apply archive
     * @return the apply archive edit request vm
     */
    ApplyArchiveEditRequestVM toApplyArchiveEditRequestVM(ApplyArchive applyArchive);

    @Mappings({
            @Mapping(target = "value", source = "id"),
            @Mapping(target = "label", source = "name"),
    })
    ArchiveVM toArchiveVM(ApplyArchive applyArchive);
}
