package com.mindskip.wdd.service.impl;

import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.wdd.domain.enums.ExamPaperTypeEnum;
import com.mindskip.wdd.domain.frame.ExamPaperAnswerFrame;
import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import com.mindskip.wdd.domain.other.PaperSession;
import com.mindskip.wdd.domain.ueit.ExamPaperAnswerError;
import com.mindskip.wdd.mapping.ExamPaperAnswerMapping;
import com.mindskip.wdd.repository.ExamPaperAnswerJsonMapper;
import com.mindskip.wdd.repository.ExamPaperAnswerMonitorMapper;
import com.mindskip.wdd.repository.UserCredentialMapper;
import com.mindskip.wdd.service.*;
import com.mindskip.wdd.service.enums.SessionEnum;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.exam.answer.AnswerMQ;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 消息队列
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Service
@AllArgsConstructor
public class MessageQueueServiceImpl implements MessageQueueService {

    private final ExamPaperService examPaperService;
    private final ExamPaperAnswerService examPaperAnswerService;
    private final ExamPaperAnswerMapping examPaperAnswerMapping;
    private final ExamPaperAnswerJsonMapper examPaperAnswerJsonMapper;
    private final UserEventLogService userEventLogService;
    private final UserCredentialMapper userCredentialMapper;
    private final ExamPaperAnswerMonitorMapper examPaperAnswerMonitorMapper;
    private final PaperSessionService paperSessionService;
    private final AsyncService asyncService;


    @Override
    @Transactional
    public void answerSend(AnswerMQ answerMQ) {
        ExamPaperAnswerFrame examPaperAnswerFrame = answerMQ.getAnswerRequestVM();
        examPaperAnswerFrame.getQuestionAnswerFrameList().forEach(item -> examPaperAnswerService.xssClear(item));
        ExamPaperCache examPaperCache = examPaperService.getExamPaperCache(examPaperAnswerFrame.getPaperId(), answerMQ.getUserId());
        ExamPaperAnswerJson examPaperAnswerJson = new ExamPaperAnswerJson(examPaperAnswerFrame);
        ExamPaperAnswer examPaperAnswer = examPaperAnswerMapping.toExamPaperAnswer(examPaperCache);
        examPaperAnswer.setDeleted(false);
        examPaperAnswer.setCreateUser(answerMQ.getUserId());
        examPaperAnswer.setCreateDepartmentId(answerMQ.getDepartmentId());
        examPaperAnswer.setCreateTime(answerMQ.getCreateTime());
        examPaperAnswer.setDoTime(examPaperAnswerFrame.getDoTime());
        examPaperAnswer.setAnswerFrameId(examPaperAnswerJson.getId());
        examPaperAnswer.setExamPaperChildId(examPaperAnswerFrame.getChildPaperId());

        Boolean needCheck = examPaperCache.getQuestionFrameList().stream().anyMatch(q -> examPaperAnswerService.needCheck(q));
        Boolean needJudge = examPaperCache.getQuestionFrameList().stream().anyMatch(q -> examPaperAnswerService.needJudge(q));

        Integer examPaperAnswerStatus = needCheck ? ExamPaperAnswerStatusEnum.WaitCheck.getCode() :
                needJudge ? ExamPaperAnswerStatusEnum.WaitJudge.getCode() : ExamPaperAnswerStatusEnum.Complete.getCode();
        examPaperAnswer.setStatus(examPaperAnswerStatus);

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
        examPaperAnswer.setQuestionCorrect(questionCorrect);
        examPaperAnswer.setUserScore(rightScore);
        examPaperAnswer.setSystemScore(rightScore);
        if (!needJudge && !needCheck) {
            if (rightScore >= examPaperAnswer.getPassScore()) {
                examPaperAnswer.setPassed(true);
            } else {
                examPaperAnswer.setPassed(false);
            }
        }

        //入库
        examPaperAnswerJsonMapper.insert(examPaperAnswerJson);

        examPaperAnswerService.save(examPaperAnswer);

        //发布证书
        if (!needJudge) {
            if (ObjectUtils.isNotEmpty(examPaperAnswer.getPassed()) && examPaperAnswer.getPassed()) {
                if (null != examPaperAnswer.getCredentialTemplateId()) {
                    UserCredential userCredential = new UserCredential();
                    userCredential.setDeleted(false);
                    userCredential.setCreateTime(new Date());
                    userCredential.setCredentialBuildTime(examPaperAnswer.getCreateTime());
                    userCredential.setUserId(examPaperAnswer.getCreateUser());
                    userCredential.setCreateDepartmentId(examPaperAnswer.getCreateDepartmentId());
                    userCredential.setCredentialTemplateId(examPaperAnswer.getCredentialTemplateId());
                    userCredential.setExamPaperAnswerId(examPaperAnswer.getId());
                    userCredential.setExamPaperName(examPaperAnswer.getPaperName());
                    userCredential.setExamPaperId(examPaperAnswer.getExamPaperId());
                    userCredential.setExamPaperBuildId(examPaperAnswer.getExamPaperBuildId());
                    userCredentialMapper.insert(userCredential);
                }
            }
        }


        String logContent = String.format("%s 提交试卷：%s 得分：%s 耗时：%s", answerMQ.getUserName(), examPaperCache.getName(), ExamUtil.scoreToVM(examPaperAnswer.getUserScore()), ExamUtil.secondToVM(examPaperAnswerFrame.getDoTime()));
        UserEventLog userEventLog = new UserEventLog(answerMQ.getUserId(), answerMQ.getUserName(), logContent, answerMQ.getCreateTime(), answerMQ.getDepartmentId());
        userEventSend(userEventLog);

        if (examPaperAnswer.getPaperType() == ExamPaperTypeEnum.OPERATE.getCode()) {
            //初始化试卷核验对象
            ExamPaperAnswerError error = ExamPaperAnswerError.initExamPaperAnswerError(answerMQ.getUserId(),examPaperAnswerFrame.getVmGuid(), examPaperCache.getVmType(), examPaperAnswerFrame.getPaperId(), examPaperAnswer.getId(), examPaperAnswerFrame.getId());
            //异步核验实训答案
            asyncService.checkAnswer(error);
        }
    }

