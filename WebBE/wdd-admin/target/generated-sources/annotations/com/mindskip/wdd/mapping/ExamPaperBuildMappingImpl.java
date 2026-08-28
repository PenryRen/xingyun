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
import java.text.SimpleDateFormat;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:40:32+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ExamPaperBuildMappingImpl implements ExamPaperBuildMapping {

    @Override
    public ExamPaperBuildPageResponseVM toExamPaperBuildPageResponseVM(ExamPaperBuild examPaperBuild) {
        if ( examPaperBuild == null ) {
            return null;
        }

        ExamPaperBuildPageResponseVM examPaperBuildPageResponseVM = new ExamPaperBuildPageResponseVM();

        examPaperBuildPageResponseVM.setBuildStatus( examPaperBuild.getBuildStatus() );
        examPaperBuildPageResponseVM.setBuildType( examPaperBuild.getBuildType() );
        examPaperBuildPageResponseVM.setId( examPaperBuild.getId() );
        examPaperBuildPageResponseVM.setName( examPaperBuild.getName() );
        examPaperBuildPageResponseVM.setQuestionCount( examPaperBuild.getQuestionCount() );
        examPaperBuildPageResponseVM.setSuggestTime( examPaperBuild.getSuggestTime() );

        examPaperBuildPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(examPaperBuild.getCreateTime()) );
        examPaperBuildPageResponseVM.setScore( ExamUtil.scoreToVM(examPaperBuild.getScore()) );
        examPaperBuildPageResponseVM.setPassScore( ExamUtil.scoreToVM(examPaperBuild.getPassScore()) );
        examPaperBuildPageResponseVM.setLimitStartTime( DateTimeUtil.dateTimeFullFormat(examPaperBuild.getLimitStartTime()) );
        examPaperBuildPageResponseVM.setLimitEndTime( DateTimeUtil.dateTimeFullFormat(examPaperBuild.getLimitEndTime()) );
        examPaperBuildPageResponseVM.setSuggestTimeStr( ExamUtil.minToVM(examPaperBuild.getSuggestTime()) );

        return examPaperBuildPageResponseVM;
    }

    @Override
    public ExamPaperPageResponseVM toExamPaperPageResponseVM(ExamPaperBuild examPaperBuild) {
        if ( examPaperBuild == null ) {
            return null;
        }

        ExamPaperPageResponseVM examPaperPageResponseVM = new ExamPaperPageResponseVM();

        examPaperPageResponseVM.setBuildType( examPaperBuild.getBuildType() );
        examPaperPageResponseVM.setCredentialTemplateId( examPaperBuild.getCredentialTemplateId() );
        examPaperPageResponseVM.setId( examPaperBuild.getId() );
        examPaperPageResponseVM.setName( examPaperBuild.getName() );

        examPaperPageResponseVM.setLimitStartTime( DateTimeUtil.dateTimeFullFormat(examPaperBuild.getLimitStartTime()) );
        examPaperPageResponseVM.setLimitEndTime( DateTimeUtil.dateTimeFullFormat(examPaperBuild.getLimitEndTime()) );

        return examPaperPageResponseVM;
    }

    @Override
    public PaperInfoVM toPaperInfoVM(ExamPaperBuild examPaperBuild) {
        if ( examPaperBuild == null ) {
            return null;
        }

        PaperInfoVM paperInfoVM = new PaperInfoVM();

        paperInfoVM.setBuildType( examPaperBuild.getBuildType() );
        paperInfoVM.setExamPaperArchiveId( examPaperBuild.getExamPaperArchiveId() );
        paperInfoVM.setId( examPaperBuild.getId() );
        paperInfoVM.setName( examPaperBuild.getName() );
        if ( examPaperBuild.getPublishTime() != null ) {
            paperInfoVM.setPublishTime( new SimpleDateFormat().format( examPaperBuild.getPublishTime() ) );
        }
        paperInfoVM.setQuestionCount( examPaperBuild.getQuestionCount() );
        paperInfoVM.setSuggestTime( examPaperBuild.getSuggestTime() );

        paperInfoVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(examPaperBuild.getCreateTime()) );
        paperInfoVM.setScore( ExamUtil.scoreToVM(examPaperBuild.getScore()) );
        paperInfoVM.setPassScore( ExamUtil.scoreToVM(examPaperBuild.getPassScore()) );
        paperInfoVM.setLimitStartTime( DateTimeUtil.dateTimeFullFormat(examPaperBuild.getLimitStartTime()) );
        paperInfoVM.setLimitEndTime( DateTimeUtil.dateTimeFullFormat(examPaperBuild.getLimitEndTime()) );
        paperInfoVM.setSuggestTimeStr( ExamUtil.minToVM(examPaperBuild.getSuggestTime()) );

        return paperInfoVM;
    }

    @Override
    public CameraInfoVM toCameraInfoVM(ExamPaperBuild examPaperBuild) {
        if ( examPaperBuild == null ) {
            return null;
        }

        CameraInfoVM cameraInfoVM = new CameraInfoVM();

        cameraInfoVM.setBuildType( examPaperBuild.getBuildType() );
        cameraInfoVM.setExamPaperArchiveId( examPaperBuild.getExamPaperArchiveId() );
        cameraInfoVM.setId( examPaperBuild.getId() );
        cameraInfoVM.setName( examPaperBuild.getName() );
        cameraInfoVM.setQuestionCount( examPaperBuild.getQuestionCount() );
        cameraInfoVM.setSuggestTime( examPaperBuild.getSuggestTime() );

        cameraInfoVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(examPaperBuild.getCreateTime()) );
        cameraInfoVM.setScore( ExamUtil.scoreToVM(examPaperBuild.getScore()) );
        cameraInfoVM.setPassScore( ExamUtil.scoreToVM(examPaperBuild.getPassScore()) );
        cameraInfoVM.setPublishTime( DateTimeUtil.dateTimeFullFormat(examPaperBuild.getPublishTime()) );
        cameraInfoVM.setLimitStartTime( DateTimeUtil.dateTimeFullFormat(examPaperBuild.getLimitStartTime()) );
        cameraInfoVM.setLimitEndTime( DateTimeUtil.dateTimeFullFormat(examPaperBuild.getLimitEndTime()) );
        cameraInfoVM.setSuggestTimeStr( ExamUtil.minToVM(examPaperBuild.getSuggestTime()) );

        return cameraInfoVM;
    }

    @Override
    public void mapCameraInfoVM(ExamPaperAnswer examPaperAnswer, CameraInfoVM cameraInfoVM) {
        if ( examPaperAnswer == null ) {
            return;
        }

        cameraInfoVM.setExamPaperArchiveId( examPaperAnswer.getExamPaperArchiveId() );
        cameraInfoVM.setId( examPaperAnswer.getId() );
        if ( examPaperAnswer.getPassScore() != null ) {
            cameraInfoVM.setPassScore( String.valueOf( examPaperAnswer.getPassScore() ) );
        }
        else {
            cameraInfoVM.setPassScore( null );
        }
        cameraInfoVM.setPassed( examPaperAnswer.getPassed() );
        cameraInfoVM.setQuestionCount( examPaperAnswer.getQuestionCount() );

        cameraInfoVM.setSubmitTime( DateTimeUtil.dateTimeFullFormat(examPaperAnswer.getCreateTime()) );
        cameraInfoVM.setSystemScore( ExamUtil.scoreToVM(examPaperAnswer.getSystemScore()) );
        cameraInfoVM.setUserScore( ExamUtil.scoreToVM(examPaperAnswer.getUserScore()) );
        cameraInfoVM.setDoTime( ExamUtil.secondToVM(examPaperAnswer.getDoTime()) );
    }

    @Override
    public void mapCameraInfoVM(ExamPaperAnswerMonitor examPaperAnswerMonitor, CameraInfoVM cameraInfoVM) {
        if ( examPaperAnswerMonitor == null ) {
            return;
        }

        cameraInfoVM.setCheatCount( examPaperAnswerMonitor.getCheatCount() );
        cameraInfoVM.setExamPaperArchiveId( examPaperAnswerMonitor.getExamPaperArchiveId() );
        cameraInfoVM.setId( examPaperAnswerMonitor.getId() );
        cameraInfoVM.setQuestionCount( examPaperAnswerMonitor.getQuestionCount() );

        cameraInfoVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(examPaperAnswerMonitor.getCreateTime()) );
        cameraInfoVM.setUpdateTime( DateTimeUtil.dateTimeFullFormat(examPaperAnswerMonitor.getUpdateTime()) );
        cameraInfoVM.setSystemScore( ExamUtil.scoreToVM(examPaperAnswerMonitor.getSystemScore()) );
        cameraInfoVM.setUserScore( ExamUtil.scoreToVM(examPaperAnswerMonitor.getUserScore()) );
        cameraInfoVM.setDoTime( ExamUtil.secondToVM(examPaperAnswerMonitor.getDoTime()) );
    }

    @Override
    public ExamPaperBuild toExamPaperBuild(ExamPaperBuildEditRequestVM examPaperBuildEditRequestVM) {
        if ( examPaperBuildEditRequestVM == null ) {
            return null;
        }

        ExamPaperBuild examPaperBuild = new ExamPaperBuild();

        examPaperBuild.setBuildConfig( examPaperBuildEditRequestVM.getBuildConfig() );
        examPaperBuild.setBuildStatus( examPaperBuildEditRequestVM.getBuildStatus() );
        examPaperBuild.setBuildType( examPaperBuildEditRequestVM.getBuildType() );
        examPaperBuild.setCredentialTemplateId( examPaperBuildEditRequestVM.getCredentialTemplateId() );
        examPaperBuild.setExamPaperArchiveId( examPaperBuildEditRequestVM.getExamPaperArchiveId() );
        examPaperBuild.setId( examPaperBuildEditRequestVM.getId() );
        examPaperBuild.setName( examPaperBuildEditRequestVM.getName() );
        examPaperBuild.setQuestionCount( examPaperBuildEditRequestVM.getQuestionCount() );
        examPaperBuild.setRangeType( examPaperBuildEditRequestVM.getRangeType() );
        examPaperBuild.setSuggestTime( examPaperBuildEditRequestVM.getSuggestTime() );
        examPaperBuild.setVmType( examPaperBuildEditRequestVM.getVmType() );

        examPaperBuild.setScore( ExamUtil.scoreFromVM(examPaperBuildEditRequestVM.getSumScore()) );
        examPaperBuild.setPassScore( ExamUtil.scoreFromVM(examPaperBuildEditRequestVM.getPassScore()) );

        return examPaperBuild;
    }

    @Override
    public void mapExamPaperBuild(ExamPaperBuildEditRequestVM examPaperBuildEditRequestVM, ExamPaperBuild examPaperBuild) {
        if ( examPaperBuildEditRequestVM == null ) {
            return;
        }

        examPaperBuild.setBuildConfig( examPaperBuildEditRequestVM.getBuildConfig() );
        examPaperBuild.setBuildStatus( examPaperBuildEditRequestVM.getBuildStatus() );
        examPaperBuild.setBuildType( examPaperBuildEditRequestVM.getBuildType() );
        examPaperBuild.setCredentialTemplateId( examPaperBuildEditRequestVM.getCredentialTemplateId() );
        examPaperBuild.setExamPaperArchiveId( examPaperBuildEditRequestVM.getExamPaperArchiveId() );
        examPaperBuild.setId( examPaperBuildEditRequestVM.getId() );
        examPaperBuild.setName( examPaperBuildEditRequestVM.getName() );
        examPaperBuild.setQuestionCount( examPaperBuildEditRequestVM.getQuestionCount() );
        examPaperBuild.setRangeType( examPaperBuildEditRequestVM.getRangeType() );
        examPaperBuild.setSuggestTime( examPaperBuildEditRequestVM.getSuggestTime() );
        examPaperBuild.setVmType( examPaperBuildEditRequestVM.getVmType() );

        examPaperBuild.setScore( ExamUtil.scoreFromVM(examPaperBuildEditRequestVM.getSumScore()) );
        examPaperBuild.setPassScore( ExamUtil.scoreFromVM(examPaperBuildEditRequestVM.getPassScore()) );
    }

    @Override
    public ExamPaperBuildEditRequestVM toExamPaperBuildEditRequestVM(ExamPaperBuild examPaperBuild) {
        if ( examPaperBuild == null ) {
            return null;
        }

        ExamPaperBuildEditRequestVM examPaperBuildEditRequestVM = new ExamPaperBuildEditRequestVM();

        examPaperBuildEditRequestVM.setBuildConfig( examPaperBuild.getBuildConfig() );
        examPaperBuildEditRequestVM.setBuildStatus( examPaperBuild.getBuildStatus() );
        examPaperBuildEditRequestVM.setBuildType( examPaperBuild.getBuildType() );
        examPaperBuildEditRequestVM.setCredentialTemplateId( examPaperBuild.getCredentialTemplateId() );
        examPaperBuildEditRequestVM.setExamPaperArchiveId( examPaperBuild.getExamPaperArchiveId() );
        examPaperBuildEditRequestVM.setId( examPaperBuild.getId() );
        examPaperBuildEditRequestVM.setName( examPaperBuild.getName() );
        examPaperBuildEditRequestVM.setQuestionCount( examPaperBuild.getQuestionCount() );
        examPaperBuildEditRequestVM.setRangeType( examPaperBuild.getRangeType() );
        examPaperBuildEditRequestVM.setSuggestTime( examPaperBuild.getSuggestTime() );
        examPaperBuildEditRequestVM.setVmType( examPaperBuild.getVmType() );

        examPaperBuildEditRequestVM.setSumScore( ExamUtil.scoreToVM(examPaperBuild.getScore()) );
        examPaperBuildEditRequestVM.setPassScore( ExamUtil.scoreToVM(examPaperBuild.getPassScore()) );

        return examPaperBuildEditRequestVM;
    }

    @Override
    public ExamPaperBuildTitle toExamPaperBuildTitle(ExamPaperTitleItemVM examPaperTitleItemVM) {
        if ( examPaperTitleItemVM == null ) {
            return null;
        }

        ExamPaperBuildTitle examPaperBuildTitle = new ExamPaperBuildTitle();

        examPaperBuildTitle.setName( examPaperTitleItemVM.getName() );

        return examPaperBuildTitle;
    }

    @Override
    public ExamPaperBuildQuestion toExamPaperBuildQuestion(QuestionEditRequestVM questionEditRequestVM) {
        if ( questionEditRequestVM == null ) {
            return null;
        }

        ExamPaperBuildQuestion examPaperBuildQuestion = new ExamPaperBuildQuestion();

        examPaperBuildQuestion.setId( questionEditRequestVM.getId() );
        examPaperBuildQuestion.setQuestionFrameId( questionEditRequestVM.getQuestionFrameId() );
        examPaperBuildQuestion.setScore( questionEditRequestVM.getScore() );

        return examPaperBuildQuestion;
    }

    @Override
    public ExamPaperTitleItemVM toExamPaperTitleItemVM(ExamPaperBuildTitle examPaperBuildTitle) {
        if ( examPaperBuildTitle == null ) {
            return null;
        }

        ExamPaperTitleItemVM examPaperTitleItemVM = new ExamPaperTitleItemVM();

        examPaperTitleItemVM.setName( examPaperBuildTitle.getName() );

        return examPaperTitleItemVM;
    }

    @Override
    public ExamPaperItemQuestionFrame toExamPaperItemQuestionFrame(QuestionRandomItem questionRandomItem) {
        if ( questionRandomItem == null ) {
            return null;
        }

        ExamPaperItemQuestionFrame examPaperItemQuestionFrame = new ExamPaperItemQuestionFrame();

        examPaperItemQuestionFrame.setId( questionRandomItem.getId() );
        examPaperItemQuestionFrame.setQuestionFrameId( questionRandomItem.getQuestionFrameId() );

        return examPaperItemQuestionFrame;
    }
}
