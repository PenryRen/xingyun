package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ExamPaper;
import com.mindskip.wdd.domain.ExamPaperArchive;
import com.mindskip.wdd.domain.ExamPaperCache;
import com.mindskip.wdd.domain.ExamPaperChild;
import com.mindskip.wdd.domain.frame.ExamPaperItemFrame;
import com.mindskip.wdd.domain.frame.ExamPaperItemQuestionFrame;
import com.mindskip.wdd.domain.other.QuestionRandomItem;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperArchiveVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoPaperVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoTitle;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperPageResponseVM;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:28:30+0800",
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
        examPaperDoPaperVM.setId( examPaper.getId() );
        examPaperDoPaperVM.setMaxCheatCount( examPaper.getMaxCheatCount() );
        examPaperDoPaperVM.setName( examPaper.getName() );
        examPaperDoPaperVM.setPaperType( examPaper.getPaperType() );
        examPaperDoPaperVM.setQuestionMess( examPaper.getQuestionMess() );
        examPaperDoPaperVM.setSuggestTime( examPaper.getSuggestTime() );

        examPaperDoPaperVM.setScore( ExamUtil.scoreToVM(examPaper.getScore()) );
        examPaperDoPaperVM.setSuggestTimeStr( ExamUtil.minToVM(examPaper.getSuggestTime()) );

        return examPaperDoPaperVM;
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
        examPaperDoPaperVM.setId( examPaperCache.getId() );
        examPaperDoPaperVM.setMaxCheatCount( examPaperCache.getMaxCheatCount() );
        examPaperDoPaperVM.setName( examPaperCache.getName() );
        examPaperDoPaperVM.setPaperType( examPaperCache.getPaperType() );
        examPaperDoPaperVM.setQuestionMess( examPaperCache.getQuestionMess() );
        examPaperDoPaperVM.setSuggestTime( examPaperCache.getSuggestTime() );

        examPaperDoPaperVM.setScore( ExamUtil.scoreToVM(examPaperCache.getScore()) );
        examPaperDoPaperVM.setSuggestTimeStr( ExamUtil.minToVM(examPaperCache.getSuggestTime()) );

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
    public ExamPaperDoTitle toExamPaperDoTitle(ExamPaperItemFrame examPaperItemFrame) {
        if ( examPaperItemFrame == null ) {
            return null;
        }

        ExamPaperDoTitle examPaperDoTitle = new ExamPaperDoTitle();

        examPaperDoTitle.setName( examPaperItemFrame.getName() );

        return examPaperDoTitle;
    }

    @Override
    public ExamPaperPageResponseVM toExamPaperPageResponseVM(ExamPaper examPaper) {
        if ( examPaper == null ) {
            return null;
        }

        ExamPaperPageResponseVM examPaperPageResponseVM = new ExamPaperPageResponseVM();

        examPaperPageResponseVM.setFaceCheck( examPaper.getFaceCheck() );
        examPaperPageResponseVM.setId( examPaper.getId() );
        examPaperPageResponseVM.setName( examPaper.getName() );
        examPaperPageResponseVM.setPaperType( examPaper.getPaperType() );
        examPaperPageResponseVM.setQuestionCount( examPaper.getQuestionCount() );
        examPaperPageResponseVM.setSuggestTime( examPaper.getSuggestTime() );

        examPaperPageResponseVM.setScore( ExamUtil.scoreToVM(examPaper.getScore()) );
        examPaperPageResponseVM.setPassScore( ExamUtil.scoreToVM(examPaper.getPassScore()) );
        examPaperPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(examPaper.getCreateTime()) );
        examPaperPageResponseVM.setLimitStartTime( DateTimeUtil.dateTimeFullFormat(examPaper.getLimitStartTime()) );
        examPaperPageResponseVM.setLimitEndTime( DateTimeUtil.dateTimeFullFormat(examPaper.getLimitEndTime()) );
        examPaperPageResponseVM.setSuggestTimeStr( ExamUtil.minToVM(examPaper.getSuggestTime()) );

        return examPaperPageResponseVM;
    }

    @Override
    public ExamPaperArchiveVM toExamPaperArchiveVM(ExamPaperArchive examPaperArchive) {
        if ( examPaperArchive == null ) {
            return null;
        }

        ExamPaperArchiveVM examPaperArchiveVM = new ExamPaperArchiveVM();

        examPaperArchiveVM.setId( examPaperArchive.getId() );
        examPaperArchiveVM.setLevel( examPaperArchive.getLevel() );
        examPaperArchiveVM.setName( examPaperArchive.getName() );

        return examPaperArchiveVM;
    }

    @Override
    public List<ExamPaperArchiveVM> toExamPaperArchiveVMList(List<ExamPaperArchive> examPaperArchiveList) {
        if ( examPaperArchiveList == null ) {
            return null;
        }

        List<ExamPaperArchiveVM> list = new ArrayList<ExamPaperArchiveVM>( examPaperArchiveList.size() );
        for ( ExamPaperArchive examPaperArchive : examPaperArchiveList ) {
            list.add( toExamPaperArchiveVM( examPaperArchive ) );
        }

        return list;
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