    @Override
    public void monitor(ExamPaperCache examPaperCache, ExamPaperAnswerFrame examPaperAnswerFrame, User createUser) {
        examPaperAnswerFrame.getQuestionAnswerFrameList().stream().forEach(questionAnswerFrame -> {
            QuestionFrame questionFrame = examPaperCache.getQuestionFrameList().stream().filter(qf -> qf.getQuestionId().equals(questionAnswerFrame.getQuestionId())).findFirst().get();
            examPaperAnswerService.questionAnswerJudge(questionAnswerFrame, questionFrame);
        });

        Boolean monitorInsert = false;
        Date now = new Date();
        ExamPaperAnswerMonitor examPaperAnswerMonitor = examPaperAnswerMonitorMapper.getMonitor(examPaperAnswerFrame.getPaperId(), createUser.getId());
        if (null == examPaperAnswerMonitor) {
            examPaperAnswerMonitor = examPaperAnswerMapping.toExamPaperAnswerMonitor(examPaperCache);
            examPaperAnswerMonitor.setCreateUser(createUser.getId());
            examPaperAnswerMonitor.setCreateDepartmentId(createUser.getDepartmentId());
            examPaperAnswerMonitor.setCreateTime(now);
            examPaperAnswerMonitor.setExamPaperChildId(examPaperCache.getChildExamPaperId());
            monitorInsert = true;
        }

        examPaperAnswerMonitor.setDoTime(examPaperAnswerFrame.getDoTime());
        examPaperAnswerMonitor.setAnswerFrameContent(examPaperAnswerFrame);
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
        examPaperAnswerMonitor.setQuestionCorrect(questionCorrect);
        examPaperAnswerMonitor.setUserScore(rightScore);
        examPaperAnswerMonitor.setSystemScore(rightScore);
        examPaperAnswerMonitor.setUpdateTime(now);
        PaperSession paperSession = paperSessionService.getPaperSession(examPaperCache.getId(), createUser, SessionEnum.Paper);
        if (null != paperSession && null != paperSession.getCheatCount()) {
            examPaperAnswerMonitor.setCheatCount(paperSession.getCheatCount());
        } else {
            examPaperAnswerMonitor.setCheatCount(0);
        }
        if (monitorInsert) {
            examPaperAnswerMonitorMapper.insert(examPaperAnswerMonitor);
        } else {
            examPaperAnswerMonitorMapper.updateMonitor(examPaperAnswerMonitor);
        }
    }

    @Override
    public void userEventSend(UserEventLog userEventLog) {
        userEventLogService.save(userEventLog);
    }


}
