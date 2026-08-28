package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ExamPaperCache;
import com.mindskip.wdd.domain.Train;
import com.mindskip.wdd.domain.TrainArchive;
import com.mindskip.wdd.domain.TrainExamPaper;
import com.mindskip.wdd.domain.TrainExamPaperAnswer;
import com.mindskip.wdd.domain.TrainItem;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.train.TrainArchiveVM;
import com.mindskip.wdd.viewmodel.train.TrainDetailItem;
import com.mindskip.wdd.viewmodel.train.TrainDetailVM;
import com.mindskip.wdd.viewmodel.train.TrainExamPaperAnswerVM;
import com.mindskip.wdd.viewmodel.train.TrainPageResponseVM;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-28T15:49:47+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class TrainMappingImpl implements TrainMapping {

    @Override
    public TrainArchiveVM ToTrainArchiveVM(TrainArchive trainArchive) {
        if ( trainArchive == null ) {
            return null;
        }

        TrainArchiveVM trainArchiveVM = new TrainArchiveVM();

        trainArchiveVM.setId( trainArchive.getId() );
        trainArchiveVM.setLevel( trainArchive.getLevel() );
        trainArchiveVM.setName( trainArchive.getName() );

        return trainArchiveVM;
    }

    @Override
    public List<TrainArchiveVM> toTrainArchiveVMList(List<TrainArchive> trainArchiveList) {
        if ( trainArchiveList == null ) {
            return null;
        }

        List<TrainArchiveVM> list = new ArrayList<TrainArchiveVM>( trainArchiveList.size() );
        for ( TrainArchive trainArchive : trainArchiveList ) {
            list.add( ToTrainArchiveVM( trainArchive ) );
        }

        return list;
    }

    @Override
    public TrainPageResponseVM toTrainPageResponseVM(Train train) {
        if ( train == null ) {
            return null;
        }

        TrainPageResponseVM trainPageResponseVM = new TrainPageResponseVM();

        trainPageResponseVM.setCoverPath( train.getCoverPath() );
        trainPageResponseVM.setDescription( train.getDescription() );
        trainPageResponseVM.setId( train.getId() );
        trainPageResponseVM.setItemCount( train.getItemCount() );
        trainPageResponseVM.setName( train.getName() );

        trainPageResponseVM.setStudyTimeStr( DateTimeUtil.secondToChineseTime(train.getStudyTime()) );

        return trainPageResponseVM;
    }

    @Override
    public TrainDetailVM toTrainDetailVM(Train train) {
        if ( train == null ) {
            return null;
        }

        TrainDetailVM trainDetailVM = new TrainDetailVM();

        trainDetailVM.setCoverPath( train.getCoverPath() );
        trainDetailVM.setDescription( train.getDescription() );
        trainDetailVM.setId( train.getId() );
        trainDetailVM.setItemCount( train.getItemCount() );
        trainDetailVM.setName( train.getName() );

        trainDetailVM.setStudyTimeStr( DateTimeUtil.secondToChineseTime(train.getStudyTime()) );
        trainDetailVM.setStartTime( DateTimeUtil.dateTimeFullFormat(train.getStartTime()) );
        trainDetailVM.setEndTime( DateTimeUtil.dateTimeFullFormat(train.getEndTime()) );

        return trainDetailVM;
    }

    @Override
    public TrainDetailItem toTrainDetailItem(TrainItem trainItem) {
        if ( trainItem == null ) {
            return null;
        }

        TrainDetailItem trainDetailItem = new TrainDetailItem();

        trainDetailItem.setAllowCount( trainItem.getAllowCount() );
        trainDetailItem.setFileType( trainItem.getFileType() );
        trainDetailItem.setItemOrder( trainItem.getItemOrder() );
        trainDetailItem.setMaxNumber( trainItem.getMaxNumber() );
        trainDetailItem.setName( trainItem.getName() );
        trainDetailItem.setPassNumber( trainItem.getPassNumber() );
        trainDetailItem.setTargetId( trainItem.getTargetId() );
        trainDetailItem.setVmType( trainItem.getVmType() );

        return trainDetailItem;
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
    public TrainExamPaperAnswer toTrainExamPaperAnswer(TrainExamPaper trainExamPaper) {
        if ( trainExamPaper == null ) {
            return null;
        }

        TrainExamPaperAnswer trainExamPaperAnswer = new TrainExamPaperAnswer();

        trainExamPaperAnswer.setCreateDepartmentId( trainExamPaper.getCreateDepartmentId() );
        trainExamPaperAnswer.setCreateTime( trainExamPaper.getCreateTime() );
        trainExamPaperAnswer.setCreateUser( trainExamPaper.getCreateUser() );
        trainExamPaperAnswer.setDeleted( trainExamPaper.getDeleted() );
        trainExamPaperAnswer.setExamPaperArchiveId( trainExamPaper.getExamPaperArchiveId() );
        trainExamPaperAnswer.setQuestionCount( trainExamPaper.getQuestionCount() );

        return trainExamPaperAnswer;
    }

    @Override
    public TrainExamPaperAnswerVM toTrainExamPaperAnswerVM(TrainExamPaperAnswer trainExamPaperAnswer) {
        if ( trainExamPaperAnswer == null ) {
            return null;
        }

        TrainExamPaperAnswerVM trainExamPaperAnswerVM = new TrainExamPaperAnswerVM();

        trainExamPaperAnswerVM.setId( trainExamPaperAnswer.getId() );
        trainExamPaperAnswerVM.setPaperName( trainExamPaperAnswer.getPaperName() );
        trainExamPaperAnswerVM.setPassed( trainExamPaperAnswer.getPassed() );
        trainExamPaperAnswerVM.setQuestionCorrect( trainExamPaperAnswer.getQuestionCorrect() );
        trainExamPaperAnswerVM.setQuestionCount( trainExamPaperAnswer.getQuestionCount() );
        trainExamPaperAnswerVM.setStatus( trainExamPaperAnswer.getStatus() );

        trainExamPaperAnswerVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(trainExamPaperAnswer.getCreateTime()) );
        trainExamPaperAnswerVM.setPassScore( ExamUtil.scoreToVM(trainExamPaperAnswer.getPassScore()) );
        trainExamPaperAnswerVM.setSystemScore( ExamUtil.scoreToVM(trainExamPaperAnswer.getSystemScore()) );
        trainExamPaperAnswerVM.setUserScore( ExamUtil.scoreToVM(trainExamPaperAnswer.getUserScore()) );
        trainExamPaperAnswerVM.setPaperScore( ExamUtil.scoreToVM(trainExamPaperAnswer.getPaperScore()) );
        trainExamPaperAnswerVM.setDoTime( ExamUtil.secondToVM(trainExamPaperAnswer.getDoTime()) );

        return trainExamPaperAnswerVM;
    }
}
