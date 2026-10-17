package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.wdd.domain.enums.TrainStatusEnum;
import com.mindskip.wdd.domain.frame.*;
import com.mindskip.wdd.mapping.TrainExamPaperMapping;
import com.mindskip.wdd.repository.*;
import com.mindskip.wdd.service.ExamPaperAnswerService;
import com.mindskip.wdd.service.TrainExamPaperService;
import com.mindskip.wdd.service.TrainService;
import com.mindskip.wdd.service.UserEventLogService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerEditResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperTitleItemVM;
import com.mindskip.wdd.viewmodel.question.QuestionEditRequestVM;
import com.mindskip.wdd.viewmodel.train.exam.answer.NextAnswerRequestVM;
import com.mindskip.wdd.viewmodel.train.exam.answer.TrainPaperAnswerPageRequestVM;
import com.mindskip.wdd.viewmodel.train.exam.paper.TrainExamPaperEditVM;
import com.mindskip.wdd.viewmodel.train.exam.paper.TrainExamPaperPageRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 9.0.0
 * @description: 培训试卷
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Service
@AllArgsConstructor
public class TrainExamPaperServiceImpl extends ServiceImpl<TrainExamPaperMapper, TrainExamPaper> implements TrainExamPaperService {

    private final TrainExamPaperMapper trainExamPaperMapper;
    private final TrainExamPaperAnswerMapper trainExamPaperAnswerMapper;
    private final ExamPaperJsonMapper examPaperJsonMapper;
    private final TrainExamPaperMapping trainExamPaperMapping;
    private final QuestionJsonMapper questionJsonMapper;
    private final ExamPaperAnswerJsonMapper examPaperAnswerJsonMapper;
    private final ExamPaperAnswerService examPaperAnswerService;
    private final TrainService trainService;
    private final UserEventLogService userEventLogService;


