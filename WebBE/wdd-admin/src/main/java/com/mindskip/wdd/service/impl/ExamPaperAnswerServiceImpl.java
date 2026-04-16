package com.mindskip.wdd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.configuration.spring.cache.CacheConfig;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.wdd.domain.enums.QuestionTypeEnum;
import com.mindskip.wdd.domain.frame.*;
import com.mindskip.wdd.domain.other.KeyValue;
import com.mindskip.wdd.mapping.ExamPaperAnswerMapping;
import com.mindskip.wdd.mapping.ExamPaperMapping;
import com.mindskip.wdd.repository.*;
import com.mindskip.wdd.service.ExamPaperAnswerService;
import com.mindskip.wdd.service.UserEventLogService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.answer.PaperAnswerPageRequestVM;
import com.mindskip.wdd.viewmodel.answer.PaperAnswerUserPageResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerEditResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerRequest;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoPaperVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoResponseVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoTitle;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 答卷
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@AllArgsConstructor
public class ExamPaperAnswerServiceImpl implements ExamPaperAnswerService {

    private final ExamPaperAnswerMapper examPaperAnswerMapper;
    private final ExamPaperAnswerMapping examPaperAnswerMapping;
    private final ExamPaperAnswerJsonMapper examPaperAnswerJsonMapper;
    private final ExamPaperMapping examPaperMapping;
    private final UserEventLogService userEventLogService;
    private final UserCredentialMapper userCredentialMapper;
    private final ExamPaperUserCameraMapper examPaperUserCameraMapper;
    private final ExamPaperMapper examPaperMapper;

    private final static String Session_CACHE_NAME = "ueit:paper:session";
    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheConfig cacheConfig;
    private static final Logger logger = LoggerFactory.getLogger(ExamPaperAnswerServiceImpl.class);


    @Override
    public ExamPaperAnswer getById(Long id) {
        return examPaperAnswerMapper.selectById(id);
    }

    @Override
    public int updateExamPaperAnswer(ExamPaperAnswer examPaperAnswer) {
        return examPaperAnswerMapper.updateById(examPaperAnswer);
    }

    @Override
    public ExamPaperAnswerStatusEnum getStatus(Long paperAnswerId) {
        Integer status = examPaperAnswerMapper.getStatus(paperAnswerId);
        return ExamPaperAnswerStatusEnum.fromCode(status);
    }


    @Override
    public ExamPaperAnswerEditResponseVM toExamPaperAnswerEditResponseVM(ExamPaperCache examPaperCache, ExamPaperAnswer examPaperAnswer) {
        ExamPaperAnswerInfoResponseVM examPaperAnswerInfoResponseVM = examPaperAnswerMapping.toExamPaperAnswerInfoResponseVM(examPaperAnswer);
        ExamPaperAnswerJson examPaperAnswerJson = examPaperAnswerJsonMapper.selectById(examPaperAnswer.getAnswerFrameId());
        return toExamPaperAnswerEditResponseVM(examPaperAnswerInfoResponseVM, examPaperCache, examPaperAnswerJson.getContent());
    }

