package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Train;
import com.mindskip.wdd.domain.TrainItem;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.train.course.TrainCoursePageResponseVM;
import com.mindskip.wdd.viewmodel.train.course.TrainEditItem;
import com.mindskip.wdd.viewmodel.train.course.TrainEditRequestVM;
import com.mindskip.wdd.viewmodel.train.detail.TrainDetailVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-28T15:49:39+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class TrainMappingImpl implements TrainMapping {

    @Override
    public TrainCoursePageResponseVM toTrainResponseVM(Train train) {
        if ( train == null ) {
            return null;
        }

        TrainCoursePageResponseVM trainCoursePageResponseVM = new TrainCoursePageResponseVM();

        trainCoursePageResponseVM.setCreateUser( train.getCreateUser() );
        trainCoursePageResponseVM.setId( train.getId() );
        trainCoursePageResponseVM.setName( train.getName() );

        trainCoursePageResponseVM.setStudyTimeStr( DateTimeUtil.secondToChineseTime(train.getStudyTime()) );
        trainCoursePageResponseVM.setStartTime( DateTimeUtil.dateTimeFullFormat(train.getStartTime()) );
        trainCoursePageResponseVM.setEndTime( DateTimeUtil.dateTimeFullFormat(train.getEndTime()) );
        trainCoursePageResponseVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(train.getCreateTime()) );

        return trainCoursePageResponseVM;
    }

    @Override
    public Train toTrain(TrainEditRequestVM trainEditRequestVM) {
        if ( trainEditRequestVM == null ) {
            return null;
        }

        Train train = new Train();

        train.setCoverPath( trainEditRequestVM.getCoverPath() );
        train.setDescription( trainEditRequestVM.getDescription() );
        train.setId( trainEditRequestVM.getId() );
        train.setName( trainEditRequestVM.getName() );
        train.setTrainArchiveId( trainEditRequestVM.getTrainArchiveId() );

        return train;
    }

    @Override
    public void mapTrain(TrainEditRequestVM trainEditRequestVM, Train train) {
        if ( trainEditRequestVM == null ) {
            return;
        }

        train.setCoverPath( trainEditRequestVM.getCoverPath() );
        train.setDescription( trainEditRequestVM.getDescription() );
        train.setId( trainEditRequestVM.getId() );
        train.setName( trainEditRequestVM.getName() );
        train.setTrainArchiveId( trainEditRequestVM.getTrainArchiveId() );
    }

    @Override
    public TrainEditRequestVM toTrainEditRequestVM(Train train) {
        if ( train == null ) {
            return null;
        }

        TrainEditRequestVM trainEditRequestVM = new TrainEditRequestVM();

        trainEditRequestVM.setCoverPath( train.getCoverPath() );
        trainEditRequestVM.setDescription( train.getDescription() );
        trainEditRequestVM.setId( train.getId() );
        trainEditRequestVM.setName( train.getName() );
        trainEditRequestVM.setTrainArchiveId( train.getTrainArchiveId() );

        return trainEditRequestVM;
    }

    @Override
    public TrainItem toTrainItem(TrainEditItem trainEditItem) {
        if ( trainEditItem == null ) {
            return null;
        }

        TrainItem trainItem = new TrainItem();

        trainItem.setAllowCount( trainEditItem.getAllowCount() );
        trainItem.setFileType( trainEditItem.getFileType() );
        trainItem.setItemOrder( trainEditItem.getItemOrder() );
        trainItem.setMaxNumber( trainEditItem.getMaxNumber() );
        trainItem.setName( trainEditItem.getName() );
        trainItem.setPassNumber( trainEditItem.getPassNumber() );
        trainItem.setTargetId( trainEditItem.getTargetId() );
        trainItem.setTargetType( trainEditItem.getTargetType() );
        trainItem.setTrainId( trainEditItem.getTrainId() );

        return trainItem;
    }

    @Override
    public void mapTrainItem(TrainEditItem trainEditItem, TrainItem trainItem) {
        if ( trainEditItem == null ) {
            return;
        }

        trainItem.setAllowCount( trainEditItem.getAllowCount() );
        trainItem.setFileType( trainEditItem.getFileType() );
        trainItem.setItemOrder( trainEditItem.getItemOrder() );
        trainItem.setMaxNumber( trainEditItem.getMaxNumber() );
        trainItem.setName( trainEditItem.getName() );
        trainItem.setPassNumber( trainEditItem.getPassNumber() );
        trainItem.setTargetId( trainEditItem.getTargetId() );
        trainItem.setTargetType( trainEditItem.getTargetType() );
        trainItem.setTrainId( trainEditItem.getTrainId() );
    }

    @Override
    public TrainEditItem toTrainEditItem(TrainItem trainItem) {
        if ( trainItem == null ) {
            return null;
        }

        TrainEditItem trainEditItem = new TrainEditItem();

        trainEditItem.setAllowCount( trainItem.getAllowCount() );
        trainEditItem.setFileType( trainItem.getFileType() );
        trainEditItem.setId( trainItem.getId() );
        trainEditItem.setItemOrder( trainItem.getItemOrder() );
        trainEditItem.setMaxNumber( trainItem.getMaxNumber() );
        trainEditItem.setName( trainItem.getName() );
        trainEditItem.setPassNumber( trainItem.getPassNumber() );
        trainEditItem.setTargetId( trainItem.getTargetId() );
        trainEditItem.setTargetType( trainItem.getTargetType() );
        trainEditItem.setTrainId( trainItem.getTrainId() );

        return trainEditItem;
    }

    @Override
    public TrainDetailVM toTrainDetailVM(Train train) {
        if ( train == null ) {
            return null;
        }

        TrainDetailVM trainDetailVM = new TrainDetailVM();

        trainDetailVM.setCreateUser( train.getCreateUser() );
        trainDetailVM.setId( train.getId() );
        trainDetailVM.setName( train.getName() );

        trainDetailVM.setStudyTimeStr( DateTimeUtil.secondToChineseTime(train.getStudyTime()) );
        trainDetailVM.setStartTime( DateTimeUtil.dateTimeFullFormat(train.getStartTime()) );
        trainDetailVM.setEndTime( DateTimeUtil.dateTimeFullFormat(train.getEndTime()) );
        trainDetailVM.setCreateTime( DateTimeUtil.dateTimeFullFormat(train.getCreateTime()) );

        return trainDetailVM;
    }
}