    @Override
    public PageInfo<TrainExamPaper> page(TrainExamPaperPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                trainExamPaperMapper.page(requestVM)
        );
    }

    @Override
    public ExamPaperFrame getExamPaperFrame(String id) {
        return examPaperJsonMapper.selectById(id).getContent();
    }


    @Override
    @Transactional
    public void insertTrainExamPaper(TrainExamPaperEditVM trainExamPaperEditVM, User createUser) {
        resetItemOrder(trainExamPaperEditVM);
        ExamPaperFrame examPaperFrame = new ExamPaperFrame();
        List<ExamPaperItemFrame> examPaperItemFrames = toExamPaperItemFrameList(trainExamPaperEditVM.getTitleItems());
        examPaperFrame.setExamPaperItemFrames(examPaperItemFrames);
        examPaperJsonMapper.insert(new ExamPaperJson(examPaperFrame));

        TrainExamPaper trainExamPaper = trainExamPaperMapping.toTrainExamPaper(trainExamPaperEditVM);
        toTrainExamPaperMapping(trainExamPaper, trainExamPaperEditVM, createUser);
        trainExamPaper.setPaperFrameId(examPaperFrame.getId());
        trainExamPaperMapper.insert(trainExamPaper);
    }

    @Override
    @Transactional
    public void updateTrainExamPaper(TrainExamPaperEditVM trainExamPaperEditVM, User createUser) {
        resetItemOrder(trainExamPaperEditVM);
        TrainExamPaper trainExamPaper = trainExamPaperMapper.selectById(trainExamPaperEditVM.getId());
        ExamPaperJson examPaperJson = examPaperJsonMapper.selectById(trainExamPaper.getPaperFrameId());
        ExamPaperFrame examPaperFrame = examPaperJson.getContent();
        List<ExamPaperItemFrame> examPaperItemFrames = toExamPaperItemFrameList(trainExamPaperEditVM.getTitleItems());
        examPaperFrame.setExamPaperItemFrames(examPaperItemFrames);
        examPaperJsonMapper.updateById(examPaperJson);

        trainExamPaperMapping.toTrainExamPaper(trainExamPaperEditVM, trainExamPaper);
        toTrainExamPaperMapping(trainExamPaper, trainExamPaperEditVM, createUser);
        trainExamPaperMapper.updateById(trainExamPaper);
    }

    @Override
    public int trainExamPaperCount(Integer trainId, Integer status) {
        return trainExamPaperAnswerMapper.trainExamPaperCount(trainId, status);
    }

    @Override
    public PageInfo<TrainExamPaperAnswer> answerPage(TrainPaperAnswerPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                trainExamPaperAnswerMapper.page(requestVM)
        );
    }

    @Override
    public TrainExamPaperAnswer trainExamPaperAnswerById(Long id) {
        return trainExamPaperAnswerMapper.selectById(id);
    }


    @Override
    public ExamPaperCache getExamPaperCache(Integer trainPaperId) {
        TrainExamPaper trainExamPaper = trainExamPaperMapper.selectById(trainPaperId);
        if (null == trainExamPaper) {
            return null;
        }
        ExamPaperCache examPaperCache = trainExamPaperMapping.toExamPaperCache(trainExamPaper);
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
    public ExamPaperAnswerEditResponseVM toExamPaperAnswerEditResponseVM(ExamPaperCache examPaperCache, TrainExamPaperAnswer trainExamPaperAnswer) {
        ExamPaperAnswerInfoResponseVM examPaperAnswerInfoResponseVM = trainExamPaperMapping.toExamPaperAnswerInfoResponseVM(trainExamPaperAnswer);
        ExamPaperAnswerJson examPaperAnswerJson = examPaperAnswerJsonMapper.selectById(trainExamPaperAnswer.getAnswerFrameId());
        return examPaperAnswerService.toExamPaperAnswerEditResponseVM(examPaperAnswerInfoResponseVM, examPaperCache, examPaperAnswerJson.getContent());
    }


    @Override
    @Transactional
    public Integer judge(ExamPaperAnswerInfoResponseVM examPaperAnswerRequestVM) {
        TrainExamPaperAnswer trainExamPaperAnswer = trainExamPaperAnswerMapper.selectById(examPaperAnswerRequestVM.getId());

        Integer resultScore = 0;
        Integer questionCorrect = 0;
        List<QuestionAnswerFrame> questionAnswerFrameList = examPaperAnswerRequestVM.getQuestionAnswerFrameList();
        ExamPaperAnswerJson examPaperAnswerJson = examPaperAnswerJsonMapper.selectById(trainExamPaperAnswer.getAnswerFrameId());
        List<QuestionAnswerFrame> oldQuestionAnswerFrameList = examPaperAnswerJson.getContent().getQuestionAnswerFrameList();
        for (int i = 0; i < questionAnswerFrameList.size(); i++) {
            QuestionAnswerFrame questionAnswerFrame = questionAnswerFrameList.get(i);
            if (questionAnswerFrame.getDoRight() == null) {
                questionAnswerFrame.setCustomerScoreVM(questionAnswerFrame.getJudgeScoreVM());
                questionAnswerFrame.setCustomerScore(ExamUtil.scoreFromVM(questionAnswerFrame.getCustomerScoreVM()));
                questionAnswerFrame.setDoRight(questionAnswerFrame.getCustomerScore().equals(questionAnswerFrame.getQuestionScore()));
                oldQuestionAnswerFrameList.stream()
                        .filter(o -> o.getQuestionId().equals(questionAnswerFrame.getQuestionId()))
                        .findFirst().ifPresent(o -> {
                            o.setCustomerScoreVM(questionAnswerFrame.getCustomerScoreVM());
                            o.setCustomerScore(questionAnswerFrame.getCustomerScore());
                            o.setDoRight(questionAnswerFrame.getDoRight());
                        });
            }
            if (questionAnswerFrame.getDoRight()) {
                ++questionCorrect;
            }
            resultScore += questionAnswerFrame.getCustomerScore();
        }

        examPaperAnswerJson.getContent().setQuestionAnswerFrameList(oldQuestionAnswerFrameList);
        examPaperAnswerJsonMapper.updateById(examPaperAnswerJson);

        trainExamPaperAnswer.setJudgeUser(examPaperAnswerRequestVM.getJudgeUser());
        trainExamPaperAnswer.setUserScore(resultScore);
        trainExamPaperAnswer.setQuestionCorrect(questionCorrect);
        trainExamPaperAnswer.setStatus(ExamPaperAnswerStatusEnum.Complete.getCode());
        if (resultScore >= trainExamPaperAnswer.getPassScore()) {
            trainExamPaperAnswer.setPassed(true);
        } else {
            trainExamPaperAnswer.setPassed(false);
        }
        trainExamPaperAnswerMapper.updateById(trainExamPaperAnswer);


        TrainItemUser trainItemUser = trainService.trainItemUserById(trainExamPaperAnswer.getTrainItemUserId());
        if (null != trainItemUser && trainItemUser.getStatus().equals(TrainStatusEnum.Going.getCode())) {
            trainItemUser.setCurrentNumber(resultScore);
            trainItemUser.setStatus(trainExamPaperAnswer.getPassed() ? TrainStatusEnum.Pass.getCode() : TrainStatusEnum.NoPass.getCode());
            trainItemUser.setUserTargetId(trainExamPaperAnswer.getId());
            trainService.paperTrainComplete(trainItemUser);
        }

        String logContent = String.format("%s 批改试卷：%s 得分：%s", examPaperAnswerRequestVM.getJudgeUserName(), trainExamPaperAnswer.getPaperName(), ExamUtil.scoreToVM(resultScore));
        UserEventLog userEventLog = new UserEventLog(examPaperAnswerRequestVM.getJudgeUser(), examPaperAnswerRequestVM.getJudgeUserName(), logContent, new Date(), null);
        userEventLogService.save(userEventLog);

        return resultScore;
    }

    @Override
    public Long nextJudgeAnswerId(NextAnswerRequestVM nextAnswerRequestVM) {
        return trainExamPaperAnswerMapper.nextJudgeAnswerId(nextAnswerRequestVM);
    }


    /**
     * 试卷基础字段赋值
     *
     * @param trainExamPaper
     * @param trainExamPaperEditVM
     * @param createUser
     */
    private void toTrainExamPaperMapping(TrainExamPaper trainExamPaper, TrainExamPaperEditVM trainExamPaperEditVM, User createUser) {
        List<ExamPaperTitleItemVM> titleItemsVM = trainExamPaperEditVM.getTitleItems();
        Integer questionCount = titleItemsVM.stream().mapToInt(t -> t.getQuestionItems().size()).sum();
        Integer score = titleItemsVM.stream().flatMapToInt(t -> t.getQuestionItems().stream().mapToInt(q -> ExamUtil.scoreFromVM(q.getScore()))).sum();
        trainExamPaper.setCreateTime(new Date());
        trainExamPaper.setCreateUser(createUser.getId());
        trainExamPaper.setDeleted(false);
        trainExamPaper.setCreateDepartmentId(createUser.getDepartmentId());
        trainExamPaper.setQuestionCount(questionCount);
        trainExamPaper.setScore(score);
    }


    /**
     * 试卷模型转化存储模型
     *
     * @param paperTitleItemVMList
     * @return {@link List}<{@link ExamPaperItemFrame}>
     */
    private List<ExamPaperItemFrame> toExamPaperItemFrameList(List<ExamPaperTitleItemVM> paperTitleItemVMList) {
        return paperTitleItemVMList.stream().map(examPaperTitleItemVM -> {
            ExamPaperItemFrame examPaperItemFrame = trainExamPaperMapping.toExamPaperItemFrame(examPaperTitleItemVM);
            List<ExamPaperItemQuestionFrame> examPaperItemQuestionFrames = examPaperTitleItemVM.getQuestionItems().stream().map(titleItem -> {
                ExamPaperItemQuestionFrame examPaperItemQuestionFrame = trainExamPaperMapping.toExamPaperItemQuestionFrame(titleItem);
                return examPaperItemQuestionFrame;
            }).collect(Collectors.toList());
            examPaperItemFrame.setExamPaperItemQuestionFrames(examPaperItemQuestionFrames);
            return examPaperItemFrame;
        }).collect(Collectors.toList());
    }

    /**
     * 重置题目序号
     *
     * @param trainExamPaperEditVM
     */
    private void resetItemOrder(TrainExamPaperEditVM trainExamPaperEditVM) {
        int index = 1;
        for (ExamPaperTitleItemVM ti : trainExamPaperEditVM.getTitleItems()) {
            for (QuestionEditRequestVM qi : ti.getQuestionItems()) {
                qi.setItemOrder(index++);
            }
        }
    }
}
