package com.mindskip.wdd.mapping;


import com.mindskip.wdd.domain.ExamPaper;
import com.mindskip.wdd.domain.ExamPaperBuild;
import com.mindskip.wdd.domain.ExamPaperCache;
import com.mindskip.wdd.domain.ExamPaperChild;
import com.mindskip.wdd.domain.frame.ExamPaperBuildQuestion;
import com.mindskip.wdd.domain.frame.ExamPaperBuildTitle;
import com.mindskip.wdd.domain.frame.ExamPaperItemFrame;
import com.mindskip.wdd.domain.frame.ExamPaperItemQuestionFrame;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoPaperVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoTitle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


/**
 * @version 1.7.0
 * @description: ExamPaperMapping
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
            @Mapping(target = "paperId", source = "id")
    })
    ExamPaperDoPaperVM toExamPaperDoResponseVM(ExamPaper examPaper);

    @Mappings({
            @Mapping(target = "paperId", source = "id")
    })
    ExamPaperCache toExamPaperCache(ExamPaper examPaper);


    @Mappings({
            @Mapping(target = "score", expression = "java(ExamUtil.scoreToVM(examPaperCache.getScore()))"),
            @Mapping(target = "suggestTimeStr", expression = "java(ExamUtil.minToVM(examPaperCache.getSuggestTime()))"),
            @Mapping(target = "paperId", source = "id")
    })
    ExamPaperDoPaperVM toExamPaperDoResponseVM(ExamPaperCache examPaperCache);

    /**
     * To exam paper do title exam paper do title.
     *
     * @param examPaperItemFrame the exam paper item frame
     * @return the exam paper do title
     */
    ExamPaperDoTitle toExamPaperDoTitle(ExamPaperItemFrame examPaperItemFrame);

    /**
     * To exam paper exam paper.
     *
     * @param examPaperBuild the exam paper build
     * @return the exam paper
     */
    @Mappings({
            @Mapping(target = "id", ignore = true)
    })
    ExamPaper toExamPaper(ExamPaperBuild examPaperBuild);

    /**
     * To exam paper item frame exam paper item frame.
     *
     * @param examPaperBuildTitle the exam paper build title
     * @return the exam paper item frame
     */
    ExamPaperItemFrame toExamPaperItemFrame(ExamPaperBuildTitle examPaperBuildTitle);

    /**
     * To exam paper item question frame exam paper item question frame.
     *
     * @param examPaperBuildQuestion the exam paper build question
     * @return the exam paper item question frame
     */
    @Mappings({@Mapping(target = "trickScore", source = "score")})
    ExamPaperItemQuestionFrame toExamPaperItemQuestionFrame(ExamPaperBuildQuestion examPaperBuildQuestion);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "createUser", ignore = true),
            @Mapping(target = "createDepartmentId", ignore = true)
    })
    ExamPaperChild toExamPaperChild(ExamPaper examPaper);
}