    @Override
    public ExamPaperAnswerEditResponseVM toExamPaperAnswerEditResponseVM(ExamPaperAnswerInfoResponseVM examPaperAnswerInfoResponseVM, ExamPaperCache examPaperCache, ExamPaperAnswerFrame examPaperAnswerFrame) {

        List<QuestionAnswerFrame> questionAnswerFrameList = examPaperAnswerFrame.getQuestionAnswerFrameList();
        examPaperAnswerInfoResponseVM.setQuestionAnswerFrameList(questionAnswerFrameList);

        ExamPaperDoResponseVM examPaperDoResponseVM = new ExamPaperDoResponseVM();
        /*ExamPaper examPaper = examPaperCache.getExamPaper();*/
        ExamPaperFrame examPaperFrame = examPaperCache.getExamPaperFrame();
        List<QuestionFrame> questionFrameList = examPaperCache.getQuestionFrameList();
        ExamPaperDoPaperVM examPaperDoPaperVM = examPaperMapping.toExamPaperDoResponseVM(examPaperCache);
        List<ExamPaperDoTitle> examPaperDoTitleList = new ArrayList<>(examPaperFrame.getExamPaperItemFrames().size());


        Integer itemOrder = 0;
        Boolean questionItemMess = examPaperCache.getQuestionItemMess() != null && examPaperCache.getQuestionItemMess();
        for (ExamPaperItemFrame paperItemFrame : examPaperFrame.getExamPaperItemFrames()) {
            ExamPaperDoTitle title = examPaperMapping.toExamPaperDoTitle(paperItemFrame);
            List<QuestionFrame> questionFrameVMList = new ArrayList<>(paperItemFrame.getExamPaperItemQuestionFrames().size());

            for (ExamPaperItemQuestionFrame ignored : paperItemFrame.getExamPaperItemQuestionFrames()) {
                //用户提交顺序
                Long questionId = questionAnswerFrameList.get(itemOrder).getQuestionId();
                ExamPaperItemQuestionFrame sortExamPaperItemQuestionFrame = paperItemFrame.getExamPaperItemQuestionFrames().stream().filter(qf -> qf.getId().equals(questionId)).findFirst().get();
                QuestionFrame questionFrame = questionFrameList.stream().filter(qf -> qf.getQuestionId().equals(questionId)).findFirst().get();
                QuestionAnswerFrame questionAnswerFrame = questionAnswerFrameList.stream().filter(qa -> qa.getQuestionId().equals(questionFrame.getQuestionId())).findFirst().get();
                questionFrameAndAnswer(sortExamPaperItemQuestionFrame, questionFrame, questionAnswerFrame, questionItemMess, ++itemOrder);
                questionFrameVMList.add(questionFrame);
            }

            title.setQuestionFrameList(questionFrameVMList);
            examPaperDoTitleList.add(title);
        }
        examPaperDoPaperVM.setExamPaperDoTitleList(examPaperDoTitleList);
        examPaperDoResponseVM.setPaper(examPaperDoPaperVM);

        ExamPaperAnswerEditResponseVM examPaperAnswerEditResponseVM = new ExamPaperAnswerEditResponseVM();
        examPaperAnswerEditResponseVM.setPaper(examPaperDoPaperVM);
        examPaperAnswerEditResponseVM.setAnswer(examPaperAnswerInfoResponseVM);
        return examPaperAnswerEditResponseVM;
    }


    @Override
    @Transactional
    public Integer judge(ExamPaperAnswerInfoResponseVM examPaperAnswerRequestVM) {
        ExamPaperAnswer examPaperAnswer = examPaperAnswerMapper.selectById(examPaperAnswerRequestVM.getId());

        Integer resultScore = 0;
        Integer questionCorrect = 0;
        List<QuestionAnswerFrame> questionAnswerFrameList = examPaperAnswerRequestVM.getQuestionAnswerFrameList();
        ExamPaperAnswerJson examPaperAnswerJson = examPaperAnswerJsonMapper.selectById(examPaperAnswer.getAnswerFrameId());
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

        examPaperAnswer.setJudgeUser(examPaperAnswerRequestVM.getJudgeUser());
        examPaperAnswer.setUserScore(resultScore);
        examPaperAnswer.setQuestionCorrect(questionCorrect);
        examPaperAnswer.setStatus(ExamPaperAnswerStatusEnum.Complete.getCode());
        if (resultScore >= examPaperAnswer.getPassScore()) {
            examPaperAnswer.setPassed(true);
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
        } else {
            examPaperAnswer.setPassed(false);
        }
        examPaperAnswerMapper.updateById(examPaperAnswer);

        String logContent = String.format("%s 批改试卷：%s 得分：%s", examPaperAnswerRequestVM.getJudgeUserName(), examPaperAnswer.getPaperName(), ExamUtil.scoreToVM(resultScore));
        UserEventLog userEventLog = new UserEventLog(examPaperAnswerRequestVM.getJudgeUser(), examPaperAnswerRequestVM.getJudgeUserName(), logContent, new Date(), null);
        userEventLogService.save(userEventLog);

        return resultScore;
    }

    @Override
    public Long getNextJudgeId(PaperAnswerPageRequestVM paperAnswerPageRequestVM) {
        return examPaperAnswerMapper.getNextJudgeId(paperAnswerPageRequestVM);
    }

