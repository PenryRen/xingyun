package com.mindskip.wdd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.wdd.domain.frame.*;
import com.mindskip.wdd.mapping.ExamPaperMapping;
import com.mindskip.wdd.mapping.PracticeBuildMapping;
import com.mindskip.wdd.repository.*;
import com.mindskip.wdd.service.ExamPaperAnswerService;
import com.mindskip.wdd.service.ExamPaperService;
import com.mindskip.wdd.service.MessageQueueService;
import com.mindskip.wdd.service.PracticeService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerEditResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerPageRequestVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoPaperVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoResponseVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoTitle;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperPageRequestVM;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 9.5.0
 * @description: 模拟练习考试
 * Copyright (C), 2024, 麒技团队
 * @date 2024/09/11 10:45
 */
@Service
@AllArgsConstructor
public class PracticeServiceImpl implements PracticeService {

    private final static String CACHE_NAME = "ueit:practice";
    private final PracticeBuildMapper practiceBuildMapper;
    private final PracticeBuildMapping practiceBuildMapping;
    private final PracticeExamPaperMapper practiceExamPaperMapper;
    private final PracticeExamPaperJsonMapper practiceExamPaperJsonMapper;
    private final QuestionJsonMapper questionJsonMapper;
    private final ExamPaperService examPaperService;
    private final PracticeExamPaperAnswerMapper practiceExamPaperAnswerMapper;
    private final PracticeExamPaperAnswerJsonMapper practiceExamPaperAnswerJsonMapper;
    private final ExamPaperAnswerService examPaperAnswerService;
    private final MessageQueueService messageQueueService;
    private final ExamPaperMapping examPaperMapping;
    private final static Logger logger = LoggerFactory.getLogger(PracticeServiceImpl.class);


    @Override
    public PageInfo<PracticeBuild> page(ExamPaperPageRequestVM examPaperPageRequestVM) {
        return PageHelper.startPage(examPaperPageRequestVM.getPageIndex(), examPaperPageRequestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                practiceBuildMapper.page(examPaperPageRequestVM));
    }


