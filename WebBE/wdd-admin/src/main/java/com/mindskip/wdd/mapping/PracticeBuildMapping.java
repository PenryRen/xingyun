package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.HtmlUtil;
import com.mindskip.wdd.viewmodel.answer.PaperInfoVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildEditRequestVM;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildPageResponseVM;
import org.mapstruct.*;


/**
 * @version 9.5.0
 * @description: 模拟练习对象影响
 * Copyright (C), 2024, 麒技团队
 * @date 2024/09/11 10:45
 */
@Mapper(componentModel = "spring", imports = {DateTimeUtil.class, ExamUtil.class, HtmlUtil.class})
public interface PracticeBuildMapping {


    /**
     * To exam paper build page response vm exam paper build page response vm.
     *
     * @param practiceBuild the exam paper build
     * @return the exam paper build page response vm
     */
    @Mappings({
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(practiceBuild.getCreateTime()))"),
            @Mapping(target = "score", expression = "java(ExamUtil.scoreToVM(practiceBuild.getScore()))"),
            @Mapping(target = "suggestTimeStr", expression = "java(ExamUtil.minToVM(practiceBuild.getSuggestTime()))"),
            @Mapping(target = "limitStartTime", expression = "java(DateTimeUtil.dateTimeFullFormat(practiceBuild.getLimitStartTime()))"),
            @Mapping(target = "limitEndTime", expression = "java(DateTimeUtil.dateTimeFullFormat(practiceBuild.getLimitEndTime()))")
    })
    ExamPaperBuildPageResponseVM toExamPaperBuildPageResponseVM(PracticeBuild practiceBuild);


    @Mappings({
            @Mapping(target = "passScore", expression = "java(ExamUtil.scoreToVM(practiceBuild.getPassScore()))"),
            @Mapping(target = "sumScore", expression = "java(ExamUtil.scoreToVM(practiceBuild.getScore()))")
    })
    ExamPaperBuildEditRequestVM toExamPaperBuildEditRequestVM(PracticeBuild practiceBuild);


    /**
     * To exam paper build exam paper build.
     *
     * @param examPaperBuildEditRequestVM
     * @return the exam paper build
     */
    @Mappings({
            @Mapping(target = "score", expression = "java(ExamUtil.scoreFromVM(examPaperBuildEditRequestVM.getSumScore()))"),
            @Mapping(target = "passScore", expression = "java(ExamUtil.scoreFromVM(examPaperBuildEditRequestVM.getPassScore()))"),
            @Mapping(target = "limitStartTime", ignore = true),
            @Mapping(target = "limitEndTime", ignore = true)
    })
    PracticeBuild toPracticeBuild(ExamPaperBuildEditRequestVM examPaperBuildEditRequestVM);

    @InheritConfiguration
    void mapPracticeBuild(ExamPaperBuildEditRequestVM examPaperBuildEditRequestVM, @MappingTarget PracticeBuild practiceBuild);


    @Mappings({
            @Mapping(target = "id", ignore = true)
    })
    PracticeExamPaper toPracticeExamPaper(PracticeBuild practiceBuild);

    @InheritConfiguration
    void mapPracticeExamPaper(PracticeBuild practiceBuild, @MappingTarget PracticeExamPaper practiceExamPaper);



    @Mappings({
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(practiceBuild.getCreateTime()))"),
            @Mapping(target = "score", expression = "java(ExamUtil.scoreToVM(practiceBuild.getScore()))"),
            @Mapping(target = "passScore", expression = "java(ExamUtil.scoreToVM(practiceBuild.getPassScore()))"),
            @Mapping(target = "limitStartTime", expression = "java(DateTimeUtil.dateTimeFullFormat(practiceBuild.getLimitStartTime()))"),
            @Mapping(target = "limitEndTime", expression = "java(DateTimeUtil.dateTimeFullFormat(practiceBuild.getLimitEndTime()))"),
            @Mapping(target = "suggestTimeStr", expression = "java(ExamUtil.minToVM(practiceBuild.getSuggestTime()))"),
    })
    PaperInfoVM toPaperInfoVM(PracticeBuild practiceBuild);


    @Mappings({
            @Mapping(target = "userScore", expression = "java(ExamUtil.scoreToVM(practiceExamPaperAnswer.getUserScore()))"),
            @Mapping(target = "paperScore", expression = "java(ExamUtil.scoreToVM(practiceExamPaperAnswer.getPaperScore()))"),
            @Mapping(target = "doTimeStr", expression = "java(ExamUtil.secondToVM(practiceExamPaperAnswer.getDoTime()))"),
            @Mapping(target = "passScore", expression = "java(ExamUtil.scoreToVM(practiceExamPaperAnswer.getPassScore()))"),
    })
    ExamPaperAnswerInfoResponseVM toExamPaperAnswerInfoResponseVM(PracticeExamPaperAnswer practiceExamPaperAnswer);


    @Mappings({
            @Mapping(target = "paperId", source = "id")
    })
    ExamPaperCache toExamPaperCache(PracticeExamPaper practiceExamPaper);
}
