package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ExamPaperAnswer;
import com.mindskip.wdd.domain.ExamPaperAnswerMonitor;
import com.mindskip.wdd.domain.ExamPaperBuild;
import com.mindskip.wdd.domain.frame.ExamPaperBuildQuestion;
import com.mindskip.wdd.domain.frame.ExamPaperBuildTitle;
import com.mindskip.wdd.domain.frame.ExamPaperItemQuestionFrame;
import com.mindskip.wdd.domain.other.QuestionRandomItem;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.HtmlUtil;
import com.mindskip.wdd.viewmodel.answer.CameraInfoVM;
import com.mindskip.wdd.viewmodel.answer.ExamPaperPageResponseVM;
import com.mindskip.wdd.viewmodel.answer.PaperInfoVM;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildEditRequestVM;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildPageResponseVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperTitleItemVM;
import com.mindskip.wdd.viewmodel.question.QuestionEditRequestVM;
import org.mapstruct.*;


/**
 * @version 1.7.0
 * @description: ExamPaperBuildMapping
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = {DateTimeUtil.class, ExamUtil.class, HtmlUtil.class})
public interface ExamPaperBuildMapping {


    /**
     * To exam paper build page response vm exam paper build page response vm.
     *
     * @param examPaperBuild the exam paper build
     * @return the exam paper build page response vm
     */
    @Mappings({
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaperBuild.getCreateTime()))"),
            @Mapping(target = "score", expression = "java(ExamUtil.scoreToVM(examPaperBuild.getScore()))"),
            @Mapping(target = "passScore", expression = "java(ExamUtil.scoreToVM(examPaperBuild.getPassScore()))"),
            @Mapping(target = "limitStartTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaperBuild.getLimitStartTime()))"),
            @Mapping(target = "limitEndTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaperBuild.getLimitEndTime()))"),
            @Mapping(target = "suggestTimeStr", expression = "java(ExamUtil.minToVM(examPaperBuild.getSuggestTime()))"),
    })
    ExamPaperBuildPageResponseVM toExamPaperBuildPageResponseVM(ExamPaperBuild examPaperBuild);


    /**
     * To exam paper page response vm exam paper page response vm.
     *
     * @param examPaperBuild the exam paper build
     * @return the exam paper page response vm
     */
    @Mappings({
            @Mapping(target = "limitStartTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaperBuild.getLimitStartTime()))"),
            @Mapping(target = "limitEndTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaperBuild.getLimitEndTime()))"),
    })
    ExamPaperPageResponseVM toExamPaperPageResponseVM(ExamPaperBuild examPaperBuild);


    /**
     * To paper info vm paper info vm.
     *
     * @param examPaperBuild the exam paper build
     * @return the paper info vm
     */
    @Mappings({
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaperBuild.getCreateTime()))"),
            @Mapping(target = "score", expression = "java(ExamUtil.scoreToVM(examPaperBuild.getScore()))"),
            @Mapping(target = "passScore", expression = "java(ExamUtil.scoreToVM(examPaperBuild.getPassScore()))"),
            @Mapping(target = "limitStartTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaperBuild.getLimitStartTime()))"),
            @Mapping(target = "limitEndTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaperBuild.getLimitEndTime()))"),
            @Mapping(target = "suggestTimeStr", expression = "java(ExamUtil.minToVM(examPaperBuild.getSuggestTime()))"),
    })
    PaperInfoVM toPaperInfoVM(ExamPaperBuild examPaperBuild);


    /**
     * To camera info vm camera info vm.
     *
     * @param examPaperBuild the exam paper build
     * @return the camera info vm
     */
    @Mappings({
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaperBuild.getCreateTime()))"),
            @Mapping(target = "score", expression = "java(ExamUtil.scoreToVM(examPaperBuild.getScore()))"),
            @Mapping(target = "passScore", expression = "java(ExamUtil.scoreToVM(examPaperBuild.getPassScore()))"),
            @Mapping(target = "publishTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaperBuild.getPublishTime()))"),
            @Mapping(target = "limitStartTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaperBuild.getLimitStartTime()))"),
            @Mapping(target = "limitEndTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaperBuild.getLimitEndTime()))"),
            @Mapping(target = "suggestTimeStr", expression = "java(ExamUtil.minToVM(examPaperBuild.getSuggestTime()))"),
    })
    CameraInfoVM toCameraInfoVM(ExamPaperBuild examPaperBuild);


    /**
     * Map camera info vm.
     *
     * @param examPaperAnswer the exam paper answer
     * @param cameraInfoVM    the camera info vm
     */
    @InheritConfiguration
    @Mappings({
            @Mapping(target = "submitTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaperAnswer.getCreateTime()))"),
            @Mapping(target = "systemScore", expression = "java(ExamUtil.scoreToVM(examPaperAnswer.getSystemScore()))"),
            @Mapping(target = "userScore", expression = "java(ExamUtil.scoreToVM(examPaperAnswer.getUserScore()))"),
            @Mapping(target = "doTime", expression = "java(ExamUtil.secondToVM(examPaperAnswer.getDoTime()))"),
            @Mapping(target = "limitStartTime", ignore = true),
            @Mapping(target = "limitEndTime", ignore = true),
            @Mapping(target = "createTime", ignore = true)
    })
    void mapCameraInfoVM(ExamPaperAnswer examPaperAnswer, @MappingTarget CameraInfoVM cameraInfoVM);


    @InheritConfiguration
    @Mappings({
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaperAnswerMonitor.getCreateTime()))"),
            @Mapping(target = "updateTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaperAnswerMonitor.getUpdateTime()))"),
            @Mapping(target = "systemScore", expression = "java(ExamUtil.scoreToVM(examPaperAnswerMonitor.getSystemScore()))"),
            @Mapping(target = "userScore", expression = "java(ExamUtil.scoreToVM(examPaperAnswerMonitor.getUserScore()))"),
            @Mapping(target = "doTime", expression = "java(ExamUtil.secondToVM(examPaperAnswerMonitor.getDoTime()))")
    })
    void mapCameraInfoVM(ExamPaperAnswerMonitor examPaperAnswerMonitor, @MappingTarget CameraInfoVM cameraInfoVM);


    /**
     * To exam paper build exam paper build.
     *
     * @param examPaperBuildEditRequestVM the exam paper build edit request vm
     * @return the exam paper build
     */
    @Mappings({
            @Mapping(target = "score", expression = "java(ExamUtil.scoreFromVM(examPaperBuildEditRequestVM.getSumScore()))"),
            @Mapping(target = "passScore", expression = "java(ExamUtil.scoreFromVM(examPaperBuildEditRequestVM.getPassScore()))"),
            @Mapping(target = "limitStartTime", ignore = true),
            @Mapping(target = "limitEndTime", ignore = true)
    })
    ExamPaperBuild toExamPaperBuild(ExamPaperBuildEditRequestVM examPaperBuildEditRequestVM);


    /**
     * Map exam paper build.
     *
     * @param examPaperBuildEditRequestVM the exam paper build edit request vm
     * @param examPaperBuild              the exam paper build
     */
    @InheritConfiguration
    @Mappings({
            @Mapping(target = "score", expression = "java(ExamUtil.scoreFromVM(examPaperBuildEditRequestVM.getSumScore()))"),
            @Mapping(target = "passScore", expression = "java(ExamUtil.scoreFromVM(examPaperBuildEditRequestVM.getPassScore()))")
    })
    void mapExamPaperBuild(ExamPaperBuildEditRequestVM examPaperBuildEditRequestVM, @MappingTarget ExamPaperBuild examPaperBuild);


    /**
     * To exam paper build edit request vm exam paper build edit request vm.
     *
     * @param examPaperBuild the exam paper build
     * @return the exam paper build edit request vm
     */
    @Mappings({
            @Mapping(target = "sumScore", expression = "java(ExamUtil.scoreToVM(examPaperBuild.getScore()))"),
            @Mapping(target = "passScore", expression = "java(ExamUtil.scoreToVM(examPaperBuild.getPassScore()))"),
    })
    ExamPaperBuildEditRequestVM toExamPaperBuildEditRequestVM(ExamPaperBuild examPaperBuild);

    /**
     * To exam paper build title exam paper build title.
     *
     * @param examPaperTitleItemVM the exam paper title item vm
     * @return the exam paper build title
     */
    @Mappings({
            @Mapping(target = "questionItems", ignore = true)
    })
    ExamPaperBuildTitle toExamPaperBuildTitle(ExamPaperTitleItemVM examPaperTitleItemVM);

    /**
     * To exam paper build question exam paper build question.
     *
     * @param questionEditRequestVM the question edit request vm
     * @return the exam paper build question
     */
    ExamPaperBuildQuestion toExamPaperBuildQuestion(QuestionEditRequestVM questionEditRequestVM);

    /**
     * To exam paper title item vm exam paper title item vm.
     *
     * @param examPaperBuildTitle the exam paper build title
     * @return the exam paper title item vm
     */
    @Mappings({
            @Mapping(target = "questionItems", ignore = true)
    })
    ExamPaperTitleItemVM toExamPaperTitleItemVM(ExamPaperBuildTitle examPaperBuildTitle);


    /**
     * To exam paper item question frame exam paper item question frame.
     *
     * @param questionRandomItem the question random item
     * @return the exam paper item question frame
     */
    ExamPaperItemQuestionFrame toExamPaperItemQuestionFrame(QuestionRandomItem questionRandomItem);

}
