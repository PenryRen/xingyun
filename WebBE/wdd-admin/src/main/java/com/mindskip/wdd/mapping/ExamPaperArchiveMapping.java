package com.mindskip.wdd.mapping;


import com.mindskip.wdd.domain.ExamPaperArchive;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperArchiveEditRequestVM;
import org.mapstruct.*;


/**
 * @version 1.7.0
 * @description: ExamPaperArchiveMapping
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface ExamPaperArchiveMapping {


    /**
     * To exam paper archive exam paper archive.
     *
     * @param examPaperArchiveEditRequestVM the exam paper archive edit request vm
     * @return the exam paper archive
     */
    ExamPaperArchive toExamPaperArchive(ExamPaperArchiveEditRequestVM examPaperArchiveEditRequestVM);

    /**
     * Map exam paper archive.
     *
     * @param examPaperArchiveEditRequestVM the exam paper archive edit request vm
     * @param examPaperArchive              the exam paper archive
     */
    @InheritConfiguration
    void mapExamPaperArchive(ExamPaperArchiveEditRequestVM examPaperArchiveEditRequestVM, @MappingTarget ExamPaperArchive examPaperArchive);

    /**
     * To exam paper archive edit request vm exam paper archive edit request vm.
     *
     * @param examPaperArchive the exam paper archive
     * @return the exam paper archive edit request vm
     */
    ExamPaperArchiveEditRequestVM toExamPaperArchiveEditRequestVM(ExamPaperArchive examPaperArchive);


    @Mappings({
            @Mapping(target = "value", source = "id"),
            @Mapping(target = "label", source = "name"),
    })
    ArchiveVM toArchiveVM(ExamPaperArchive examPaperArchive);
}
