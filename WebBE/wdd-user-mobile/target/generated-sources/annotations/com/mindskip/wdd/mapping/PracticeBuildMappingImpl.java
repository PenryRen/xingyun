package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ExamPaperCache;
import com.mindskip.wdd.domain.PracticeBuild;
import com.mindskip.wdd.domain.PracticeExamPaper;
import com.mindskip.wdd.domain.PracticeExamPaperAnswer;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.HtmlUtil;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerPageResponseVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperPageResponseVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:28:30+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class PracticeBuildMappingImpl implements PracticeBuildMapping {

    @Override
    public ExamPaperPageResponseVM toExamPaperPageResponseVM(PracticeBuild practiceBuild) {
        if ( practiceBuild == null ) {
            return null;
        }

        ExamPaperPageResponseVM examPaperPageResponseVM = new ExamPaperPageResponseVM();

        examPaperPageResponseVM.setBuildType( practiceBuild.getBuildType() );
        examPaperPageResponseVM.setId( practiceBuild.getId() );
        examPaperPageResponseVM.setName( practiceBuild.getName() );
        examPaperPageResponseVM.setPracticeExamPaperId( practiceBuild.getPracticeExamPaperId() );
        examPaperPageResponseVM.setQuestionCount( practiceBuild.getQuestionCount() );
        examPaperPageResponseVM.setSuggestTime( practiceBuild.getSuggestTime() );

        examPaperPageResponseVM.setScore( ExamUtil.scoreToVM(practiceBuild.getScore()) );
        examPaperPageResponseVM.setPassScore( ExamUtil.scoreToVM(practiceBuild.getPassScore()) );
        examPaperPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(practiceBuild.getCreateTime()) );
        examPaperPageResponseVM.setLimitStartTime( DateTimeUtil.dateTimeFullFormat(practiceBuild.getLimitStartTime()) );
        examPaperPageResponseVM.setLimitEndTime( DateTimeUtil.dateTimeFullFormat(practiceBuild.getLimitEndTime()) );
        examPaperPageResponseVM.setSuggestTimeStr( ExamUtil.minToVM(practiceBuild.getSuggestTime()) );

        return examPaperPageResponseVM;
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
    public PracticeExamPaperAnswer toExamPaperAnswer(ExamPaperCache examPaperCache) {
        if ( examPaperCache == null ) {
            return null;
        }

        PracticeExamPaperAnswer practiceExamPaperAnswer = new PracticeExamPaperAnswer();

        practiceExamPaperAnswer.setPracticeExamPaperId( examPaperCache.getId() );
        practiceExamPaperAnswer.setPaperName( examPaperCache.getName() );
        practiceExamPaperAnswer.setPaperScore( examPaperCache.getScore() );
        practiceExamPaperAnswer.setCreateDepartmentId( examPaperCache.getCreateDepartmentId() );
        practiceExamPaperAnswer.setDeleted( examPaperCache.getDeleted() );
        practiceExamPaperAnswer.setExamPaperArchiveId( examPaperCache.getExamPaperArchiveId() );
        practiceExamPaperAnswer.setPaperType( examPaperCache.getPaperType() );
        practiceExamPaperAnswer.setPassScore( examPaperCache.getPassScore() );
        practiceExamPaperAnswer.setPracticeBuildId( examPaperCache.getPracticeBuildId() );
        practiceExamPaperAnswer.setQuestionCount( examPaperCache.getQuestionCount() );

        return practiceExamPaperAnswer;
    }

    @Override
    public ExamPaperAnswerInfoResponseVM toExamPaperAnswerInfoResponseVM(PracticeExamPaperAnswer practiceExamPaperAnswer) {
        if ( practiceExamPaperAnswer == null ) {
            return null;
        }

        ExamPaperAnswerInfoResponseVM examPaperAnswerInfoResponseVM = new ExamPaperAnswerInfoResponseVM();

        examPaperAnswerInfoResponseVM.setCreateUser( practiceExamPaperAnswer.getCreateUser() );
        examPaperAnswerInfoResponseVM.setDoTime( practiceExamPaperAnswer.getDoTime() );
        examPaperAnswerInfoResponseVM.setId( practiceExamPaperAnswer.getId() );
        examPaperAnswerInfoResponseVM.setPaperType( practiceExamPaperAnswer.getPaperType() );
        examPaperAnswerInfoResponseVM.setPassed( practiceExamPaperAnswer.getPassed() );

        examPaperAnswerInfoResponseVM.setUserScore( ExamUtil.scoreToVM(practiceExamPaperAnswer.getUserScore()) );
        examPaperAnswerInfoResponseVM.setPaperScore( ExamUtil.scoreToVM(practiceExamPaperAnswer.getPaperScore()) );
        examPaperAnswerInfoResponseVM.setDoTimeStr( ExamUtil.secondToVM(practiceExamPaperAnswer.getDoTime()) );
        examPaperAnswerInfoResponseVM.setPassScore( ExamUtil.scoreToVM(practiceExamPaperAnswer.getPassScore()) );
        examPaperAnswerInfoResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(practiceExamPaperAnswer.getCreateTime()) );

        return examPaperAnswerInfoResponseVM;
    }

    @Override
    public ExamPaperAnswerPageResponseVM toExamPaperAnswerPageResponseVM(PracticeExamPaperAnswer practiceExamPaperAnswer) {
        if ( practiceExamPaperAnswer == null ) {
            return null;
        }

        ExamPaperAnswerPageResponseVM examPaperAnswerPageResponseVM = new ExamPaperAnswerPageResponseVM();

        examPaperAnswerPageResponseVM.setId( practiceExamPaperAnswer.getId() );
        examPaperAnswerPageResponseVM.setPaperName( practiceExamPaperAnswer.getPaperName() );
        examPaperAnswerPageResponseVM.setPassed( practiceExamPaperAnswer.getPassed() );
        examPaperAnswerPageResponseVM.setQuestionCorrect( practiceExamPaperAnswer.getQuestionCorrect() );
        examPaperAnswerPageResponseVM.setQuestionCount( practiceExamPaperAnswer.getQuestionCount() );
        examPaperAnswerPageResponseVM.setStatus( practiceExamPaperAnswer.getStatus() );

        examPaperAnswerPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(practiceExamPaperAnswer.getCreateTime()) );
        examPaperAnswerPageResponseVM.setPassScore( ExamUtil.scoreToVM(practiceExamPaperAnswer.getPassScore()) );
        examPaperAnswerPageResponseVM.setSystemScore( ExamUtil.scoreToVM(practiceExamPaperAnswer.getSystemScore()) );
        examPaperAnswerPageResponseVM.setUserScore( ExamUtil.scoreToVM(practiceExamPaperAnswer.getUserScore()) );
        examPaperAnswerPageResponseVM.setPaperScore( ExamUtil.scoreToVM(practiceExamPaperAnswer.getPaperScore()) );
        examPaperAnswerPageResponseVM.setDoTime( ExamUtil.secondToVM(practiceExamPaperAnswer.getDoTime()) );

        return examPaperAnswerPageResponseVM;
    }
}
