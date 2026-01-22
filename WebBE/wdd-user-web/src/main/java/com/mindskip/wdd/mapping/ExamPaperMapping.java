package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ExamPaper;
import com.mindskip.wdd.domain.ExamPaperArchive;
import com.mindskip.wdd.domain.ExamPaperCache;
import com.mindskip.wdd.domain.ExamPaperChild;
import com.mindskip.wdd.domain.frame.ExamPaperItemFrame;
import com.mindskip.wdd.domain.frame.ExamPaperItemQuestionFrame;
import com.mindskip.wdd.domain.other.QuestionRandomItem;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperArchiveVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoPaperVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoTitle;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperPageResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;


/**
 * @version 1.7.0
 * @description: The interface Exam paper mapping.
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = {DateTimeUtil.class, ExamUtil.class})
public interface ExamPaperMapping {


    /**
     * To exam paper do response vm exam paper do paper vm.
     *
     * @param examPaper the exam paper
     * @return the exam paper do paper vm
     */
    @Mappings({
            @Mapping(target = "score", expression = "java(ExamUtil.scoreToVM(examPaper.getScore()))"),
            @Mapping(target = "suggestTimeStr", expression = "java(ExamUtil.minToVM(examPaper.getSuggestTime()))"),
            @Mapping(target = "paperId", source = "id")
    })
    ExamPaperDoPaperVM toExamPaperDoResponseVM(ExamPaper examPaper);


    @Mappings({
            @Mapping(target = "score", expression = "java(ExamUtil.scoreToVM(examPaperCache.getScore()))"),
            @Mapping(target = "suggestTimeStr", expression = "java(ExamUtil.minToVM(examPaperCache.getSuggestTime()))"),
            @Mapping(target = "paperId", source = "id")
    })
    ExamPaperDoPaperVM toExamPaperDoResponseVM(ExamPaperCache examPaperCache);


    @Mappings({
            @Mapping(target = "paperId", source = "id")
    })
    ExamPaperCache toExamPaperCache(ExamPaper examPaper);

    /**
     * To exam paper do title exam paper do title.
     *
     * @param examPaperItemFrame the exam paper item frame
     * @return the exam paper do title
     */
    ExamPaperDoTitle toExamPaperDoTitle(ExamPaperItemFrame examPaperItemFrame);


    /**
     * To exam paper page response vm exam paper page response vm.
     *
     * @param examPaper the exam paper
     * @return the exam paper page response vm
     */
    @Mappings({
            @Mapping(target = "score", expression = "java(ExamUtil.scoreToVM(examPaper.getScore()))"),
            @Mapping(target = "passScore", expression = "java(ExamUtil.scoreToVM(examPaper.getPassScore()))"),
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaper.getCreateTime()))"),
            @Mapping(target = "limitStartTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaper.getLimitStartTime()))"),
            @Mapping(target = "limitEndTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaper.getLimitEndTime()))"),
            @Mapping(target = "suggestTimeStr", expression = "java(ExamUtil.minToVM(examPaper.getSuggestTime()))"),
    })
    ExamPaperPageResponseVM toExamPaperPageResponseVM(ExamPaper examPaper);


    ExamPaperArchiveVM toExamPaperArchiveVM(ExamPaperArchive examPaperArchive);

    List<ExamPaperArchiveVM> toExamPaperArchiveVMList(List<ExamPaperArchive> examPaperArchiveList);


    ExamPaperItemQuestionFrame toExamPaperItemQuestionFrame(QuestionRandomItem questionRandomItem);


    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createUser", ignore = true),
            @Mapping(target = "createDepartmentId", ignore = true)
    })
    ExamPaperChild toExamPaperChild(ExamPaper examPaper);
}
