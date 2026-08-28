package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Question;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import com.mindskip.wdd.domain.frame.QuestionItemFrame;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.HtmlUtil;
import com.mindskip.wdd.viewmodel.excel.GapFillingVM;
import com.mindskip.wdd.viewmodel.excel.MultipleVM;
import com.mindskip.wdd.viewmodel.excel.QuestionCommonVM;
import com.mindskip.wdd.viewmodel.excel.ShortAnswerVM;
import com.mindskip.wdd.viewmodel.excel.SingleChoiceVM;
import com.mindskip.wdd.viewmodel.excel.TrueFalseVM;
import com.mindskip.wdd.viewmodel.question.QuestionEditItemVM;
import com.mindskip.wdd.viewmodel.question.QuestionEditRequestVM;
import com.mindskip.wdd.viewmodel.question.QuestionPageResponseVM;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:40:32+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class QuestionMappingImpl implements QuestionMapping {

    @Override
    public QuestionPageResponseVM toQuestionResponseVM(Question question) {
        if ( question == null ) {
            return null;
        }

        QuestionPageResponseVM questionPageResponseVM = new QuestionPageResponseVM();

        questionPageResponseVM.setDifficult( question.getDifficult() );
        questionPageResponseVM.setId( question.getId() );
        questionPageResponseVM.setQuestionFrameId( question.getQuestionFrameId() );
        questionPageResponseVM.setQuestionType( question.getQuestionType() );

        questionPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(question.getCreateTime()) );
        questionPageResponseVM.setScore( ExamUtil.scoreToVM(question.getScore()) );

        return questionPageResponseVM;
    }

    @Override
    public Question toQuestion(QuestionEditRequestVM questionEditRequestVM) {
        if ( questionEditRequestVM == null ) {
            return null;
        }

        Question question = new Question();

        question.setDifficult( questionEditRequestVM.getDifficult() );
        question.setId( questionEditRequestVM.getId() );
        question.setQuestionArchiveId( questionEditRequestVM.getQuestionArchiveId() );
        question.setQuestionFrameId( questionEditRequestVM.getQuestionFrameId() );
        question.setQuestionType( questionEditRequestVM.getQuestionType() );

        question.setScore( ExamUtil.scoreFromVM(questionEditRequestVM.getScore()) );

        return question;
    }

    @Override
    public QuestionEditRequestVM toQuestionEditRequestVM(Question question) {
        if ( question == null ) {
            return null;
        }

        QuestionEditRequestVM questionEditRequestVM = new QuestionEditRequestVM();

        questionEditRequestVM.setDifficult( question.getDifficult() );
        questionEditRequestVM.setId( question.getId() );
        questionEditRequestVM.setQuestionArchiveId( question.getQuestionArchiveId() );
        questionEditRequestVM.setQuestionFrameId( question.getQuestionFrameId() );
        questionEditRequestVM.setQuestionType( question.getQuestionType() );

        questionEditRequestVM.setScore( ExamUtil.scoreToVM(question.getScore()) );

        return questionEditRequestVM;
    }

    @Override
    public void toQuestion(QuestionEditRequestVM questionEditRequestVM, Question question) {
        if ( questionEditRequestVM == null ) {
            return;
        }

        question.setDifficult( questionEditRequestVM.getDifficult() );
        question.setId( questionEditRequestVM.getId() );
        question.setQuestionArchiveId( questionEditRequestVM.getQuestionArchiveId() );
        question.setQuestionFrameId( questionEditRequestVM.getQuestionFrameId() );
        question.setQuestionType( questionEditRequestVM.getQuestionType() );

        question.setScore( ExamUtil.scoreFromVM(questionEditRequestVM.getScore()) );
    }

    @Override
    public QuestionFrame toQuestionFrame(QuestionEditRequestVM questionEditRequestVM) {
        if ( questionEditRequestVM == null ) {
            return null;
        }

        QuestionFrame questionFrame = new QuestionFrame();

        questionFrame.setAnalyze( questionEditRequestVM.getAnalyze() );
        questionFrame.setCommand( questionEditRequestVM.getCommand() );
        questionFrame.setCommandType( questionEditRequestVM.getCommandType() );
        questionFrame.setDifficult( questionEditRequestVM.getDifficult() );
        questionFrame.setItemOrder( questionEditRequestVM.getItemOrder() );
        questionFrame.setQuestionArchiveId( questionEditRequestVM.getQuestionArchiveId() );
        questionFrame.setQuestionType( questionEditRequestVM.getQuestionType() );
        questionFrame.setTitle( questionEditRequestVM.getTitle() );
        questionFrame.setVmType( questionEditRequestVM.getVmType() );

        questionFrame.setScore( ExamUtil.scoreFromVM(questionEditRequestVM.getScore()) );

        return questionFrame;
    }

    @Override
    public void toQuestionFrame(QuestionEditRequestVM questionEditRequestVM, QuestionFrame questionFrame) {
        if ( questionEditRequestVM == null ) {
            return;
        }

        questionFrame.setAnalyze( questionEditRequestVM.getAnalyze() );
        questionFrame.setCommand( questionEditRequestVM.getCommand() );
        questionFrame.setCommandType( questionEditRequestVM.getCommandType() );
        questionFrame.setDifficult( questionEditRequestVM.getDifficult() );
        questionFrame.setItemOrder( questionEditRequestVM.getItemOrder() );
        questionFrame.setQuestionArchiveId( questionEditRequestVM.getQuestionArchiveId() );
        questionFrame.setQuestionType( questionEditRequestVM.getQuestionType() );
        questionFrame.setTitle( questionEditRequestVM.getTitle() );
        questionFrame.setVmType( questionEditRequestVM.getVmType() );

        questionFrame.setScore( ExamUtil.scoreFromVM(questionEditRequestVM.getScore()) );
    }

    @Override
    public void toQuestionEditRequestVM(QuestionFrame questionFrame, QuestionEditRequestVM questionEditRequestVM) {
        if ( questionFrame == null ) {
            return;
        }

        questionEditRequestVM.setAnalyze( questionFrame.getAnalyze() );
        questionEditRequestVM.setCommand( questionFrame.getCommand() );
        questionEditRequestVM.setCommandType( questionFrame.getCommandType() );
        questionEditRequestVM.setDifficult( questionFrame.getDifficult() );
        questionEditRequestVM.setItemOrder( questionFrame.getItemOrder() );
        questionEditRequestVM.setQuestionArchiveId( questionFrame.getQuestionArchiveId() );
        questionEditRequestVM.setQuestionType( questionFrame.getQuestionType() );
        questionEditRequestVM.setTitle( questionFrame.getTitle() );
        questionEditRequestVM.setVmType( questionFrame.getVmType() );

        questionEditRequestVM.setScore( ExamUtil.scoreToVM(questionFrame.getScore()) );
    }

    @Override
    public QuestionEditRequestVM toQuestionEditRequestVM(SingleChoiceVM singleChoiceVM) {
        if ( singleChoiceVM == null ) {
            return null;
        }

        QuestionEditRequestVM questionEditRequestVM = new QuestionEditRequestVM();

        questionEditRequestVM.setAnalyze( singleChoiceVM.getAnalyze() );
        questionEditRequestVM.setCorrect( singleChoiceVM.getCorrect() );
        questionEditRequestVM.setDifficult( singleChoiceVM.getDifficult() );
        questionEditRequestVM.setScore( singleChoiceVM.getScore() );
        questionEditRequestVM.setTitle( singleChoiceVM.getTitle() );

        return questionEditRequestVM;
    }

    @Override
    public QuestionEditRequestVM toQuestionEditRequestVM(MultipleVM multipleVM) {
        if ( multipleVM == null ) {
            return null;
        }

        QuestionEditRequestVM questionEditRequestVM = new QuestionEditRequestVM();

        questionEditRequestVM.setAnalyze( multipleVM.getAnalyze() );
        questionEditRequestVM.setCorrect( multipleVM.getCorrect() );
        questionEditRequestVM.setDifficult( multipleVM.getDifficult() );
        questionEditRequestVM.setScore( multipleVM.getScore() );
        questionEditRequestVM.setTitle( multipleVM.getTitle() );

        return questionEditRequestVM;
    }

    @Override
    public QuestionEditRequestVM toQuestionEditRequestVM(TrueFalseVM trueFalseVM) {
        if ( trueFalseVM == null ) {
            return null;
        }

        QuestionEditRequestVM questionEditRequestVM = new QuestionEditRequestVM();

        questionEditRequestVM.setAnalyze( trueFalseVM.getAnalyze() );
        questionEditRequestVM.setCorrect( trueFalseVM.getCorrect() );
        questionEditRequestVM.setDifficult( trueFalseVM.getDifficult() );
        questionEditRequestVM.setScore( trueFalseVM.getScore() );
        questionEditRequestVM.setTitle( trueFalseVM.getTitle() );

        return questionEditRequestVM;
    }

    @Override
    public QuestionEditRequestVM toQuestionEditRequestVM(GapFillingVM gapFillingVM) {
        if ( gapFillingVM == null ) {
            return null;
        }

        QuestionEditRequestVM questionEditRequestVM = new QuestionEditRequestVM();

        questionEditRequestVM.setAnalyze( gapFillingVM.getAnalyze() );
        questionEditRequestVM.setDifficult( gapFillingVM.getDifficult() );
        questionEditRequestVM.setScore( gapFillingVM.getScore() );
        questionEditRequestVM.setTitle( gapFillingVM.getTitle() );

        return questionEditRequestVM;
    }

    @Override
    public QuestionEditRequestVM toQuestionEditRequestVM(ShortAnswerVM shortAnswerVM) {
        if ( shortAnswerVM == null ) {
            return null;
        }

        QuestionEditRequestVM questionEditRequestVM = new QuestionEditRequestVM();

        questionEditRequestVM.setAnalyze( shortAnswerVM.getAnalyze() );
        questionEditRequestVM.setCorrect( shortAnswerVM.getCorrect() );
        questionEditRequestVM.setDifficult( shortAnswerVM.getDifficult() );
        questionEditRequestVM.setScore( shortAnswerVM.getScore() );
        questionEditRequestVM.setTitle( shortAnswerVM.getTitle() );

        return questionEditRequestVM;
    }

    @Override
    public List<QuestionItemFrame> toQuestionItemFrameList(List<QuestionEditItemVM> questionEditItemVMList) {
        if ( questionEditItemVMList == null ) {
            return null;
        }

        List<QuestionItemFrame> list = new ArrayList<QuestionItemFrame>( questionEditItemVMList.size() );
        for ( QuestionEditItemVM questionEditItemVM : questionEditItemVMList ) {
            list.add( toQuestionItemFrame( questionEditItemVM ) );
        }

        return list;
    }

    @Override
    public QuestionItemFrame toQuestionItemFrame(QuestionEditItemVM questionEditItemVM) {
        if ( questionEditItemVM == null ) {
            return null;
        }

        QuestionItemFrame questionItemFrame = new QuestionItemFrame();

        questionItemFrame.setContent( questionEditItemVM.getContent() );
        questionItemFrame.setItemUuid( questionEditItemVM.getItemUuid() );
        questionItemFrame.setPrefix( questionEditItemVM.getPrefix() );

        questionItemFrame.setScore( ExamUtil.scoreFromVM(questionEditItemVM.getScore()) );

        return questionItemFrame;
    }

    @Override
    public List<QuestionEditItemVM> toQuestionEditItemVMList(List<QuestionItemFrame> questionItemFrameList) {
        if ( questionItemFrameList == null ) {
            return null;
        }

        List<QuestionEditItemVM> list = new ArrayList<QuestionEditItemVM>( questionItemFrameList.size() );
        for ( QuestionItemFrame questionItemFrame : questionItemFrameList ) {
            list.add( toQuestionEditItemVM( questionItemFrame ) );
        }

        return list;
    }

    @Override
    public QuestionEditItemVM toQuestionEditItemVM(QuestionItemFrame questionItemFrame) {
        if ( questionItemFrame == null ) {
            return null;
        }

        QuestionEditItemVM questionEditItemVM = new QuestionEditItemVM();

        questionEditItemVM.setContent( questionItemFrame.getContent() );
        questionEditItemVM.setItemUuid( questionItemFrame.getItemUuid() );
        questionEditItemVM.setPrefix( questionItemFrame.getPrefix() );

        questionEditItemVM.setScore( ExamUtil.scoreToVM(questionItemFrame.getScore()) );

        return questionEditItemVM;
    }

    @Override
    public SingleChoiceVM toSingleChoiceVM(QuestionCommonVM questionCommonVM) {
        if ( questionCommonVM == null ) {
            return null;
        }

        SingleChoiceVM singleChoiceVM = new SingleChoiceVM();

        singleChoiceVM.setAnalyze( questionCommonVM.getAnalyze() );
        singleChoiceVM.setCorrect( questionCommonVM.getCorrect() );
        singleChoiceVM.setDifficult( questionCommonVM.getDifficult() );
        singleChoiceVM.setLevel( questionCommonVM.getLevel() );
        singleChoiceVM.setScore( questionCommonVM.getScore() );

        return singleChoiceVM;
    }

    @Override
    public MultipleVM toMultipleVM(QuestionCommonVM questionCommonVM) {
        if ( questionCommonVM == null ) {
            return null;
        }

        MultipleVM multipleVM = new MultipleVM();

        multipleVM.setAnalyze( questionCommonVM.getAnalyze() );
        multipleVM.setCorrect( questionCommonVM.getCorrect() );
        multipleVM.setDifficult( questionCommonVM.getDifficult() );
        multipleVM.setLevel( questionCommonVM.getLevel() );
        multipleVM.setScore( questionCommonVM.getScore() );

        return multipleVM;
    }

    @Override
    public TrueFalseVM toTrueFalseVM(QuestionCommonVM questionCommonVM) {
        if ( questionCommonVM == null ) {
            return null;
        }

        TrueFalseVM trueFalseVM = new TrueFalseVM();

        trueFalseVM.setAnalyze( questionCommonVM.getAnalyze() );
        trueFalseVM.setCorrect( questionCommonVM.getCorrect() );
        trueFalseVM.setDifficult( questionCommonVM.getDifficult() );
        trueFalseVM.setLevel( questionCommonVM.getLevel() );
        trueFalseVM.setScore( questionCommonVM.getScore() );

        return trueFalseVM;
    }

    @Override
    public GapFillingVM toGapFillingVM(QuestionCommonVM questionCommonVM) {
        if ( questionCommonVM == null ) {
            return null;
        }

        GapFillingVM gapFillingVM = new GapFillingVM();

        gapFillingVM.setAnalyze( questionCommonVM.getAnalyze() );
        gapFillingVM.setDifficult( questionCommonVM.getDifficult() );
        gapFillingVM.setLevel( questionCommonVM.getLevel() );
        gapFillingVM.setScore( questionCommonVM.getScore() );

        return gapFillingVM;
    }

    @Override
    public ShortAnswerVM toShortAnswerVM(QuestionCommonVM questionCommonVM) {
        if ( questionCommonVM == null ) {
            return null;
        }

        ShortAnswerVM shortAnswerVM = new ShortAnswerVM();

        shortAnswerVM.setAnalyze( questionCommonVM.getAnalyze() );
        shortAnswerVM.setCorrect( questionCommonVM.getCorrect() );
        shortAnswerVM.setDifficult( questionCommonVM.getDifficult() );
        shortAnswerVM.setLevel( questionCommonVM.getLevel() );
        shortAnswerVM.setScore( questionCommonVM.getScore() );

        return shortAnswerVM;
    }
}
