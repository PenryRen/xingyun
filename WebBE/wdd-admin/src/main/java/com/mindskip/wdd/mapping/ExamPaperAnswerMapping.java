package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ExamPaper;
import com.mindskip.wdd.domain.ExamPaperAnswer;
import com.mindskip.wdd.domain.ExamPaperAnswerMonitor;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.monitor.PaperMonitorPageResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


/**
 * @version 1.7.0
 * @description: ExamPaperAnswerMapping
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = {DateTimeUtil.class, ExamUtil.class})
public interface ExamPaperAnswerMapping {


    /**
     * To exam paper answer info response vm exam paper answer info response vm.
     *
     * @param examPaperAnswer the exam paper answer
     * @return the exam paper answer info response vm
     */
    @Mappings({
            @Mapping(target = "userScore", expression = "java(ExamUtil.scoreToVM(examPaperAnswer.getUserScore()))"),
            @Mapping(target = "paperScore", expression = "java(ExamUtil.scoreToVM(examPaperAnswer.getPaperScore()))"),
            @Mapping(target = "doTimeStr", expression = "java(ExamUtil.secondToVM(examPaperAnswer.getDoTime()))"),
    })
    ExamPaperAnswerInfoResponseVM toExamPaperAnswerInfoResponseVM(ExamPaperAnswer examPaperAnswer);

    @Mappings({
            @Mapping(target = "userScore", expression = "java(ExamUtil.scoreToVM(examPaperAnswerMonitor.getUserScore()))"),
            @Mapping(target = "paperScore", expression = "java(ExamUtil.scoreToVM(examPaperAnswerMonitor.getPaperScore()))"),
            @Mapping(target = "doTimeStr", expression = "java(ExamUtil.secondToVM(examPaperAnswerMonitor.getDoTime()))"),
    })
    ExamPaperAnswerInfoResponseVM toExamPaperAnswerInfoResponseVM(ExamPaperAnswerMonitor examPaperAnswerMonitor);

    @Mappings({
            @Mapping(target = "userScore", expression = "java(ExamUtil.scoreToVM(examPaperAnswerMonitor.getUserScore()))"),
            @Mapping(target = "paperScore", expression = "java(ExamUtil.scoreToVM(examPaperAnswerMonitor.getPaperScore()))"),
            @Mapping(target = "doTimeStr", expression = "java(ExamUtil.secondToVM(examPaperAnswerMonitor.getDoTime()))"),
            @Mapping(target = "updateTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaperAnswerMonitor.getUpdateTime()))"),
    })
    PaperMonitorPageResponseVM toPaperMonitorPageResponseVM(ExamPaperAnswerMonitor examPaperAnswerMonitor);


    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "examPaperId", source = "id"),
            @Mapping(target = "paperName", source = "name"),
            @Mapping(target = "paperScore", source = "score"),
            @Mapping(target = "createUser", ignore = true),
            @Mapping(target = "createTime", ignore = true),
    })
    ExamPaperAnswer toExamPaperAnswer(ExamPaper examPaper);
}
