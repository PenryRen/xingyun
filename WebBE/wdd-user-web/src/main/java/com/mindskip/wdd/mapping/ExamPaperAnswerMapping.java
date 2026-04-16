package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ExamPaper;
import com.mindskip.wdd.domain.ExamPaperAnswer;
import com.mindskip.wdd.domain.ExamPaperAnswerMonitor;
import com.mindskip.wdd.domain.ExamPaperCache;
import com.mindskip.wdd.domain.frame.CustomerQuestionAnswerFrame;
import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerPageResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @version 1.7.0
 * @description: The interface Exam paper answer mapping.
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = {DateTimeUtil.class, ExamUtil.class})
public interface ExamPaperAnswerMapping {

    /**
     * To exam paper answer page response vm exam paper answer page response vm.
     *
     * @param examPaperAnswer the exam paper answer
     * @return the exam paper answer page response vm
     */
    @Mappings({
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(examPaperAnswer.getCreateTime()))"),
            @Mapping(target = "passScore", expression = "java(ExamUtil.scoreToVM(examPaperAnswer.getPassScore()))"),
            @Mapping(target = "systemScore", expression = "java(ExamUtil.scoreToVM(examPaperAnswer.getSystemScore()))"),
            @Mapping(target = "userScore", expression = "java(ExamUtil.scoreToVM(examPaperAnswer.getUserScore()))"),
            @Mapping(target = "paperScore", expression = "java(ExamUtil.scoreToVM(examPaperAnswer.getPaperScore()))"),
            @Mapping(target = "doTime", expression = "java(ExamUtil.secondToVM(examPaperAnswer.getDoTime()))"),
    })
    ExamPaperAnswerPageResponseVM toExamPaperAnswerPageResponseVM(ExamPaperAnswer examPaperAnswer);


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


    /**
     * To exam paper answer exam paper answer.
     *
     * @param examPaper the exam paper
     * @return the exam paper answer
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "examPaperId", source = "id"),
            @Mapping(target = "paperName", source = "name"),
            @Mapping(target = "paperScore", source = "score"),
            @Mapping(target = "createUser", ignore = true),
            @Mapping(target = "createTime", ignore = true),
    })
    ExamPaperAnswer toExamPaperAnswer(ExamPaper examPaper);


    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "examPaperId", source = "id"),
            @Mapping(target = "paperName", source = "name"),
            @Mapping(target = "paperScore", source = "score"),
            @Mapping(target = "createUser", ignore = true),
            @Mapping(target = "createTime", ignore = true),
    })
    ExamPaperAnswer toExamPaperAnswer(ExamPaperCache examPaperCache);


    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "examPaperId", source = "id"),
            @Mapping(target = "paperName", source = "name"),
            @Mapping(target = "paperScore", source = "score"),
            @Mapping(target = "createUser", ignore = true),
            @Mapping(target = "createTime", ignore = true),
    })
    ExamPaperAnswerMonitor toExamPaperAnswerMonitor(ExamPaper examPaper);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "examPaperId", source = "id"),
            @Mapping(target = "paperName", source = "name"),
            @Mapping(target = "paperScore", source = "score"),
            @Mapping(target = "createUser", ignore = true),
            @Mapping(target = "createTime", ignore = true),
    })
    ExamPaperAnswerMonitor toExamPaperAnswerMonitor(ExamPaperCache examPaperCache);

    /**
     * To exam paper answer exam paper answer.
     *
     * @param examPaperAnswerInfoResponseVM the exam paper answer info response vm
     * @return the exam paper answer
     */
    @Mappings({
            @Mapping(target = "paperScore", ignore = true),
            @Mapping(target = "userScore", ignore = true),
            @Mapping(target = "doTime", ignore = true)
    })
    ExamPaperAnswer toExamPaperAnswer(ExamPaperAnswerInfoResponseVM examPaperAnswerInfoResponseVM);


    /**
     * To customer question answer frame customer question answer frame.
     *
     * @param questionAnswerFrame the question answer frame
     * @return the customer question answer frame
     */
    CustomerQuestionAnswerFrame toCustomerQuestionAnswerFrame(QuestionAnswerFrame questionAnswerFrame);
}
