package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Question;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import com.mindskip.wdd.domain.frame.QuestionItemFrame;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.HtmlUtil;
import com.mindskip.wdd.viewmodel.excel.*;
import com.mindskip.wdd.viewmodel.question.QuestionEditItemVM;
import com.mindskip.wdd.viewmodel.question.QuestionEditRequestVM;
import com.mindskip.wdd.viewmodel.question.QuestionPageResponseVM;
import org.mapstruct.*;

import java.util.List;


/**
 * @version 1.7.0
 * @description: QuestionMapping
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = {DateTimeUtil.class, ExamUtil.class, HtmlUtil.class})
public interface QuestionMapping {


    /**
     * To question response vm question page response vm.
     *
     * @param question the question
     * @return the question page response vm
     */
    @Mappings({
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(question.getCreateTime()))"),
            @Mapping(target = "score", expression = "java(ExamUtil.scoreToVM(question.getScore()))"),
    })
    QuestionPageResponseVM toQuestionResponseVM(Question question);


    /**
     * To question question.
     *
     * @param questionEditRequestVM the question edit request vm
     * @return the question
     */
    @Mappings({@Mapping(target = "score", expression = "java(ExamUtil.scoreFromVM(questionEditRequestVM.getScore()))")})
    Question toQuestion(QuestionEditRequestVM questionEditRequestVM);

    /**
     * To question edit request vm question edit request vm.
     *
     * @param question the question
     * @return the question edit request vm
     */
    @Mappings({@Mapping(target = "score", expression = "java(ExamUtil.scoreToVM(question.getScore()))")})
    QuestionEditRequestVM toQuestionEditRequestVM(Question question);

    /**
     * To question.
     *
     * @param questionEditRequestVM the question edit request vm
     * @param question              the question
     */
    @InheritConfiguration
    void toQuestion(QuestionEditRequestVM questionEditRequestVM, @MappingTarget Question question);


    /**
     * To question frame question frame.
     *
     * @param questionEditRequestVM the question edit request vm
     * @return the question frame
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "score", expression = "java(ExamUtil.scoreFromVM(questionEditRequestVM.getScore()))"),
            @Mapping(target = "correct", ignore = true)
    })
    QuestionFrame toQuestionFrame(QuestionEditRequestVM questionEditRequestVM);

    /**
     * To question frame.
     *
     * @param questionEditRequestVM the question edit request vm
     * @param questionFrame         the question frame
     */
    @InheritConfiguration
    @Mappings({
            @Mapping(target = "score", expression = "java(ExamUtil.scoreFromVM(questionEditRequestVM.getScore()))"),
            @Mapping(target = "correct", ignore = true)
    })
    void toQuestionFrame(QuestionEditRequestVM questionEditRequestVM, @MappingTarget QuestionFrame questionFrame);

    /**
     * To question edit request vm.
     *
     * @param questionFrame         the question frame
     * @param questionEditRequestVM the question edit request vm
     */
    @InheritConfiguration
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "score", expression = "java(ExamUtil.scoreToVM(questionFrame.getScore()))"),
            @Mapping(target = "correct", ignore = true)
    })
    void toQuestionEditRequestVM(QuestionFrame questionFrame, @MappingTarget QuestionEditRequestVM questionEditRequestVM);


    /**
     * To question edit request vm question edit request vm.
     *
     * @param singleChoiceVM the single choice vm
     * @return the question edit request vm
     */
    QuestionEditRequestVM toQuestionEditRequestVM(SingleChoiceVM singleChoiceVM);


    /**
     * To question edit request vm question edit request vm.
     *
     * @param multipleVM the multiple vm
     * @return the question edit request vm
     */
    QuestionEditRequestVM toQuestionEditRequestVM(MultipleVM multipleVM);


    /**
     * To question edit request vm question edit request vm.
     *
     * @param trueFalseVM the true false vm
     * @return the question edit request vm
     */
    QuestionEditRequestVM toQuestionEditRequestVM(TrueFalseVM trueFalseVM);

    /**
     * To question edit request vm question edit request vm.
     *
     * @param gapFillingVM the gap filling vm
     * @return the question edit request vm
     */
    QuestionEditRequestVM toQuestionEditRequestVM(GapFillingVM gapFillingVM);

    /**
     * To question edit request vm question edit request vm.
     *
     * @param shortAnswerVM the short answer vm
     * @return the question edit request vm
     */
    QuestionEditRequestVM toQuestionEditRequestVM(ShortAnswerVM shortAnswerVM);

    /**
     * To question item frame list list.
     *
     * @param questionEditItemVMList the question edit item vm list
     * @return the list
     */
    List<QuestionItemFrame> toQuestionItemFrameList(List<QuestionEditItemVM> questionEditItemVMList);

    /**
     * To question item frame question item frame.
     *
     * @param questionEditItemVM the question edit item vm
     * @return the question item frame
     */
    @Mappings({@Mapping(target = "score", expression = "java(ExamUtil.scoreFromVM(questionEditItemVM.getScore()))")})
    QuestionItemFrame toQuestionItemFrame(QuestionEditItemVM questionEditItemVM);

    /**
     * To question edit item vm list list.
     *
     * @param questionItemFrameList the question item frame list
     * @return the list
     */
    List<QuestionEditItemVM> toQuestionEditItemVMList(List<QuestionItemFrame> questionItemFrameList);

    /**
     * To question edit item vm question edit item vm.
     *
     * @param questionItemFrame the question item frame
     * @return the question edit item vm
     */
    @Mappings({@Mapping(target = "score", expression = "java(ExamUtil.scoreToVM(questionItemFrame.getScore()))")})
    QuestionEditItemVM toQuestionEditItemVM(QuestionItemFrame questionItemFrame);

    /**
     * To single choice vm single choice vm.
     *
     * @param questionCommonVM the question common vm
     * @return the single choice vm
     */
    SingleChoiceVM toSingleChoiceVM(QuestionCommonVM questionCommonVM);

    /**
     * To multiple vm multiple vm.
     *
     * @param questionCommonVM the question common vm
     * @return the multiple vm
     */
    MultipleVM toMultipleVM(QuestionCommonVM questionCommonVM);

    /**
     * To true false vm true false vm.
     *
     * @param questionCommonVM the question common vm
     * @return the true false vm
     */
    TrueFalseVM toTrueFalseVM(QuestionCommonVM questionCommonVM);

    /**
     * To gap filling vm gap filling vm.
     *
     * @param questionCommonVM the question common vm
     * @return the gap filling vm
     */
    GapFillingVM toGapFillingVM(QuestionCommonVM questionCommonVM);

    /**
     * To short answer vm short answer vm.
     *
     * @param questionCommonVM the question common vm
     * @return the short answer vm
     */
    ShortAnswerVM toShortAnswerVM(QuestionCommonVM questionCommonVM);
}