    @Override
    @Transactional
    public void submit(ExamPaperCache examPaperCache, ExamPaperAnswerFrame examPaperAnswerFrame, User user) {
        ExamPaperAnswerJson examPaperAnswerJson = new ExamPaperAnswerJson(examPaperAnswerFrame);
        ExamPaper examPaper = examPaperMapper.selectById(examPaperCache.getId());
        ExamPaperAnswer examPaperAnswer = examPaperAnswerMapping.toExamPaperAnswer(examPaper);
        examPaperAnswer.setDeleted(false);
        examPaperAnswer.setCreateUser(user.getId());
        examPaperAnswer.setCreateDepartmentId(user.getDepartmentId());
        examPaperAnswer.setCreateTime(new Date());
        examPaperAnswer.setDoTime(examPaperAnswerFrame.getDoTime());
        examPaperAnswer.setAnswerFrameId(examPaperAnswerJson.getId());
        examPaperAnswer.setExamPaperChildId(examPaperCache.getChildExamPaperId());

        Boolean needJudge = examPaperCache.getQuestionFrameList().stream().anyMatch(q -> needJudge(q));
        Integer examPaperAnswerStatus = needJudge ? ExamPaperAnswerStatusEnum.WaitJudge.getCode() : ExamPaperAnswerStatusEnum.Complete.getCode();
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
        if (!needJudge) {
            if (rightScore >= examPaperAnswer.getPassScore()) {
                examPaperAnswer.setPassed(true);
            } else {
                examPaperAnswer.setPassed(false);
            }
        }

        //入库
        examPaperAnswerJsonMapper.insert(examPaperAnswerJson);

        examPaperAnswerMapper.insert(examPaperAnswer);

        //发布证书
        if (!needJudge) {
            if (examPaperAnswer.getPassed()) {
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

        String key = cacheConfig.simpleKeyGenerator(Session_CACHE_NAME, String.format("%d_%d", examPaper.getId(), user.getId()));
        redisTemplate.delete(key);
    }


    @Override
    public Integer getAnswerCount(ExamPaperAnswerRequest examPaperAnswerRequest) {
        return examPaperAnswerMapper.getAnswerCount(examPaperAnswerRequest);
    }

    @Override
    public List<Integer> getAnswerScore(ExamPaperAnswerRequest examPaperAnswerRequest) {
        return examPaperAnswerMapper.getAnswerScore(examPaperAnswerRequest);
    }

    @Override
    public PageInfo<PaperAnswerUserPageResponseVM> userAnswerPage(PaperAnswerPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                examPaperAnswerMapper.userAnswerPage(requestVM)
        );
    }


    @Override
    public List<KeyValue> selectMothCount(Date startTime, Date endTime) {
        return examPaperAnswerMapper.selectMothCount(startTime, endTime);
    }

    @Override
    public List<String> examPaperUserCamera(Integer userId, Long examPaperId) {
        return examPaperUserCameraMapper.getCameraImage(userId, examPaperId);
    }

    @Override
    public ExamPaperAnswer getUserAnswer(Integer userId, Long examPaperId) {
        return examPaperAnswerMapper.getUserAnswer(userId, examPaperId);
    }

    @Override
    public void questionFrameAndAnswer(ExamPaperItemQuestionFrame examPaperItemQuestionFrame, QuestionFrame questionFrame, QuestionAnswerFrame questionAnswerFrame, Boolean questionItemMess, Integer itemOrder) {
        questionFrame.setItemOrder(itemOrder);
        questionFrame.setTrickScore(examPaperItemQuestionFrame.getTrickScore());
        //题目选项顺序还原
        if (questionItemMess) {
            try {
                QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(questionFrame.getQuestionType());
                if (questionTypeEnum == QuestionTypeEnum.SingleChoice || questionTypeEnum == QuestionTypeEnum.MultipleChoice || questionTypeEnum == QuestionTypeEnum.UncertainMultipleChoice || questionTypeEnum == QuestionTypeEnum.TrueFalse) {
                    List<QuestionItemFrame> questionItemFrames = questionFrame.getQuestionItemFrames();
                    if (questionItemFrames.size() >= 2) {
                        Integer size = questionItemFrames.size();
                        List<QuestionItemFrame> userQuestionItemFrame = new ArrayList<>(size);
                        List<String> preFixList = questionItemFrames.stream().map(d -> d.getPrefix()).collect(Collectors.toList());
                        List<Integer> keyOrderList = questionAnswerFrame.getQuestionItemKeyOrder();
                        for (int i = 0; i < size; i++) {
                            Integer key = keyOrderList.get(i);
                            QuestionItemFrame questionItemFrame = questionItemFrames.stream().filter(qi -> qi.getKey().equals(key)).findFirst().get();
                            questionItemFrame.setPrefix(preFixList.get(i));
                            userQuestionItemFrame.add(questionItemFrame);
                        }
                        questionFrame.setQuestionItemFrames(userQuestionItemFrame);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }


    /**
     * 题目是否需要批改
     *
     * @param questionFrame
     * @return {@link Boolean}
     */
    private Boolean needJudge(QuestionFrame questionFrame) {
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(questionFrame.getQuestionType());
        if (QuestionTypeEnum.ShortAnswer == questionTypeEnum) {
            return true;
        }
        return false;
    }


}
