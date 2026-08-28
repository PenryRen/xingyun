package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ExamPaper;
import com.mindskip.wdd.domain.ExamPaperBuild;
import com.mindskip.wdd.domain.ExamPaperCache;
import com.mindskip.wdd.domain.ExamPaperChild;
import com.mindskip.wdd.domain.frame.ExamPaperBuildQuestion;
import com.mindskip.wdd.domain.frame.ExamPaperBuildTitle;
import com.mindskip.wdd.domain.frame.ExamPaperItemFrame;
import com.mindskip.wdd.domain.frame.ExamPaperItemQuestionFrame;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoPaperVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoTitle;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-28T15:49:38+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ExamPaperMappingImpl implements ExamPaperMapping {

    @Override
    public ExamPaperDoPaperVM toExamPaperDoResponseVM(ExamPaper examPaper) {
        if ( examPaper == null ) {
            return null;
        }

        ExamPaperDoPaperVM examPaperDoPaperVM = new ExamPaperDoPaperVM();

        examPaperDoPaperVM.setPaperId( examPaper.getId() );
        examPaperDoPaperVM.setCapture( examPaper.getCapture() );
        examPaperDoPaperVM.setCheat( examPaper.getCheat() );
        examPaperDoPaperVM.setFaceCheck( examPaper.getFaceCheck() );
        examPaperDoPaperVM.setId( examPaper.getId() );
        examPaperDoPaperVM.setMaxCheatCount( examPaper.getMaxCheatCount() );
        examPaperDoPaperVM.setName( examPaper.getName() );
        examPaperDoPaperVM.setQuestionMess( examPaper.getQuestionMess() );
        examPaperDoPaperVM.setSuggestTime( examPaper.getSuggestTime() );

        examPaperDoPaperVM.setScore( ExamUtil.scoreToVM(examPaper.getScore()) );

        return examPaperDoPaperVM;
    }

    @Override
    public ExamPaperCache toExamPaperCache(ExamPaper examPaper) {
        if ( examPaper == null ) {
            return null;
        }

        ExamPaperCache examPaperCache = new ExamPaperCache();

        examPaperCache.setPaperId( examPaper.getId() );
        examPaperCache.setCapture( examPaper.getCapture() );
        examPaperCache.setCheat( examPaper.getCheat() );
        examPaperCache.setCreateDepartmentId( examPaper.getCreateDepartmentId() );
        examPaperCache.setCreateTime( examPaper.getCreateTime() );
        examPaperCache.setCreateUser( examPaper.getCreateUser() );
        examPaperCache.setCredentialTemplateId( examPaper.getCredentialTemplateId() );
        examPaperCache.setDeleted( examPaper.getDeleted() );
        examPaperCache.setExamPaperArchiveId( examPaper.getExamPaperArchiveId() );
        examPaperCache.setExamPaperBuildId( examPaper.getExamPaperBuildId() );
        examPaperCache.setFaceCheck( examPaper.getFaceCheck() );
        examPaperCache.setId( examPaper.getId() );
        examPaperCache.setLimitEndTime( examPaper.getLimitEndTime() );
        examPaperCache.setLimitStartTime( examPaper.getLimitStartTime() );
        examPaperCache.setMaxCheatCount( examPaper.getMaxCheatCount() );
        examPaperCache.setName( examPaper.getName() );
        examPaperCache.setPaperFrameId( examPaper.getPaperFrameId() );
        examPaperCache.setPaperType( examPaper.getPaperType() );
        examPaperCache.setPassScore( examPaper.getPassScore() );
        examPaperCache.setQuestionCount( examPaper.getQuestionCount() );
        examPaperCache.setQuestionItemMess( examPaper.getQuestionItemMess() );
        examPaperCache.setQuestionMess( examPaper.getQuestionMess() );
        examPaperCache.setScore( examPaper.getScore() );
        examPaperCache.setSuggestTime( examPaper.getSuggestTime() );
        examPaperCache.setWatch( examPaper.getWatch() );

        return examPaperCache;
    }

    @Override
    public ExamPaperDoPaperVM toExamPaperDoResponseVM(ExamPaperCache examPaperCache) {
        if ( examPaperCache == null ) {
            return null;
        }

        ExamPaperDoPaperVM examPaperDoPaperVM = new ExamPaperDoPaperVM();

        examPaperDoPaperVM.setPaperId( examPaperCache.getId() );
        examPaperDoPaperVM.setCapture( examPaperCache.getCapture() );
        examPaperDoPaperVM.setCheat( examPaperCache.getCheat() );
        examPaperDoPaperVM.setFaceCheck( examPaperCache.getFaceCheck() );
        examPaperDoPaperVM.setId( examPaperCache.getId() );
        examPaperDoPaperVM.setMaxCheatCount( examPaperCache.getMaxCheatCount() );
        examPaperDoPaperVM.setName( examPaperCache.getName() );
        examPaperDoPaperVM.setQuestionMess( examPaperCache.getQuestionMess() );
        examPaperDoPaperVM.setSuggestTime( examPaperCache.getSuggestTime() );

        examPaperDoPaperVM.setScore( ExamUtil.scoreToVM(examPaperCache.getScore()) );
        examPaperDoPaperVM.setSuggestTimeStr( ExamUtil.minToVM(examPaperCache.getSuggestTime()) );

        return examPaperDoPaperVM;
    }

    @Override
    public ExamPaperDoTitle toExamPaperDoTitle(ExamPaperItemFrame examPaperItemFrame) {
        if ( examPaperItemFrame == null ) {
            return null;
        }

        ExamPaperDoTitle examPaperDoTitle = new ExamPaperDoTitle();

        examPaperDoTitle.setName( examPaperItemFrame.getName() );

        return examPaperDoTitle;
    }

    @Override
    public ExamPaper toExamPaper(ExamPaperBuild examPaperBuild) {
        if ( examPaperBuild == null ) {
            return null;
        }

        ExamPaper examPaper = new ExamPaper();

        examPaper.setCreateDepartmentId( examPaperBuild.getCreateDepartmentId() );
        examPaper.setCreateTime( examPaperBuild.getCreateTime() );
        examPaper.setCreateUser( examPaperBuild.getCreateUser() );
        examPaper.setCredentialTemplateId( examPaperBuild.getCredentialTemplateId() );
        examPaper.setDeleted( examPaperBuild.getDeleted() );
        examPaper.setExamPaperArchiveId( examPaperBuild.getExamPaperArchiveId() );
        examPaper.setLimitEndTime( examPaperBuild.getLimitEndTime() );
        examPaper.setLimitStartTime( examPaperBuild.getLimitStartTime() );
        examPaper.setName( examPaperBuild.getName() );
        examPaper.setPassScore( examPaperBuild.getPassScore() );
        examPaper.setQuestionCount( examPaperBuild.getQuestionCount() );
        examPaper.setScore( examPaperBuild.getScore() );
        examPaper.setSuggestTime( examPaperBuild.getSuggestTime() );
        examPaper.setVmType( examPaperBuild.getVmType() );

        return examPaper;
    }

    @Override
    public ExamPaperItemFrame toExamPaperItemFrame(ExamPaperBuildTitle examPaperBuildTitle) {
        if ( examPaperBuildTitle == null ) {
            return null;
        }

        ExamPaperItemFrame examPaperItemFrame = new ExamPaperItemFrame();

        examPaperItemFrame.setName( examPaperBuildTitle.getName() );

        return examPaperItemFrame;
    }

    @Override
    public ExamPaperItemQuestionFrame toExamPaperItemQuestionFrame(ExamPaperBuildQuestion examPaperBuildQuestion) {
        if ( examPaperBuildQuestion == null ) {
            return null;
        }

        ExamPaperItemQuestionFrame examPaperItemQuestionFrame = new ExamPaperItemQuestionFrame();

        examPaperItemQuestionFrame.setTrickScore( examPaperBuildQuestion.getScore() );
        examPaperItemQuestionFrame.setId( examPaperBuildQuestion.getId() );
        examPaperItemQuestionFrame.setQuestionFrameId( examPaperBuildQuestion.getQuestionFrameId() );

        return examPaperItemQuestionFrame;
    }

    @Override
    public ExamPaperChild toExamPaperChild(ExamPaper examPaper) {
        if ( examPaper == null ) {
            return null;
        }

        ExamPaperChild examPaperChild = new ExamPaperChild();

        examPaperChild.setCapture( examPaper.getCapture() );
        examPaperChild.setCheat( examPaper.getCheat() );
        examPaperChild.setCreateTime( examPaper.getCreateTime() );
        examPaperChild.setCredentialTemplateId( examPaper.getCredentialTemplateId() );
        examPaperChild.setDeleted( examPaper.getDeleted() );
        examPaperChild.setExamPaperArchiveId( examPaper.getExamPaperArchiveId() );
        examPaperChild.setExamPaperBuildId( examPaper.getExamPaperBuildId() );
        examPaperChild.setFaceCheck( examPaper.getFaceCheck() );
        examPaperChild.setLimitEndTime( examPaper.getLimitEndTime() );
        examPaperChild.setLimitStartTime( examPaper.getLimitStartTime() );
        examPaperChild.setMaxCheatCount( examPaper.getMaxCheatCount() );
        examPaperChild.setName( examPaper.getName() );
        examPaperChild.setPaperFrameId( examPaper.getPaperFrameId() );
        examPaperChild.setPaperType( examPaper.getPaperType() );
        examPaperChild.setPassScore( examPaper.getPassScore() );
        examPaperChild.setQuestionCount( examPaper.getQuestionCount() );
        examPaperChild.setQuestionItemMess( examPaper.getQuestionItemMess() );
        examPaperChild.setQuestionMess( examPaper.getQuestionMess() );
        examPaperChild.setScore( examPaper.getScore() );
        examPaperChild.setSuggestTime( examPaper.getSuggestTime() );
        examPaperChild.setWatch( examPaper.getWatch() );

        return examPaperChild;
    }
}
