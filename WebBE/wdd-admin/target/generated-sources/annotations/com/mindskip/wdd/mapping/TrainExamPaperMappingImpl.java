package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ExamPaperCache;
import com.mindskip.wdd.domain.TrainExamPaper;
import com.mindskip.wdd.domain.TrainExamPaperAnswer;
import com.mindskip.wdd.domain.frame.ExamPaperItemFrame;
import com.mindskip.wdd.domain.frame.ExamPaperItemQuestionFrame;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperTitleItemVM;
import com.mindskip.wdd.viewmodel.question.QuestionEditRequestVM;
import com.mindskip.wdd.viewmodel.train.exam.answer.TrainPaperAnswerPageResponseVM;
import com.mindskip.wdd.viewmodel.train.exam.paper.TrainExamPaperEditVM;
import com.mindskip.wdd.viewmodel.train.exam.paper.TrainExamPaperPageResponseVM;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-28T15:49:38+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class TrainExamPaperMappingImpl implements TrainExamPaperMapping {

    @Override
    public TrainExamPaperPageResponseVM toTrainExamPaperPageResponseVM(TrainExamPaper trainExamPaper) {
        if ( trainExamPaper == null ) {
            return null;
        }

        TrainExamPaperPageResponseVM trainExamPaperPageResponseVM = new TrainExamPaperPageResponseVM();

        trainExamPaperPageResponseVM.setCreateUser( trainExamPaper.getCreateUser() );
        trainExamPaperPageResponseVM.setId( trainExamPaper.getId() );
        trainExamPaperPageResponseVM.setName( trainExamPaper.getName() );
        trainExamPaperPageResponseVM.setQuestionCount( trainExamPaper.getQuestionCount() );
        trainExamPaperPageResponseVM.setSuggestTime( trainExamPaper.getSuggestTime() );

        trainExamPaperPageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(trainExamPaper.getCreateTime()) );
        trainExamPaperPageResponseVM.setSuggestTimeStr( ExamUtil.minToVM(trainExamPaper.getSuggestTime()) );
        trainExamPaperPageResponseVM.setScore( ExamUtil.scoreToVM(trainExamPaper.getScore()) );

        return trainExamPaperPageResponseVM;
    }

    @Override
    public TrainExamPaperEditVM toTrainExamPaperEditVM(TrainExamPaper trainExamPaper) {
        if ( trainExamPaper == null ) {
            return null;
        }

        TrainExamPaperEditVM trainExamPaperEditVM = new TrainExamPaperEditVM();

        trainExamPaperEditVM.setCheat( trainExamPaper.getCheat() );
        trainExamPaperEditVM.setExamPaperArchiveId( trainExamPaper.getExamPaperArchiveId() );
        trainExamPaperEditVM.setId( trainExamPaper.getId() );
        trainExamPaperEditVM.setMaxCheatCount( trainExamPaper.getMaxCheatCount() );
        trainExamPaperEditVM.setName( trainExamPaper.getName() );
        trainExamPaperEditVM.setQuestionCount( trainExamPaper.getQuestionCount() );
        trainExamPaperEditVM.setQuestionItemMess( trainExamPaper.getQuestionItemMess() );
        trainExamPaperEditVM.setQuestionMess( trainExamPaper.getQuestionMess() );
        trainExamPaperEditVM.setSuggestTime( trainExamPaper.getSuggestTime() );

        trainExamPaperEditVM.setScore( ExamUtil.scoreToVM(trainExamPaper.getScore()) );

        return trainExamPaperEditVM;
    }

    @Override
    public TrainExamPaper toTrainExamPaper(TrainExamPaperEditVM trainExamPaperEditVM) {
        if ( trainExamPaperEditVM == null ) {
            return null;
        }

        TrainExamPaper trainExamPaper = new TrainExamPaper();

        trainExamPaper.setCheat( trainExamPaperEditVM.getCheat() );
        trainExamPaper.setExamPaperArchiveId( trainExamPaperEditVM.getExamPaperArchiveId() );
        trainExamPaper.setId( trainExamPaperEditVM.getId() );
        trainExamPaper.setMaxCheatCount( trainExamPaperEditVM.getMaxCheatCount() );
        trainExamPaper.setName( trainExamPaperEditVM.getName() );
        trainExamPaper.setQuestionCount( trainExamPaperEditVM.getQuestionCount() );
        trainExamPaper.setQuestionItemMess( trainExamPaperEditVM.getQuestionItemMess() );
        trainExamPaper.setQuestionMess( trainExamPaperEditVM.getQuestionMess() );
        if ( trainExamPaperEditVM.getScore() != null ) {
            trainExamPaper.setScore( Integer.parseInt( trainExamPaperEditVM.getScore() ) );
        }
        trainExamPaper.setSuggestTime( trainExamPaperEditVM.getSuggestTime() );

        return trainExamPaper;
    }

    @Override
    public void toTrainExamPaper(TrainExamPaperEditVM trainExamPaperEditVM, TrainExamPaper trainExamPaper) {
        if ( trainExamPaperEditVM == null ) {
            return;
        }

        trainExamPaper.setCheat( trainExamPaperEditVM.getCheat() );
        trainExamPaper.setExamPaperArchiveId( trainExamPaperEditVM.getExamPaperArchiveId() );
        trainExamPaper.setId( trainExamPaperEditVM.getId() );
        trainExamPaper.setMaxCheatCount( trainExamPaperEditVM.getMaxCheatCount() );
        trainExamPaper.setName( trainExamPaperEditVM.getName() );
        trainExamPaper.setQuestionCount( trainExamPaperEditVM.getQuestionCount() );
        trainExamPaper.setQuestionItemMess( trainExamPaperEditVM.getQuestionItemMess() );
        trainExamPaper.setQuestionMess( trainExamPaperEditVM.getQuestionMess() );
        trainExamPaper.setSuggestTime( trainExamPaperEditVM.getSuggestTime() );

        trainExamPaper.setScore( ExamUtil.scoreFromVM(trainExamPaperEditVM.getScore()) );
    }

    @Override
    public ExamPaperItemFrame toExamPaperItemFrame(ExamPaperTitleItemVM examPaperTitleItemVM) {
        if ( examPaperTitleItemVM == null ) {
            return null;
        }

        ExamPaperItemFrame examPaperItemFrame = new ExamPaperItemFrame();

        examPaperItemFrame.setName( examPaperTitleItemVM.getName() );

        return examPaperItemFrame;
    }

    @Override
    public ExamPaperItemQuestionFrame toExamPaperItemQuestionFrame(QuestionEditRequestVM questionEditRequestVM) {
        if ( questionEditRequestVM == null ) {
            return null;
        }

        ExamPaperItemQuestionFrame examPaperItemQuestionFrame = new ExamPaperItemQuestionFrame();

        examPaperItemQuestionFrame.setTrickScore( questionEditRequestVM.getScore() );
        examPaperItemQuestionFrame.setId( questionEditRequestVM.getId() );
        examPaperItemQuestionFrame.setItemOrder( questionEditRequestVM.getItemOrder() );
        examPaperItemQuestionFrame.setQuestionFrameId( questionEditRequestVM.getQuestionFrameId() );

        return examPaperItemQuestionFrame;
    }

    @Override
    public List<ExamPaperItemQuestionFrame> toExamPaperItemQuestionFrameList(List<QuestionEditRequestVM> questionEditRequestVMList) {
        if ( questionEditRequestVMList == null ) {
            return null;
        }

        List<ExamPaperItemQuestionFrame> list = new ArrayList<ExamPaperItemQuestionFrame>( questionEditRequestVMList.size() );
        for ( QuestionEditRequestVM questionEditRequestVM : questionEditRequestVMList ) {
            list.add( toExamPaperItemQuestionFrame( questionEditRequestVM ) );
        }

        return list;
    }

    @Override
    public TrainPaperAnswerPageResponseVM toTrainPaperAnswerPageResponseVM(TrainExamPaperAnswer trainExamPaperAnswer) {
        if ( trainExamPaperAnswer == null ) {
            return null;
        }

        TrainPaperAnswerPageResponseVM trainPaperAnswerPageResponseVM = new TrainPaperAnswerPageResponseVM();

        trainPaperAnswerPageResponseVM.setCreateTime( trainExamPaperAnswer.getCreateTime() );
        trainPaperAnswerPageResponseVM.setCreateUser( trainExamPaperAnswer.getCreateUser() );
        trainPaperAnswerPageResponseVM.setDoTime( trainExamPaperAnswer.getDoTime() );
        trainPaperAnswerPageResponseVM.setId( trainExamPaperAnswer.getId() );
        trainPaperAnswerPageResponseVM.setPaperScore( trainExamPaperAnswer.getPaperScore() );
        trainPaperAnswerPageResponseVM.setPassed( trainExamPaperAnswer.getPassed() );
        trainPaperAnswerPageResponseVM.setPreviewFilePath( trainExamPaperAnswer.getPreviewFilePath() );
        trainPaperAnswerPageResponseVM.setQuestionCorrect( trainExamPaperAnswer.getQuestionCorrect() );
        trainPaperAnswerPageResponseVM.setQuestionCount( trainExamPaperAnswer.getQuestionCount() );
        trainPaperAnswerPageResponseVM.setStatus( trainExamPaperAnswer.getStatus() );
        trainPaperAnswerPageResponseVM.setUserScore( trainExamPaperAnswer.getUserScore() );

        return trainPaperAnswerPageResponseVM;
    }

    @Override
    public ExamPaperCache toExamPaperCache(TrainExamPaper trainExamPaper) {
        if ( trainExamPaper == null ) {
            return null;
        }

        ExamPaperCache examPaperCache = new ExamPaperCache();

        if ( trainExamPaper.getId() != null ) {
            examPaperCache.setPaperId( trainExamPaper.getId().longValue() );
        }
        examPaperCache.setCheat( trainExamPaper.getCheat() );
        examPaperCache.setCreateDepartmentId( trainExamPaper.getCreateDepartmentId() );
        examPaperCache.setCreateTime( trainExamPaper.getCreateTime() );
        examPaperCache.setCreateUser( trainExamPaper.getCreateUser() );
        examPaperCache.setDeleted( trainExamPaper.getDeleted() );
        examPaperCache.setExamPaperArchiveId( trainExamPaper.getExamPaperArchiveId() );
        if ( trainExamPaper.getId() != null ) {
            examPaperCache.setId( trainExamPaper.getId().longValue() );
        }
        examPaperCache.setMaxCheatCount( trainExamPaper.getMaxCheatCount() );
        examPaperCache.setName( trainExamPaper.getName() );
        examPaperCache.setPaperFrameId( trainExamPaper.getPaperFrameId() );
        examPaperCache.setQuestionCount( trainExamPaper.getQuestionCount() );
        examPaperCache.setQuestionItemMess( trainExamPaper.getQuestionItemMess() );
        examPaperCache.setQuestionMess( trainExamPaper.getQuestionMess() );
        examPaperCache.setScore( trainExamPaper.getScore() );
        examPaperCache.setSuggestTime( trainExamPaper.getSuggestTime() );

        return examPaperCache;
    }

    @Override
    public ExamPaperAnswerInfoResponseVM toExamPaperAnswerInfoResponseVM(TrainExamPaperAnswer trainExamPaperAnswer) {
        if ( trainExamPaperAnswer == null ) {
            return null;
        }

        ExamPaperAnswerInfoResponseVM examPaperAnswerInfoResponseVM = new ExamPaperAnswerInfoResponseVM();

        examPaperAnswerInfoResponseVM.setCreateTime( trainExamPaperAnswer.getCreateTime() );
        examPaperAnswerInfoResponseVM.setCreateUser( trainExamPaperAnswer.getCreateUser() );
        examPaperAnswerInfoResponseVM.setDoTime( trainExamPaperAnswer.getDoTime() );
        examPaperAnswerInfoResponseVM.setId( trainExamPaperAnswer.getId() );
        examPaperAnswerInfoResponseVM.setJudgeUser( trainExamPaperAnswer.getJudgeUser() );
        if ( trainExamPaperAnswer.getPassScore() != null ) {
            examPaperAnswerInfoResponseVM.setPassScore( String.valueOf( trainExamPaperAnswer.getPassScore() ) );
        }
        examPaperAnswerInfoResponseVM.setPassed( trainExamPaperAnswer.getPassed() );
        examPaperAnswerInfoResponseVM.setStatus( trainExamPaperAnswer.getStatus() );

        examPaperAnswerInfoResponseVM.setUserScore( ExamUtil.scoreToVM(trainExamPaperAnswer.getUserScore()) );
        examPaperAnswerInfoResponseVM.setPaperScore( ExamUtil.scoreToVM(trainExamPaperAnswer.getPaperScore()) );
        examPaperAnswerInfoResponseVM.setDoTimeStr( ExamUtil.secondToVM(trainExamPaperAnswer.getDoTime()) );

        return examPaperAnswerInfoResponseVM;
    }
}
