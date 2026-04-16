package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.frame.ExamPaperItemFrame;
import com.mindskip.wdd.domain.frame.ExamPaperItemQuestionFrame;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareEditRequestVM;
import com.mindskip.wdd.viewmodel.course.ware.CourseWarePageResponseVM;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareQuestionVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperTitleItemVM;
import com.mindskip.wdd.viewmodel.question.QuestionEditRequestVM;
import com.mindskip.wdd.viewmodel.train.exam.answer.TrainPaperAnswerPageResponseVM;
import com.mindskip.wdd.viewmodel.train.exam.paper.TrainExamPaperEditVM;
import com.mindskip.wdd.viewmodel.train.exam.paper.TrainExamPaperPageRequestVM;
import com.mindskip.wdd.viewmodel.train.exam.paper.TrainExamPaperPageResponseVM;
import org.mapstruct.*;

import java.util.List;


/**
 * @version 9.0.0
 * @description: 培训试卷
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Mapper(componentModel = "spring", imports = {DateTimeUtil.class, ExamUtil.class})
public interface TrainExamPaperMapping {

    /**
     * @param trainExamPaper
     * @return {@link TrainExamPaperPageResponseVM}
     */
    @Mappings({
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(trainExamPaper.getCreateTime()))"),
            @Mapping(target = "suggestTimeStr", expression = "java(ExamUtil.minToVM(trainExamPaper.getSuggestTime()))"),
            @Mapping(target = "score", expression = "java(ExamUtil.scoreToVM(trainExamPaper.getScore()))"),
    })
    TrainExamPaperPageResponseVM toTrainExamPaperPageResponseVM(TrainExamPaper trainExamPaper);


    /**
     * @param trainExamPaper
     * @return {@link TrainExamPaperEditVM}
     */
    @Mappings({
            @Mapping(target = "score", expression = "java(ExamUtil.scoreToVM(trainExamPaper.getScore()))"),
    })
    TrainExamPaperEditVM toTrainExamPaperEditVM(TrainExamPaper trainExamPaper);


    /**
     * @param trainExamPaperEditVM
     * @return {@link TrainExamPaper}
     */
    TrainExamPaper toTrainExamPaper(TrainExamPaperEditVM trainExamPaperEditVM);


    /**
     * @param trainExamPaperEditVM
     * @param trainExamPaper
     */
    @InheritConfiguration
    @Mappings({@Mapping(target = "score", expression = "java(ExamUtil.scoreFromVM(trainExamPaperEditVM.getScore()))")})
    void toTrainExamPaper(TrainExamPaperEditVM trainExamPaperEditVM, @MappingTarget TrainExamPaper trainExamPaper);


    /**
     * To exam paper item frame exam paper item frame.
     *
     * @param examPaperTitleItemVM the exam paper title item vm
     * @return the exam paper item frame
     */
    ExamPaperItemFrame toExamPaperItemFrame(ExamPaperTitleItemVM examPaperTitleItemVM);


    /**
     * To exam paper item question frame exam paper item question frame.
     *
     * @param questionEditRequestVM the question edit request vm
     * @return the exam paper item question frame
     */
    @Mappings({@Mapping(target = "trickScore", source = "score")})
    ExamPaperItemQuestionFrame toExamPaperItemQuestionFrame(QuestionEditRequestVM questionEditRequestVM);

    /**
     * To exam paper item question frame list list.
     *
     * @param questionEditRequestVMList the question edit request vm list
     * @return the list
     */
    List<ExamPaperItemQuestionFrame> toExamPaperItemQuestionFrameList(List<QuestionEditRequestVM> questionEditRequestVMList);


    TrainPaperAnswerPageResponseVM toTrainPaperAnswerPageResponseVM(TrainExamPaperAnswer trainExamPaperAnswer);


    @Mappings({
            @Mapping(target = "paperId", source = "id")
    })
    ExamPaperCache toExamPaperCache(TrainExamPaper trainExamPaper);


    @Mappings({
            @Mapping(target = "userScore", expression = "java(ExamUtil.scoreToVM(trainExamPaperAnswer.getUserScore()))"),
            @Mapping(target = "paperScore", expression = "java(ExamUtil.scoreToVM(trainExamPaperAnswer.getPaperScore()))"),
            @Mapping(target = "doTimeStr", expression = "java(ExamUtil.secondToVM(trainExamPaperAnswer.getDoTime()))"),
    })
    ExamPaperAnswerInfoResponseVM toExamPaperAnswerInfoResponseVM(TrainExamPaperAnswer trainExamPaperAnswer);
}
