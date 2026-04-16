package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.train.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @version 1.7.0
 * @description: The interface train mapping.
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = {DateTimeUtil.class, ExamUtil.class})
public interface TrainMapping {

    TrainArchiveVM ToTrainArchiveVM(TrainArchive trainArchive);

    List<TrainArchiveVM> toTrainArchiveVMList(List<TrainArchive> trainArchiveList);

    @Mappings({
            @Mapping(target = "studyTimeStr", expression = "java(DateTimeUtil.secondToChineseTime(train.getStudyTime()))")
    })
    TrainPageResponseVM toTrainPageResponseVM(Train train);


    @Mappings({
            @Mapping(target = "studyTimeStr", expression = "java(DateTimeUtil.secondToChineseTime(train.getStudyTime()))"),
            @Mapping(target = "startTime", expression = "java(DateTimeUtil.dateTimeFullFormat(train.getStartTime()))"),
            @Mapping(target = "endTime", expression = "java(DateTimeUtil.dateTimeFullFormat(train.getEndTime()))")
    })
    TrainDetailVM toTrainDetailVM(Train train);

    TrainDetailItem toTrainDetailItem(TrainItem trainItem);

    @Mappings({
            @Mapping(target = "paperId", source = "id")
    })
    ExamPaperCache toExamPaperCache(TrainExamPaper trainExamPaper);


    @Mappings({
            @Mapping(target = "id", ignore = true)
    })
    TrainExamPaperAnswer toTrainExamPaperAnswer(TrainExamPaper trainExamPaper);


    @Mappings({
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(trainExamPaperAnswer.getCreateTime()))"),
            @Mapping(target = "passScore", expression = "java(ExamUtil.scoreToVM(trainExamPaperAnswer.getPassScore()))"),
            @Mapping(target = "systemScore", expression = "java(ExamUtil.scoreToVM(trainExamPaperAnswer.getSystemScore()))"),
            @Mapping(target = "userScore", expression = "java(ExamUtil.scoreToVM(trainExamPaperAnswer.getUserScore()))"),
            @Mapping(target = "paperScore", expression = "java(ExamUtil.scoreToVM(trainExamPaperAnswer.getPaperScore()))"),
            @Mapping(target = "doTime", expression = "java(ExamUtil.secondToVM(trainExamPaperAnswer.getDoTime()))"),
    })
    TrainExamPaperAnswerVM toTrainExamPaperAnswerVM(TrainExamPaperAnswer trainExamPaperAnswer);

}
