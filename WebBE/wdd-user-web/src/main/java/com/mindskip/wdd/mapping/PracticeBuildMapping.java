package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.frame.ExamPaperItemQuestionFrame;
import com.mindskip.wdd.domain.other.QuestionRandomItem;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.HtmlUtil;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerPageResponseVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperPageResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


/**
 * @version 9.5.0
 * @description: 模拟练习对象映射
 * Copyright (C), 2024, 麒技团队
 * @date 2024/09/11 10:45
 */
@Mapper(componentModel = "spring", imports = {DateTimeUtil.class, ExamUtil.class, HtmlUtil.class})
public interface PracticeBuildMapping {

    @Mappings({
            @Mapping(target = "score", expression = "java(ExamUtil.scoreToVM(practiceBuild.getScore()))"),
            @Mapping(target = "passScore", expression = "java(ExamUtil.scoreToVM(practiceBuild.getPassScore()))"),
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(practiceBuild.getCreateTime()))"),
            @Mapping(target = "limitStartTime", expression = "java(DateTimeUtil.dateTimeFullFormat(practiceBuild.getLimitStartTime()))"),
            @Mapping(target = "limitEndTime", expression = "java(DateTimeUtil.dateTimeFullFormat(practiceBuild.getLimitEndTime()))"),
            @Mapping(target = "suggestTimeStr", expression = "java(ExamUtil.minToVM(practiceBuild.getSuggestTime()))"),
    })
    ExamPaperPageResponseVM toExamPaperPageResponseVM(PracticeBuild practiceBuild);


    @Mappings({
            @Mapping(target = "paperId", source = "id")
    })
    ExamPaperCache toExamPaperCache(PracticeExamPaper practiceExamPaper);


    @Mappings({
            @Mapping(target = "id", ignore = true)
    })
    PracticeExamPaper toPracticeExamPaper(PracticeBuild practiceBuild);


    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "practiceExamPaperId", source = "id"),
            @Mapping(target = "paperName", source = "name"),
            @Mapping(target = "paperScore", source = "score"),
            @Mapping(target = "createUser", ignore = true),
            @Mapping(target = "createTime", ignore = true),
    })
    PracticeExamPaperAnswer toExamPaperAnswer(ExamPaperCache examPaperCache);



    @Mappings({
            @Mapping(target = "userScore", expression = "java(ExamUtil.scoreToVM(practiceExamPaperAnswer.getUserScore()))"),
            @Mapping(target = "paperScore", expression = "java(ExamUtil.scoreToVM(practiceExamPaperAnswer.getPaperScore()))"),
            @Mapping(target = "doTimeStr", expression = "java(ExamUtil.secondToVM(practiceExamPaperAnswer.getDoTime()))"),
            @Mapping(target = "passScore", expression = "java(ExamUtil.scoreToVM(practiceExamPaperAnswer.getPassScore()))"),
    })
    ExamPaperAnswerInfoResponseVM toExamPaperAnswerInfoResponseVM(PracticeExamPaperAnswer practiceExamPaperAnswer);


    @Mappings({
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(practiceExamPaperAnswer.getCreateTime()))"),
            @Mapping(target = "passScore", expression = "java(ExamUtil.scoreToVM(practiceExamPaperAnswer.getPassScore()))"),
            @Mapping(target = "systemScore", expression = "java(ExamUtil.scoreToVM(practiceExamPaperAnswer.getSystemScore()))"),
            @Mapping(target = "userScore", expression = "java(ExamUtil.scoreToVM(practiceExamPaperAnswer.getUserScore()))"),
            @Mapping(target = "paperScore", expression = "java(ExamUtil.scoreToVM(practiceExamPaperAnswer.getPaperScore()))"),
            @Mapping(target = "doTime", expression = "java(ExamUtil.secondToVM(practiceExamPaperAnswer.getDoTime()))"),
    })
    ExamPaperAnswerPageResponseVM toExamPaperAnswerPageResponseVM(PracticeExamPaperAnswer practiceExamPaperAnswer);

}
