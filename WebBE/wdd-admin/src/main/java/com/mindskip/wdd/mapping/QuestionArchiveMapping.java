package com.mindskip.wdd.mapping;


import com.mindskip.wdd.domain.QuestionArchive;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import com.mindskip.wdd.viewmodel.question.QuestionArchiveEditRequestVM;
import org.mapstruct.*;


/**
 * @version 1.7.0
 * @description: QuestionArchiveMapping
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface QuestionArchiveMapping {


    /**
     * To question archive question archive.
     *
     * @param questionArchiveEditRequestVM the question archive edit request vm
     * @return the question archive
     */
    QuestionArchive toQuestionArchive(QuestionArchiveEditRequestVM questionArchiveEditRequestVM);

    /**
     * Map question archive.
     *
     * @param questionArchiveEditRequestVM the question archive edit request vm
     * @param questionArchive              the question archive
     */
    @InheritConfiguration
    void mapQuestionArchive(QuestionArchiveEditRequestVM questionArchiveEditRequestVM, @MappingTarget QuestionArchive questionArchive);

    /**
     * To question archive edit request vm question archive edit request vm.
     *
     * @param questionArchive the question archive
     * @return the question archive edit request vm
     */
    QuestionArchiveEditRequestVM toQuestionArchiveEditRequestVM(QuestionArchive questionArchive);

    @Mappings({
            @Mapping(target = "value", source = "id"),
            @Mapping(target = "label", source = "name"),
    })
    ArchiveVM toArchiveVM(QuestionArchive questionArchive);

}
