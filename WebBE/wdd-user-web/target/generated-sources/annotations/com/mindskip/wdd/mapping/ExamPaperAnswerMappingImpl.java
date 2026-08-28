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
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:40:39+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ExamPaperAnswerMappingImpl implements ExamPaperAnswerMapping {

    @Override
    public ExamPaperAnswerPageResponseVM toExamPaperAnswerPageResponseVM(ExamPaperAnswer examPaperAnswer) {
        if ( examPaperAnswer == null ) {
            return null;
        }

        ExamPaperAnswerPageResponseVM examPaperAnswerPageResponseVM = new ExamPaperAnswerPageResponseVM();

        examPaperAnswerPageResponseVM.setId( examPaperAnswer.getId() );
        examPaperAnswerPageResponseVM.setPaperName( examPaperAnswer.getPaperName() );
        examPaperAnswerPageResponseVM.setPassed( examPaperAnswer.getPassed() );
        examPaperAnswerPageResponseVM.setQuestionCorrect( examPaperAnswer.getQuestionCorrect() );
        examPaperAnswerPageResponseVM.setQuestionCount( examPaperAnswer.getQuestionCount() );
        examPaperAnswerPageResponseVM.setStatus( examPaperAnswer.getStatus() );
        examPaperAnswerPageResponseVM.setWatch( examPaperAnswer.getWatch() );

        examPaperAnswerPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(examPaperAnswer.getCreateTime()) );
        examPaperAnswerPageResponseVM.setPassScore( ExamUtil.scoreToVM(examPaperAnswer.getPassScore()) );
        examPaperAnswerPageResponseVM.setSystemScore( ExamUtil.scoreToVM(examPaperAnswer.getSystemScore()) );
        examPaperAnswerPageResponseVM.setUserScore( ExamUtil.scoreToVM(examPaperAnswer.getUserScore()) );
        examPaperAnswerPageResponseVM.setPaperScore( ExamUtil.scoreToVM(examPaperAnswer.getPaperScore()) );
        examPaperAnswerPageResponseVM.setDoTime( ExamUtil.secondToVM(examPaperAnswer.getDoTime()) );

        return examPaperAnswerPageResponseVM;
    }

    @Override
    public ExamPaperAnswerInfoResponseVM toExamPaperAnswerInfoResponseVM(ExamPaperAnswer examPaperAnswer) {
        if ( examPaperAnswer == null ) {
            return null;
        }

        ExamPaperAnswerInfoResponseVM examPaperAnswerInfoResponseVM = new ExamPaperAnswerInfoResponseVM();

        examPaperAnswerInfoResponseVM.setCreateUser( examPaperAnswer.getCreateUser() );
        examPaperAnswerInfoResponseVM.setDoTime( examPaperAnswer.getDoTime() );
        examPaperAnswerInfoResponseVM.setExamPaperId( examPaperAnswer.getExamPaperId() );
        examPaperAnswerInfoResponseVM.setId( examPaperAnswer.getId() );
        examPaperAnswerInfoResponseVM.setPaperType( examPaperAnswer.getPaperType() );
        if ( examPaperAnswer.getPassScore() != null ) {
            examPaperAnswerInfoResponseVM.setPassScore( String.valueOf( examPaperAnswer.getPassScore() ) );
        }
        examPaperAnswerInfoResponseVM.setPassed( examPaperAnswer.getPassed() );
        examPaperAnswerInfoResponseVM.setStatus( examPaperAnswer.getStatus() );

        examPaperAnswerInfoResponseVM.setUserScore( ExamUtil.scoreToVM(examPaperAnswer.getUserScore()) );
        examPaperAnswerInfoResponseVM.setPaperScore( ExamUtil.scoreToVM(examPaperAnswer.getPaperScore()) );
        examPaperAnswerInfoResponseVM.setDoTimeStr( ExamUtil.secondToVM(examPaperAnswer.getDoTime()) );

        return examPaperAnswerInfoResponseVM;
    }

    @Override
    public ExamPaperAnswer toExamPaperAnswer(ExamPaper examPaper) {
        if ( examPaper == null ) {
            return null;
        }

        ExamPaperAnswer examPaperAnswer = new ExamPaperAnswer();

        examPaperAnswer.setExamPaperId( examPaper.getId() );
        examPaperAnswer.setPaperName( examPaper.getName() );
        examPaperAnswer.setPaperScore( examPaper.getScore() );
        examPaperAnswer.setCreateDepartmentId( examPaper.getCreateDepartmentId() );
        examPaperAnswer.setCredentialTemplateId( examPaper.getCredentialTemplateId() );
        examPaperAnswer.setDeleted( examPaper.getDeleted() );
        examPaperAnswer.setExamPaperArchiveId( examPaper.getExamPaperArchiveId() );
        examPaperAnswer.setExamPaperBuildId( examPaper.getExamPaperBuildId() );
        examPaperAnswer.setLimitEndTime( examPaper.getLimitEndTime() );
        examPaperAnswer.setLimitStartTime( examPaper.getLimitStartTime() );
        examPaperAnswer.setPaperType( examPaper.getPaperType() );
        examPaperAnswer.setPassScore( examPaper.getPassScore() );
        examPaperAnswer.setQuestionCount( examPaper.getQuestionCount() );
        examPaperAnswer.setWatch( examPaper.getWatch() );

        return examPaperAnswer;
    }

    @Override
    public ExamPaperAnswer toExamPaperAnswer(ExamPaperCache examPaperCache) {
        if ( examPaperCache == null ) {
            return null;
        }

        ExamPaperAnswer examPaperAnswer = new ExamPaperAnswer();

        examPaperAnswer.setExamPaperId( examPaperCache.getId() );
        examPaperAnswer.setPaperName( examPaperCache.getName() );
        examPaperAnswer.setPaperScore( examPaperCache.getScore() );
        examPaperAnswer.setCreateDepartmentId( examPaperCache.getCreateDepartmentId() );
        examPaperAnswer.setCredentialTemplateId( examPaperCache.getCredentialTemplateId() );
        examPaperAnswer.setDeleted( examPaperCache.getDeleted() );
        examPaperAnswer.setExamPaperArchiveId( examPaperCache.getExamPaperArchiveId() );
        examPaperAnswer.setExamPaperBuildId( examPaperCache.getExamPaperBuildId() );
        examPaperAnswer.setLimitEndTime( examPaperCache.getLimitEndTime() );
        examPaperAnswer.setLimitStartTime( examPaperCache.getLimitStartTime() );
        examPaperAnswer.setPaperType( examPaperCache.getPaperType() );
        examPaperAnswer.setPassScore( examPaperCache.getPassScore() );
        examPaperAnswer.setQuestionCount( examPaperCache.getQuestionCount() );
        examPaperAnswer.setWatch( examPaperCache.getWatch() );

        return examPaperAnswer;
    }

    @Override
    public ExamPaperAnswerMonitor toExamPaperAnswerMonitor(ExamPaper examPaper) {
        if ( examPaper == null ) {
            return null;
        }

        ExamPaperAnswerMonitor examPaperAnswerMonitor = new ExamPaperAnswerMonitor();

        examPaperAnswerMonitor.setExamPaperId( examPaper.getId() );
        examPaperAnswerMonitor.setPaperName( examPaper.getName() );
        examPaperAnswerMonitor.setPaperScore( examPaper.getScore() );
        examPaperAnswerMonitor.setCreateDepartmentId( examPaper.getCreateDepartmentId() );
        examPaperAnswerMonitor.setExamPaperArchiveId( examPaper.getExamPaperArchiveId() );
        examPaperAnswerMonitor.setExamPaperBuildId( examPaper.getExamPaperBuildId() );
        examPaperAnswerMonitor.setPaperType( examPaper.getPaperType() );
        examPaperAnswerMonitor.setQuestionCount( examPaper.getQuestionCount() );

        return examPaperAnswerMonitor;
    }

    @Override
    public ExamPaperAnswerMonitor toExamPaperAnswerMonitor(ExamPaperCache examPaperCache) {
        if ( examPaperCache == null ) {
            return null;
        }

        ExamPaperAnswerMonitor examPaperAnswerMonitor = new ExamPaperAnswerMonitor();

        examPaperAnswerMonitor.setExamPaperId( examPaperCache.getId() );
        examPaperAnswerMonitor.setPaperName( examPaperCache.getName() );
        examPaperAnswerMonitor.setPaperScore( examPaperCache.getScore() );
        examPaperAnswerMonitor.setCreateDepartmentId( examPaperCache.getCreateDepartmentId() );
        examPaperAnswerMonitor.setExamPaperArchiveId( examPaperCache.getExamPaperArchiveId() );
        examPaperAnswerMonitor.setExamPaperBuildId( examPaperCache.getExamPaperBuildId() );
        examPaperAnswerMonitor.setPaperType( examPaperCache.getPaperType() );
        examPaperAnswerMonitor.setQuestionCount( examPaperCache.getQuestionCount() );

        return examPaperAnswerMonitor;
    }

    @Override
    public ExamPaperAnswer toExamPaperAnswer(ExamPaperAnswerInfoResponseVM examPaperAnswerInfoResponseVM) {
        if ( examPaperAnswerInfoResponseVM == null ) {
            return null;
        }

        ExamPaperAnswer examPaperAnswer = new ExamPaperAnswer();

        examPaperAnswer.setCreateUser( examPaperAnswerInfoResponseVM.getCreateUser() );
        examPaperAnswer.setExamPaperId( examPaperAnswerInfoResponseVM.getExamPaperId() );
        examPaperAnswer.setId( examPaperAnswerInfoResponseVM.getId() );
        examPaperAnswer.setPaperType( examPaperAnswerInfoResponseVM.getPaperType() );
        if ( examPaperAnswerInfoResponseVM.getPassScore() != null ) {
            examPaperAnswer.setPassScore( Integer.parseInt( examPaperAnswerInfoResponseVM.getPassScore() ) );
        }
        examPaperAnswer.setPassed( examPaperAnswerInfoResponseVM.getPassed() );
        examPaperAnswer.setStatus( examPaperAnswerInfoResponseVM.getStatus() );

        return examPaperAnswer;
    }

    @Override
    public CustomerQuestionAnswerFrame toCustomerQuestionAnswerFrame(QuestionAnswerFrame questionAnswerFrame) {
        if ( questionAnswerFrame == null ) {
            return null;
        }

        CustomerQuestionAnswerFrame customerQuestionAnswerFrame = new CustomerQuestionAnswerFrame();

        customerQuestionAnswerFrame.setCompleted( questionAnswerFrame.getCompleted() );
        customerQuestionAnswerFrame.setCustomerScore( questionAnswerFrame.getCustomerScore() );
        customerQuestionAnswerFrame.setCustomerScoreVM( questionAnswerFrame.getCustomerScoreVM() );
        customerQuestionAnswerFrame.setDoRight( questionAnswerFrame.getDoRight() );
        customerQuestionAnswerFrame.setId( questionAnswerFrame.getId() );
        customerQuestionAnswerFrame.setItemOrder( questionAnswerFrame.getItemOrder() );
        customerQuestionAnswerFrame.setJudgeScoreVM( questionAnswerFrame.getJudgeScoreVM() );
        customerQuestionAnswerFrame.setQuestionFrameId( questionAnswerFrame.getQuestionFrameId() );
        customerQuestionAnswerFrame.setQuestionId( questionAnswerFrame.getQuestionId() );
        List<Integer> list = questionAnswerFrame.getQuestionItemKeyOrder();
        if ( list != null ) {
            customerQuestionAnswerFrame.setQuestionItemKeyOrder( new ArrayList<Integer>( list ) );
        }

        return customerQuestionAnswerFrame;
    }
}