    @Override
    public PageInfo<PracticeExamPaperAnswer> answerPage(ExamPaperAnswerPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                practiceExamPaperAnswerMapper.page(requestVM));
    }


    @Override
    @Cacheable(value = CACHE_NAME, key = "#paperId", unless = "#result == null || #result.allowCache == false")
    public ExamPaperCache getExamPaperCache(Long paperId) {
        PracticeExamPaper practiceExamPaper = practiceExamPaperMapper.selectById(paperId);
        if (null == practiceExamPaper) {
            return null;
        }
        ExamPaperCache examPaperCache = practiceBuildMapping.toExamPaperCache(practiceExamPaper);
        examPaperCache.setAllowCache(false);
        ExamPaperFrame examPaperFrame = practiceExamPaperJsonMapper.selectById(practiceExamPaper.getPracticePaperFrameId()).getContent();
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
    public PracticeExamPaper getWaiteAnswer(Long practiceBuildId, Integer userId) {
        return practiceExamPaperMapper.waiteAnswer(practiceBuildId, userId);
    }

    @Override
    @Transactional
    public PracticeExamPaper randomBuild(Long practiceBuildId, User user) {
        PracticeBuild practiceBuild = practiceBuildMapper.selectById(practiceBuildId);
        if (null == practiceBuild) {
            return null;
        }
        ExamPaperBuildConfig examPaperBuildConfig = practiceBuild.getBuildConfig();
        ExamPaperFrame examPaperFrame = new ExamPaperFrame();
        examPaperFrame.setExamPaperItemFrames(new ArrayList<>());
        examPaperService.randomExamPaperBuild(examPaperBuildConfig, examPaperFrame);
        return savePracticePaper(practiceBuild, examPaperFrame, user);
    }

    @Override
    @Transactional
    public RestResponse submit(ExamPaperCache examPaperCache, ExamPaperAnswerFrame examPaperAnswerFrame, User user) {
        Date now = new Date();
        Integer sumScore = examPaperAnswerFrame.getQuestionAnswerFrameList().stream().mapToInt(questionAnswerFrame -> {
            QuestionFrame questionFrame = examPaperCache.getQuestionFrameList().stream().filter(qf -> qf.getQuestionId().equals(questionAnswerFrame.getQuestionId())).findFirst().get();
            return examPaperAnswerService.questionAnswerJudge(questionAnswerFrame, questionFrame);
        }).sum();
        examPaperAnswerFrame.getQuestionAnswerFrameList().forEach(item -> examPaperAnswerService.xssClear(item));
        PracticeExamPaperAnswerJson practiceExamPaperAnswerJson = new PracticeExamPaperAnswerJson(examPaperAnswerFrame);
        PracticeExamPaperAnswer practiceExamPaperAnswer = practiceBuildMapping.toExamPaperAnswer(examPaperCache);
        practiceExamPaperAnswer.setDeleted(false);
        practiceExamPaperAnswer.setCreateUser(user.getId());
        practiceExamPaperAnswer.setCreateDepartmentId(user.getDepartmentId());
        practiceExamPaperAnswer.setCreateTime(now);
        practiceExamPaperAnswer.setDoTime(examPaperAnswerFrame.getDoTime());
        practiceExamPaperAnswer.setPracticeAnswerFrameId(practiceExamPaperAnswerJson.getId());
        Boolean needJudge = examPaperCache.getQuestionFrameList().stream().anyMatch(q -> examPaperAnswerService.needJudge(q));
        Integer examPaperAnswerStatus = needJudge ? ExamPaperAnswerStatusEnum.WaitJudge.getCode() : ExamPaperAnswerStatusEnum.Complete.getCode();
        practiceExamPaperAnswer.setStatus(examPaperAnswerStatus);


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
        practiceExamPaperAnswer.setQuestionCorrect(questionCorrect);
        practiceExamPaperAnswer.setUserScore(rightScore);
        practiceExamPaperAnswer.setSystemScore(rightScore);
        if (!needJudge) {
            if (rightScore >= practiceExamPaperAnswer.getPassScore()) {
                practiceExamPaperAnswer.setPassed(true);
            } else {
                practiceExamPaperAnswer.setPassed(false);
            }
        }

        practiceExamPaperAnswerJsonMapper.insert(practiceExamPaperAnswerJson);
        practiceExamPaperAnswerMapper.insert(practiceExamPaperAnswer);

        //日志
        String logContent = String.format("%s 提交模拟练习：%s 得分：%s 耗时：%s", user.getUserName(), examPaperCache.getName(), ExamUtil.scoreToVM(practiceExamPaperAnswer.getUserScore()), ExamUtil.secondToVM(examPaperAnswerFrame.getDoTime()));
        UserEventLog userEventLog = new UserEventLog(user.getId(), user.getUserName(), logContent, now, user.getDepartmentId());
        messageQueueService.userEventSend(userEventLog);

        RestResponse response = new RestResponse();
        response.setCode(!needJudge ? SystemCode.OK.getCode() : 2);
        response.setResponse(practiceExamPaperAnswer.getId());
        response.setMessage(ExamUtil.scoreToVM(sumScore));
        return response;
    }

    @Override
    public PracticeExamPaperAnswer getPracticeExamPaperAnswer(Long id) {
        return practiceExamPaperAnswerMapper.selectById(id);
    }

    @Override
    public ExamPaperAnswerEditResponseVM toExamPaperAnswerEditResponseVM(ExamPaperCache examPaperCache, PracticeExamPaperAnswer practiceExamPaperAnswer) {

        //用户答案
        ExamPaperAnswerInfoResponseVM examPaperAnswerInfoResponseVM = practiceBuildMapping.toExamPaperAnswerInfoResponseVM(practiceExamPaperAnswer);
        PracticeExamPaperAnswerJson practiceExamPaperAnswerJson = practiceExamPaperAnswerJsonMapper.selectById(practiceExamPaperAnswer.getPracticeAnswerFrameId());
        ExamPaperAnswerFrame examPaperAnswerFrame = practiceExamPaperAnswerJson.getContent();
        List<QuestionAnswerFrame> questionAnswerFrameList = examPaperAnswerFrame.getQuestionAnswerFrameList();
        examPaperAnswerInfoResponseVM.setQuestionAnswerFrameList(questionAnswerFrameList);


        ExamPaperDoResponseVM examPaperDoResponseVM = new ExamPaperDoResponseVM();
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
                try {
                    Long questionId = questionAnswerFrameList.get(itemOrder).getQuestionId();
                    ExamPaperItemQuestionFrame sortExamPaperItemQuestionFrame = paperItemFrame.getExamPaperItemQuestionFrames().stream().filter(qf -> qf.getId().equals(questionId)).findFirst().get();
                    QuestionFrame questionFrame = questionFrameList.stream().filter(qf -> qf.getQuestionId().equals(questionId)).findFirst().get();
                    QuestionAnswerFrame questionAnswerFrame = questionAnswerFrameList.stream().filter(qa -> qa.getQuestionId().equals(questionFrame.getQuestionId())).findFirst().get();
                    examPaperAnswerService.questionFrameAndAnswer(sortExamPaperItemQuestionFrame, questionFrame, questionAnswerFrame, questionItemMess, ++itemOrder);
                    questionFrameVMList.add(questionFrame);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
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
    public Integer judge(ExamPaperAnswerInfoResponseVM examPaperAnswerRequestVM, User user) {
        PracticeExamPaperAnswer practiceExamPaperAnswer = practiceExamPaperAnswerMapper.selectById(examPaperAnswerRequestVM.getId());

        Integer resultScore = 0;
        Integer questionCorrect = 0;
        List<QuestionAnswerFrame> questionAnswerFrameList = examPaperAnswerRequestVM.getQuestionAnswerFrameList();
        PracticeExamPaperAnswerJson practiceExamPaperAnswerJson = practiceExamPaperAnswerJsonMapper.selectById(practiceExamPaperAnswer.getPracticeAnswerFrameId());
        List<QuestionAnswerFrame> oldQuestionAnswerFrameList = practiceExamPaperAnswerJson.getContent().getQuestionAnswerFrameList();
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

        practiceExamPaperAnswerJson.getContent().setQuestionAnswerFrameList(oldQuestionAnswerFrameList);

        practiceExamPaperAnswer.setJudgeUser(user.getId());
        practiceExamPaperAnswer.setUserScore(resultScore);
        practiceExamPaperAnswer.setQuestionCorrect(questionCorrect);
        practiceExamPaperAnswer.setStatus(ExamPaperAnswerStatusEnum.Complete.getCode());
        if (resultScore >= practiceExamPaperAnswer.getPassScore()) {
            practiceExamPaperAnswer.setPassed(true);
        } else {
            practiceExamPaperAnswer.setPassed(false);
        }
        practiceExamPaperAnswerMapper.updateById(practiceExamPaperAnswer);

        //日志
        String logContent = String.format("%s 批改模拟练习：%s 得分：%s 耗时：%s", user.getUserName(), practiceExamPaperAnswer.getPaperName(), ExamUtil.scoreToVM(practiceExamPaperAnswer.getUserScore()), ExamUtil.secondToVM(practiceExamPaperAnswer.getDoTime()));
        UserEventLog userEventLog = new UserEventLog(user.getId(), user.getUserName(), logContent, new Date(), user.getDepartmentId());
        messageQueueService.userEventSend(userEventLog);

        return resultScore;
    }


    /**
     * 保存模拟练习卷
     *
     * @param practiceBuild
     * @param examPaperFrame
     * @param user
     */
    private PracticeExamPaper savePracticePaper(PracticeBuild practiceBuild, ExamPaperFrame examPaperFrame, User user) {
        practiceExamPaperJsonMapper.insert(new PracticeExamPaperJson(examPaperFrame));
        PracticeExamPaper practiceExamPaper = practiceBuildMapping.toPracticeExamPaper(practiceBuild);
        practiceExamPaper.setCreateTime(new Date());
        practiceExamPaper.setCreateUser(user.getId());
        practiceExamPaper.setCreateDepartmentId(user.getDepartmentId());
        practiceExamPaper.setPracticeBuildId(practiceBuild.getId());
        practiceExamPaper.setPracticePaperFrameId(examPaperFrame.getId());
        practiceExamPaper.setPaperType(practiceBuild.getBuildType());
        practiceExamPaperMapper.insert(practiceExamPaper);
        return practiceExamPaper;
    }

}
