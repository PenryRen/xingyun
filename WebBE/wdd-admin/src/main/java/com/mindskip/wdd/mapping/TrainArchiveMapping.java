package com.mindskip.wdd.mapping;


import com.mindskip.wdd.domain.TrainArchive;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import com.mindskip.wdd.viewmodel.train.archive.TrainArchiveEditRequestVM;
import org.mapstruct.*;


/**
 * @version 9.0.0
 * @description: 培训分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface TrainArchiveMapping {


    /**
     * To train archive train archive.
     *
     * @param trainArchiveEditRequestVM the train archive edit request vm
     * @return the train archive
     */
    TrainArchive toTrainArchive(TrainArchiveEditRequestVM trainArchiveEditRequestVM);

    /**
     * Map train archive.
     *
     * @param trainArchiveEditRequestVM the train archive edit request vm
     * @param trainArchive              the train archive
     */
    @InheritConfiguration
    void mapTrainArchive(TrainArchiveEditRequestVM trainArchiveEditRequestVM, @MappingTarget TrainArchive trainArchive);

    /**
     * To train archive edit request vm train archive edit request vm.
     *
     * @param trainArchive the train archive
     * @return the train archive edit request vm
     */
    TrainArchiveEditRequestVM toTrainArchiveEditRequestVM(TrainArchive trainArchive);


    @Mappings({
            @Mapping(target = "value", source = "id"),
            @Mapping(target = "label", source = "name"),
    })
    ArchiveVM toArchiveVM(TrainArchive trainArchive);

}
