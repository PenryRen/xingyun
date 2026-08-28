package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.Train;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.train.course.TrainCoursePageResponseVM;
import com.mindskip.wdd.viewmodel.train.course.TrainEditItem;
import com.mindskip.wdd.viewmodel.train.course.TrainEditRequestVM;
import com.mindskip.wdd.viewmodel.train.detail.TrainDetailVM;
import org.mapstruct.*;


/**
 * @version 1.7.0
 * @description: TrainMapping
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface TrainMapping {

    /**
     * @param train
     * @return {@link TrainCoursePageResponseVM}
     */
    @Mappings({
            @Mapping(target = "studyTimeStr", expression = "java(DateTimeUtil.secondToChineseTime(train.getStudyTime()))"),
            @Mapping(target = "startTime", expression = "java(DateTimeUtil.dateTimeFullFormat(train.getStartTime()))"),
            @Mapping(target = "endTime", expression = "java(DateTimeUtil.dateTimeFullFormat(train.getEndTime()))"),
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(train.getCreateTime()))")
    })
    TrainCoursePageResponseVM toTrainResponseVM(Train train);


    /**
     * @param trainEditRequestVM
     * @return {@link Train}
     */
    Train toTrain(TrainEditRequestVM trainEditRequestVM);


    /**
     * @param trainEditRequestVM
     * @param train
     */
    @InheritConfiguration
    void mapTrain(TrainEditRequestVM trainEditRequestVM, @MappingTarget Train train);


    /**
     * @param train
     * @return {@link TrainEditRequestVM}
     */
    TrainEditRequestVM toTrainEditRequestVM(Train train);

    @Mappings({@Mapping(target = "id", ignore = true)})
    TrainItem toTrainItem(TrainEditItem trainEditItem);


    @InheritConfiguration
    void mapTrainItem(TrainEditItem trainEditItem, @MappingTarget TrainItem trainItem);

    TrainEditItem toTrainEditItem(TrainItem trainItem);


    @Mappings({
            @Mapping(target = "studyTimeStr", expression = "java(DateTimeUtil.secondToChineseTime(train.getStudyTime()))"),
            @Mapping(target = "startTime", expression = "java(DateTimeUtil.dateTimeFullFormat(train.getStartTime()))"),
            @Mapping(target = "endTime", expression = "java(DateTimeUtil.dateTimeFullFormat(train.getEndTime()))"),
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(train.getCreateTime()))")
    })
    TrainDetailVM toTrainDetailVM(Train train);
}
