package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.wdd.domain.enums.TrainStatusEnum;
import com.mindskip.wdd.domain.frame.ExamPaperAnswerFrame;
import com.mindskip.wdd.domain.frame.ExamPaperFrame;
import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import com.mindskip.wdd.mapping.TrainMapping;
import com.mindskip.wdd.repository.*;
import com.mindskip.wdd.service.ExamPaperAnswerService;
import com.mindskip.wdd.service.TrainExamPaperService;
import com.mindskip.wdd.service.TrainService;
import com.mindskip.wdd.service.enums.ResultEnum;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerResult;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 9.0.0
 * @description: 培训试卷
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/16 10:45
 */
@Service
@AllArgsConstructor
public class TrainExamPaperServiceImpl extends ServiceImpl<TrainExamPaperMapper, TrainExamPaper> implements TrainExamPaperService {

    private final static String CACHE_NAME = "ueit:train:paper";
    private final TrainService trainService;
    private final TrainExamPaperMapper trainExamPaperMapper;
    private final TrainExamPaperAnswerMapper trainExamPaperAnswerMapper;
    private final ExamPaperJsonMapper examPaperJsonMapper;
    private final QuestionJsonMapper questionJsonMapper;
    private final ExamPaperAnswerJsonMapper examPaperAnswerJsonMapper;
    private final TrainMapping trainMapping;
    private final ExamPaperAnswerService examPaperAnswerService;


    @Override
    @Cacheable(value = CACHE_NAME, key = "#trainPaperId", unless = "#result == null")
    public ExamPaperCache getExamPaperCache(Integer trainPaperId) {
        TrainExamPaper trainExamPaper = trainExamPaperMapper.selectById(trainPaperId);
        if (null == trainExamPaper) {
            return null;
        }
        ExamPaperCache examPaperCache = trainMapping.toExamPaperCache(trainExamPaper);
        ExamPaperFrame examPaperFrame = examPaperJsonMapper.selectById(trainExamPaper.getPaperFrameId()).getContent();
        examPaperCache.setExamPaperFrame(examPaperFrame);
        List<String> questionFrameIdList = examPaperFrame.getExamPaperItemFrames().stream()
                .flatMap(pt -> pt.getExamPaperItemQuestionFrames().stream()
                        .map(q -> q.getQuestionFrameId()))
                .collect(Collectors.toList());
        List<QuestionFrame> questionFrameList = questionFrameIdList.size() > 0
                ? questionJsonMapper.selectBatchIds(questionFrameIdList).stream().map(d -> d.getContent()).collect(Collectors.toList())
                : new ArrayList<>(0);
        examPaperCache.setQuestionFrameList(questionFrameList);
        return examPaperCache;
    }

    @Override
    public ExamPaperAnswerResult submit(ExamPaperCache examPaperCache, ExamPaperAnswerFrame examPaperAnswerFrame, User createUser, TrainItemUser trainItemUser) {

        Integer sumScore = examPaperAnswerFrame.getQuestionAnswerFrameList().stream().mapToInt(questionAnswerFrame -> {
            QuestionFrame questionFrame = examPaperCache.getQuestionFrameList().stream().filter(qf -> qf.getQuestionId().equals(questionAnswerFrame.getQuestionId())).findFirst().get();
            return examPaperAnswerService.questionAnswerJudge(questionAnswerFrame, questionFrame);
        }).sum();
        examPaperAnswerFrame.getQuestionAnswerFrameList().forEach(item -> examPaperAnswerService.xssClear(item));
        ExamPaperAnswerJson examPaperAnswerJson = new ExamPaperAnswerJson(examPaperAnswerFrame);
        TrainExamPaperAnswer trainExamPaperAnswer = new TrainExamPaperAnswer();
        trainExamPaperAnswer.setPassScore(trainItemUser.getPassNumber());
        trainExamPaperAnswer.setPaperName(examPaperCache.getName());
        trainExamPaperAnswer.setQuestionCount(examPaperCache.getQuestionCount());
        trainExamPaperAnswer.setCreateTime(new Date());
        trainExamPaperAnswer.setExamPaperArchiveId(examPaperCache.getExamPaperArchiveId());
        trainExamPaperAnswer.setDeleted(false);
        trainExamPaperAnswer.setTrainExamPaperId(examPaperCache.getId().intValue());
        trainExamPaperAnswer.setPaperScore(examPaperCache.getScore());
        trainExamPaperAnswer.setCreateUser(createUser.getId());
        trainExamPaperAnswer.setCreateDepartmentId(createUser.getDepartmentId());
        trainExamPaperAnswer.setDoTime(examPaperAnswerFrame.getDoTime());
        trainExamPaperAnswer.setAnswerFrameId(examPaperAnswerJson.getId());
        trainExamPaperAnswer.setTrainItemId(trainItemUser.getTrainItemId());
        trainExamPaperAnswer.setTrainId(trainItemUser.getTrainId());

        Boolean needJudge = examPaperCache.getQuestionFrameList().stream().anyMatch(q -> examPaperAnswerService.needJudge(q));
        Integer examPaperAnswerStatus = needJudge ? ExamPaperAnswerStatusEnum.WaitJudge.getCode() : ExamPaperAnswerStatusEnum.Complete.getCode();
        trainExamPaperAnswer.setStatus(examPaperAnswerStatus);


        //正确题数和分数
        Integer rightScore = 0;
        Integer questionCorrect = 0;
        for (int j = 0; j < examPaperAnswerFrame.getQuestionAnswerFrameList().size(); j++) {
            QuestionAnswerFrame item = examPaperAnswerFrame.getQuestionAnswerFrameList().get(j);
            if (item.getDoRight() != null && item.getDoRight()) {
                questionCorrect += 1;
            }
            if (null != item.getCustomerScore()) {  //累计得分
                rightScore += item.getCustomerScore();
            }
        }
        trainExamPaperAnswer.setQuestionCorrect(questionCorrect);
        trainExamPaperAnswer.setUserScore(rightScore);
        trainExamPaperAnswer.setSystemScore(rightScore);
        if (!needJudge) {
            if (rightScore >= trainItemUser.getPassNumber()) {
                trainExamPaperAnswer.setPassed(true);
            } else {
                trainExamPaperAnswer.setPassed(false);
            }
        }
        trainExamPaperAnswer.setTrainItemUserId(trainItemUser.getId());

        examPaperAnswerJsonMapper.insert(examPaperAnswerJson);
        trainExamPaperAnswerMapper.insert(trainExamPaperAnswer);

        //更新培训状态
        if (!needJudge && trainItemUser.getStatus().equals(TrainStatusEnum.Going.getCode())) {
            trainItemUser.setCurrentNumber(rightScore);
            trainItemUser.setStatus(trainExamPaperAnswer.getPassed() ? TrainStatusEnum.Pass.getCode() : TrainStatusEnum.NoPass.getCode());
            trainItemUser.setUserTargetId(trainExamPaperAnswer.getId());
            trainService.paperTrainComplete(trainItemUser);
        }
        return new ExamPaperAnswerResult(ResultEnum.SUCCESS, ExamUtil.scoreToVM(sumScore));
    }

    @Override
    public List<TrainExamPaperAnswer> getPaperAnswer(Long trainItemUserId) {
        return trainExamPaperAnswerMapper.getPaperAnswer(trainItemUserId);
    }

    @Override
    public Integer paperDoCount(Long trainItemUserId) {
        return trainExamPaperAnswerMapper.paperDoCount(trainItemUserId);
    }

}
