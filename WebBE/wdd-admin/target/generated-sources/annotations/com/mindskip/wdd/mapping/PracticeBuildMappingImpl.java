package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ExamPaperCache;
import com.mindskip.wdd.domain.PracticeBuild;
import com.mindskip.wdd.domain.PracticeExamPaper;
import com.mindskip.wdd.domain.PracticeExamPaperAnswer;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.HtmlUtil;
import com.mindskip.wdd.viewmodel.answer.PaperInfoVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildEditRequestVM;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildPageResponseVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:40:32+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class PracticeBuildMappingImpl implements PracticeBuildMapping {

    @Override
    public ExamPaperBuildPageResponseVM toExamPaperBuildPageResponseVM(PracticeBuild practiceBuild) {
        if ( practiceBuild == null ) {
            return null;
        }

        ExamPaperBuildPageResponseVM examPaperBuildPageResponseVM = new ExamPaperBuildPageResponseVM();

        examPaperBuildPageResponseVM.setBuildType( practiceBuild.getBuildType() );
        examPaperBuildPageResponseVM.setId( practiceBuild.getId() );
        examPaperBuildPageResponseVM.setName( practiceBuild.getName() );
        if ( practiceBuild.getPassScore() != null ) {
            examPaperBuildPageResponseVM.setPassScore( String.valueOf( practiceBuild.getPassScore() ) );
        }
        examPaperBuildPageResponseVM.setQuestionCount( practiceBuild.getQuestionCount() );
        examPaperBuildPageResponseVM.setSuggestTime( practiceBuild.getSuggestTime() );

        examPaperBuildPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(practiceBuild.getCreateTime()) );
        examPaperBuildPageResponseVM.setScore( ExamUtil.scoreToVM(practiceBuild.getScore()) );
        examPaperBuildPageResponseVM.setSuggestTimeStr( ExamUtil.minToVM(practiceBuild.getSuggestTime()) );
        examPaperBuildPageResponseVM.setLimitStartTime( DateTimeUtil.dateTimeFullFormat(practiceBuild.getLimitStartTime()) );
        examPaperBuildPageResponseVM.setLimitEndTime( DateTimeUtil.dateTimeFullFormat(practiceBuild.getLimitEndTime()) );

        return examPaperBuildPageResponseVM;
    }

    @Override
    public ExamPaperBuildEditRequestVM toExamPaperBuildEditRequestVM(PracticeBuild practiceBuild) {
        if ( practiceBuild == null ) {
            return null;
        }

        ExamPaperBuildEditRequestVM examPaperBuildEditRequestVM = new ExamPaperBuildEditRequestVM();

        examPaperBuildEditRequestVM.setBuildConfig( practiceBuild.getBuildConfig() );
        examPaperBuildEditRequestVM.setBuildType( practiceBuild.getBuildType() );
        examPaperBuildEditRequestVM.setExamPaperArchiveId( practiceBuild.getExamPaperArchiveId() );
        examPaperBuildEditRequestVM.setId( practiceBuild.getId() );
        examPaperBuildEditRequestVM.setName( practiceBuild.getName() );
        examPaperBuildEditRequestVM.setQuestionCount( practiceBuild.getQuestionCount() );
        examPaperBuildEditRequestVM.setSuggestTime( practiceBuild.getSuggestTime() );

        examPaperBuildEditRequestVM.setPassScore( ExamUtil.scoreToVM(practiceBuild.getPassScore()) );
        examPaperBuildEditRequestVM.setSumScore( ExamUtil.scoreToVM(practiceBuild.getScore()) );

        return examPaperBuildEditRequestVM;
    }

    @Override
    public PracticeBuild toPracticeBuild(ExamPaperBuildEditRequestVM examPaperBuildEditRequestVM) {
        if ( examPaperBuildEditRequestVM == null ) {
            return null;
        }

        PracticeBuild practiceBuild = new PracticeBuild();

        practiceBuild.setBuildConfig( examPaperBuildEditRequestVM.getBuildConfig() );
        practiceBuild.setBuildType( examPaperBuildEditRequestVM.getBuildType() );
        practiceBuild.setExamPaperArchiveId( examPaperBuildEditRequestVM.getExamPaperArchiveId() );
        practiceBuild.setId( examPaperBuildEditRequestVM.getId() );
        practiceBuild.setName( examPaperBuildEditRequestVM.getName() );
        practiceBuild.setQuestionCount( examPaperBuildEditRequestVM.getQuestionCount() );
        practiceBuild.setSuggestTime( examPaperBuildEditRequestVM.getSuggestTime() );

        practiceBuild.setScore( ExamUtil.scoreFromVM(examPaperBuildEditRequestVM.getSumScore()) );
        practiceBuild.setPassScore( ExamUtil.scoreFromVM(examPaperBuildEditRequestVM.getPassScore()) );

        return practiceBuild;
    }

    @Override
    public void mapPracticeBuild(ExamPaperBuildEditRequestVM examPaperBuildEditRequestVM, PracticeBuild practiceBuild) {
        if ( examPaperBuildEditRequestVM == null ) {
            return;
        }

        practiceBuild.setBuildConfig( examPaperBuildEditRequestVM.getBuildConfig() );
        practiceBuild.setBuildType( examPaperBuildEditRequestVM.getBuildType() );
        practiceBuild.setExamPaperArchiveId( examPaperBuildEditRequestVM.getExamPaperArchiveId() );
        practiceBuild.setId( examPaperBuildEditRequestVM.getId() );
        practiceBuild.setName( examPaperBuildEditRequestVM.getName() );
        practiceBuild.setQuestionCount( examPaperBuildEditRequestVM.getQuestionCount() );
        practiceBuild.setSuggestTime( examPaperBuildEditRequestVM.getSuggestTime() );

        practiceBuild.setScore( ExamUtil.scoreFromVM(examPaperBuildEditRequestVM.getSumScore()) );
        practiceBuild.setPassScore( ExamUtil.scoreFromVM(examPaperBuildEditRequestVM.getPassScore()) );
    }

    @Override
    public PracticeExamPaper toPracticeExamPaper(PracticeBuild practiceBuild) {
        if ( practiceBuild == null ) {
            return null;
        }

        PracticeExamPaper practiceExamPaper = new PracticeExamPaper();

        practiceExamPaper.setCreateDepartmentId( practiceBuild.getCreateDepartmentId() );
        practiceExamPaper.setCreateTime( practiceBuild.getCreateTime() );
        practiceExamPaper.setCreateUser( practiceBuild.getCreateUser() );
        practiceExamPaper.setDeleted( practiceBuild.getDeleted() );
        practiceExamPaper.setExamPaperArchiveId( practiceBuild.getExamPaperArchiveId() );
        practiceExamPaper.setLimitEndTime( practiceBuild.getLimitEndTime() );
        practiceExamPaper.setLimitStartTime( practiceBuild.getLimitStartTime() );
        practiceExamPaper.setName( practiceBuild.getName() );
        practiceExamPaper.setPassScore( practiceBuild.getPassScore() );
        practiceExamPaper.setQuestionCount( practiceBuild.getQuestionCount() );
        practiceExamPaper.setScore( practiceBuild.getScore() );
        practiceExamPaper.setSuggestTime( practiceBuild.getSuggestTime() );

        return practiceExamPaper;
    }

    @Override
    public void mapPracticeExamPaper(PracticeBuild practiceBuild, PracticeExamPaper practiceExamPaper) {
        if ( practiceBuild == null ) {
            return;
        }

        practiceExamPaper.setCreateDepartmentId( practiceBuild.getCreateDepartmentId() );
        practiceExamPaper.setCreateTime( practiceBuild.getCreateTime() );
        practiceExamPaper.setCreateUser( practiceBuild.getCreateUser() );
        practiceExamPaper.setDeleted( practiceBuild.getDeleted() );
        practiceExamPaper.setExamPaperArchiveId( practiceBuild.getExamPaperArchiveId() );
        practiceExamPaper.setLimitEndTime( practiceBuild.getLimitEndTime() );
        practiceExamPaper.setLimitStartTime( practiceBuild.getLimitStartTime() );
        practiceExamPaper.setName( practiceBuild.getName() );
        practiceExamPaper.setPassScore( practiceBuild.getPassScore() );
        practiceExamPaper.setQuestionCount( practiceBuild.getQuestionCount() );
        practiceExamPaper.setScore( practiceBuild.getScore() );
        practiceExamPaper.setSuggestTime( practiceBuild.getSuggestTime() );
    }

    @Override
    public PaperInfoVM toPaperInfoVM(PracticeBuild practiceBuild) {
        if ( practiceBuild == null ) {
            return null;
        }

        PaperInfoVM paperInfoVM = new PaperInfoVM();

        paperInfoVM.setBuildType( practiceBuild.getBuildType() );
        paperInfoVM.setExamPaperArchiveId( practiceBuild.getExamPaperArchiveId() );
        paperInfoVM.setId( practiceBuild.getId() );
        paperInfoVM.setName( practiceBuild.getName() );
        paperInfoVM.setQuestionCount( practiceBuild.getQuestionCount() );
        paperInfoVM.setSuggestTime( practiceBuild.getSuggestTime() );

        paperInfoVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(practiceBuild.getCreateTime()) );
        paperInfoVM.setScore( ExamUtil.scoreToVM(practiceBuild.getScore()) );
        paperInfoVM.setPassScore( ExamUtil.scoreToVM(practiceBuild.getPassScore()) );
        paperInfoVM.setLimitStartTime( DateTimeUtil.dateTimeFullFormat(practiceBuild.getLimitStartTime()) );
        paperInfoVM.setLimitEndTime( DateTimeUtil.dateTimeFullFormat(practiceBuild.getLimitEndTime()) );
        paperInfoVM.setSuggestTimeStr( ExamUtil.minToVM(practiceBuild.getSuggestTime()) );

        return paperInfoVM;
    }

    @Override
    public ExamPaperAnswerInfoResponseVM toExamPaperAnswerInfoResponseVM(PracticeExamPaperAnswer practiceExamPaperAnswer) {
        if ( practiceExamPaperAnswer == null ) {
            return null;
        }

        ExamPaperAnswerInfoResponseVM examPaperAnswerInfoResponseVM = new ExamPaperAnswerInfoResponseVM();

        examPaperAnswerInfoResponseVM.setCreateTime( practiceExamPaperAnswer.getCreateTime() );
        examPaperAnswerInfoResponseVM.setCreateUser( practiceExamPaperAnswer.getCreateUser() );
        examPaperAnswerInfoResponseVM.setDoTime( practiceExamPaperAnswer.getDoTime() );
        examPaperAnswerInfoResponseVM.setId( practiceExamPaperAnswer.getId() );
        examPaperAnswerInfoResponseVM.setJudgeUser( practiceExamPaperAnswer.getJudgeUser() );
        examPaperAnswerInfoResponseVM.setPaperType( practiceExamPaperAnswer.getPaperType() );
        examPaperAnswerInfoResponseVM.setPassed( practiceExamPaperAnswer.getPassed() );
        examPaperAnswerInfoResponseVM.setStatus( practiceExamPaperAnswer.getStatus() );

        examPaperAnswerInfoResponseVM.setUserScore( ExamUtil.scoreToVM(practiceExamPaperAnswer.getUserScore()) );
        examPaperAnswerInfoResponseVM.setPaperScore( ExamUtil.scoreToVM(practiceExamPaperAnswer.getPaperScore()) );
        examPaperAnswerInfoResponseVM.setDoTimeStr( ExamUtil.secondToVM(practiceExamPaperAnswer.getDoTime()) );
        examPaperAnswerInfoResponseVM.setPassScore( ExamUtil.scoreToVM(practiceExamPaperAnswer.getPassScore()) );

        return examPaperAnswerInfoResponseVM;
    }

    @Override
    public ExamPaperCache toExamPaperCache(PracticeExamPaper practiceExamPaper) {
        if ( practiceExamPaper == null ) {
            return null;
        }

        ExamPaperCache examPaperCache = new ExamPaperCache();

        examPaperCache.setPaperId( practiceExamPaper.getId() );
        examPaperCache.setCreateDepartmentId( practiceExamPaper.getCreateDepartmentId() );
        examPaperCache.setCreateTime( practiceExamPaper.getCreateTime() );
        examPaperCache.setCreateUser( practiceExamPaper.getCreateUser() );
        examPaperCache.setDeleted( practiceExamPaper.getDeleted() );
        examPaperCache.setExamPaperArchiveId( practiceExamPaper.getExamPaperArchiveId() );
        examPaperCache.setId( practiceExamPaper.getId() );
        examPaperCache.setLimitEndTime( practiceExamPaper.getLimitEndTime() );
        examPaperCache.setLimitStartTime( practiceExamPaper.getLimitStartTime() );
        examPaperCache.setName( practiceExamPaper.getName() );
        examPaperCache.setPaperType( practiceExamPaper.getPaperType() );
        examPaperCache.setPassScore( practiceExamPaper.getPassScore() );
        examPaperCache.setPracticeBuildId( practiceExamPaper.getPracticeBuildId() );
        examPaperCache.setQuestionCount( practiceExamPaper.getQuestionCount() );
        examPaperCache.setScore( practiceExamPaper.getScore() );
        examPaperCache.setSuggestTime( practiceExamPaper.getSuggestTime() );

        return examPaperCache;
    }
}
