package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ExamPaper;
import com.mindskip.wdd.domain.ExamPaperAnswer;
import com.mindskip.wdd.domain.ExamPaperAnswerMonitor;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.monitor.PaperMonitorPageResponseVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:40:32+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ExamPaperAnswerMappingImpl implements ExamPaperAnswerMapping {

    @Override
    public ExamPaperAnswerInfoResponseVM toExamPaperAnswerInfoResponseVM(ExamPaperAnswer examPaperAnswer) {
        if ( examPaperAnswer == null ) {
            return null;
        }

        ExamPaperAnswerInfoResponseVM examPaperAnswerInfoResponseVM = new ExamPaperAnswerInfoResponseVM();

        examPaperAnswerInfoResponseVM.setCreateTime( examPaperAnswer.getCreateTime() );
        examPaperAnswerInfoResponseVM.setCreateUser( examPaperAnswer.getCreateUser() );
        examPaperAnswerInfoResponseVM.setDoTime( examPaperAnswer.getDoTime() );
        examPaperAnswerInfoResponseVM.setExamPaperId( examPaperAnswer.getExamPaperId() );
        examPaperAnswerInfoResponseVM.setId( examPaperAnswer.getId() );
        examPaperAnswerInfoResponseVM.setJudgeUser( examPaperAnswer.getJudgeUser() );
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
    public ExamPaperAnswerInfoResponseVM toExamPaperAnswerInfoResponseVM(ExamPaperAnswerMonitor examPaperAnswerMonitor) {
        if ( examPaperAnswerMonitor == null ) {
            return null;
        }

        ExamPaperAnswerInfoResponseVM examPaperAnswerInfoResponseVM = new ExamPaperAnswerInfoResponseVM();

        examPaperAnswerInfoResponseVM.setCreateTime( examPaperAnswerMonitor.getCreateTime() );
        examPaperAnswerInfoResponseVM.setCreateUser( examPaperAnswerMonitor.getCreateUser() );
        examPaperAnswerInfoResponseVM.setDoTime( examPaperAnswerMonitor.getDoTime() );
        examPaperAnswerInfoResponseVM.setExamPaperId( examPaperAnswerMonitor.getExamPaperId() );
        examPaperAnswerInfoResponseVM.setId( examPaperAnswerMonitor.getId() );
        examPaperAnswerInfoResponseVM.setPaperType( examPaperAnswerMonitor.getPaperType() );

        examPaperAnswerInfoResponseVM.setUserScore( ExamUtil.scoreToVM(examPaperAnswerMonitor.getUserScore()) );
        examPaperAnswerInfoResponseVM.setPaperScore( ExamUtil.scoreToVM(examPaperAnswerMonitor.getPaperScore()) );
        examPaperAnswerInfoResponseVM.setDoTimeStr( ExamUtil.secondToVM(examPaperAnswerMonitor.getDoTime()) );

        return examPaperAnswerInfoResponseVM;
    }

    @Override
    public PaperMonitorPageResponseVM toPaperMonitorPageResponseVM(ExamPaperAnswerMonitor examPaperAnswerMonitor) {
        if ( examPaperAnswerMonitor == null ) {
            return null;
        }

        PaperMonitorPageResponseVM paperMonitorPageResponseVM = new PaperMonitorPageResponseVM();

        paperMonitorPageResponseVM.setCheatCount( examPaperAnswerMonitor.getCheatCount() );
        paperMonitorPageResponseVM.setCreateUser( examPaperAnswerMonitor.getCreateUser() );
        paperMonitorPageResponseVM.setDoTime( examPaperAnswerMonitor.getDoTime() );
        paperMonitorPageResponseVM.setId( examPaperAnswerMonitor.getId() );
        paperMonitorPageResponseVM.setPaperName( examPaperAnswerMonitor.getPaperName() );
        paperMonitorPageResponseVM.setQuestionCorrect( examPaperAnswerMonitor.getQuestionCorrect() );
        paperMonitorPageResponseVM.setQuestionCount( examPaperAnswerMonitor.getQuestionCount() );

        paperMonitorPageResponseVM.setUserScore( ExamUtil.scoreToVM(examPaperAnswerMonitor.getUserScore()) );
        paperMonitorPageResponseVM.setPaperScore( ExamUtil.scoreToVM(examPaperAnswerMonitor.getPaperScore()) );
        paperMonitorPageResponseVM.setDoTimeStr( ExamUtil.secondToVM(examPaperAnswerMonitor.getDoTime()) );
        paperMonitorPageResponseVM.setUpdateTime( DateTimeUtil.dateTimeFullFormat(examPaperAnswerMonitor.getUpdateTime()) );

        return paperMonitorPageResponseVM;
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
}
