package com.mindskip.wdd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.wdd.domain.enums.ExamPaperBuildTypeEnum;
import com.mindskip.wdd.domain.frame.*;
import com.mindskip.wdd.mapping.ExamPaperMapping;
import com.mindskip.wdd.mapping.PracticeBuildMapping;
import com.mindskip.wdd.repository.*;
import com.mindskip.wdd.service.ExamPaperAnswerService;
import com.mindskip.wdd.service.PracticeService;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.answer.PaperAnswerPageRequestVM;
import com.mindskip.wdd.viewmodel.answer.PaperAnswerUserPageResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerEditResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerRequest;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildEditRequestVM;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildPageRequestVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoPaperVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoResponseVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoTitle;
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
 * Copyright (C), 2025, 麟航团队
 * @date 2025/09/11 10:45
 */
@Service
@AllArgsConstructor
public class PracticeServiceImpl implements PracticeService {

    private final static String CACHE_NAME = "ueit:practice";
    private final PracticeBuildMapper practiceBuildMapper;
    private final PracticeBuildMapping practiceBuildMapping;
    private final ExamPaperMapping examPaperMapping;
    private final PracticeExamPaperMapper practiceExamPaperMapper;
    private final PracticeExamPaperJsonMapper practiceExamPaperJsonMapper;
    private final PracticeBuildDepartmentMapper practiceBuildDepartmentMapper;
    private final PracticeExamPaperAnswerMapper practiceExamPaperAnswerMapper;
    private final PracticeExamPaperAnswerJsonMapper practiceExamPaperAnswerJsonMapper;
    private final ExamPaperAnswerService examPaperAnswerService;
    private final QuestionJsonMapper questionJsonMapper;
    private final static Logger logger = LoggerFactory.getLogger(PracticeServiceImpl.class);

    @Override
    public PageInfo<PracticeBuild> page(ExamPaperBuildPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), null).doSelectPageInfo(() ->
                practiceBuildMapper.page(requestVM)
        );
    }

    @Override
    public PracticeBuild getById(Long id) {
        return practiceBuildMapper.selectById(id);
    }

    @Override
    @Transactional
    public void insertBuild(ExamPaperBuildEditRequestVM requestVM, User user) {
        PracticeBuild practiceBuild = practiceBuildMapping.toPracticeBuild(requestVM);
        practiceBuild.setCreateTime(new Date());
        practiceBuild.setCreateUser(user.getId());
        practiceBuild.setCreateDepartmentId(user.getDepartmentId());
        practiceBuild.setDeleted(false);
        practiceBuild.setBuildConfig(requestVM.getBuildConfig());
        practiceBuild.setLimitStartTime(DateTimeUtil.parse(requestVM.getLimitDateTime().get(0)));
        practiceBuild.setLimitEndTime(DateTimeUtil.parse(requestVM.getLimitDateTime().get(1)));
        practiceBuildMapper.insert(practiceBuild);
        ExamPaperBuildTypeEnum examPaperBuildTypeEnum = ExamPaperBuildTypeEnum.fromCode(practiceBuild.getBuildType());
        if (examPaperBuildTypeEnum == ExamPaperBuildTypeEnum.MANUAL || examPaperBuildTypeEnum == ExamPaperBuildTypeEnum.EXTRACT) {
            PracticeExamPaper practiceExamPaper = practiceExamPaperInsert(practiceBuild, user);
            practiceBuild.setPracticeExamPaperId(practiceExamPaper.getId());
            practiceBuildMapper.updateById(practiceBuild);
        }
        practiceBuild.getBuildConfig().getDepartmentIdList().forEach(departmentId -> {
            PracticeBuildDepartment practiceBuildDepartment = new PracticeBuildDepartment();
            practiceBuildDepartment.setDepartmentId(departmentId);
            practiceBuildDepartment.setPracticeBuildId(practiceBuild.getId());
            practiceBuildDepartment.setCreateUserId(user.getId());
            practiceBuildDepartment.setCreateDepartmentId(user.getDepartmentId());
            practiceBuildDepartment.setDeleted(false);
            practiceBuildDepartmentMapper.insert(practiceBuildDepartment);
        });
    }

    @Override
    @Transactional
    public void updateBuild(ExamPaperBuildEditRequestVM requestVM, User user) {
        PracticeBuild oldPracticeBuild = practiceBuildMapper.selectById(requestVM.getId());
        practiceBuildMapping.mapPracticeBuild(requestVM, oldPracticeBuild);
        oldPracticeBuild.setBuildConfig(requestVM.getBuildConfig());
        oldPracticeBuild.setLimitStartTime(DateTimeUtil.parse(requestVM.getLimitDateTime().get(0)));
        oldPracticeBuild.setLimitEndTime(DateTimeUtil.parse(requestVM.getLimitDateTime().get(1)));
        practiceBuildMapper.updateById(oldPracticeBuild);

        ExamPaperBuildTypeEnum examPaperBuildTypeEnum = ExamPaperBuildTypeEnum.fromCode(oldPracticeBuild.getBuildType());
        if (examPaperBuildTypeEnum == ExamPaperBuildTypeEnum.MANUAL || examPaperBuildTypeEnum == ExamPaperBuildTypeEnum.EXTRACT) {
            practiceExamPaperUpdate(oldPracticeBuild);
        }

        practiceBuildDepartmentMapper.clearDepartment(oldPracticeBuild.getId());
        oldPracticeBuild.getBuildConfig().getDepartmentIdList().forEach(departmentId -> {
            PracticeBuildDepartment practiceBuildDepartment = new PracticeBuildDepartment();
            practiceBuildDepartment.setDepartmentId(departmentId);
            practiceBuildDepartment.setPracticeBuildId(oldPracticeBuild.getId());
            practiceBuildDepartment.setCreateUserId(user.getId());
            practiceBuildDepartment.setCreateDepartmentId(user.getDepartmentId());
            practiceBuildDepartment.setDeleted(false);
            practiceBuildDepartmentMapper.insert(practiceBuildDepartment);
        });
    }


    @Override
    public void delete(Long id) {
        PracticeBuild practiceBuild = practiceBuildMapper.selectById(id);
        practiceBuild.setDeleted(true);
        practiceBuildMapper.updateById(practiceBuild);
    }


    @Override
    public Integer getAnswerCount(ExamPaperAnswerRequest examPaperAnswerRequest) {
        return practiceExamPaperAnswerMapper.getAnswerCount(examPaperAnswerRequest);
    }


    @Override
    public PageInfo<PaperAnswerUserPageResponseVM> answerPage(PaperAnswerPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                practiceExamPaperAnswerMapper.page(requestVM)
        );
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
        practiceExamPaperAnswerJsonMapper.updateById(practiceExamPaperAnswerJson);

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

        return resultScore;
    }


    /**
     * 模拟练习试卷插入
     *
     * @param practiceBuild
     * @param user
     * @return {@link PracticeExamPaper}
     */
    private PracticeExamPaper practiceExamPaperInsert(PracticeBuild practiceBuild, User user) {
        ExamPaperFrame examPaperFrame = new ExamPaperFrame();
        examPaperFrame.setExamPaperItemFrames(new ArrayList<>());
        Integer order = 1;
        ExamPaperBuildConfig buildConfig = practiceBuild.getBuildConfig();
        for (ExamPaperBuildTitle examPaperBuildTitle : buildConfig.getExamPaperBuildTitleList()) {
            ExamPaperItemFrame examPaperItemFrame = examPaperMapping.toExamPaperItemFrame(examPaperBuildTitle);
            examPaperItemFrame.setExamPaperItemQuestionFrames(new ArrayList<>());
            for (ExamPaperBuildQuestion examPaperBuildQuestion : examPaperBuildTitle.getQuestionItems()) {
                ExamPaperItemQuestionFrame examPaperItemQuestionFrame = examPaperMapping.toExamPaperItemQuestionFrame(examPaperBuildQuestion);
                examPaperItemQuestionFrame.setItemOrder(order);
                examPaperItemFrame.getExamPaperItemQuestionFrames().add(examPaperItemQuestionFrame);
                ++order;
            }
            examPaperFrame.getExamPaperItemFrames().add(examPaperItemFrame);
        }

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


    private void practiceExamPaperUpdate(PracticeBuild practiceBuild) {
        PracticeExamPaper practiceExamPaper = practiceExamPaperMapper.selectById(practiceBuild.getPracticeExamPaperId());
        PracticeExamPaperJson practiceExamPaperJson = practiceExamPaperJsonMapper.selectById(practiceExamPaper.getPracticePaperFrameId());

        ExamPaperFrame examPaperFrame = practiceExamPaperJson.getContent();
        examPaperFrame.setExamPaperItemFrames(new ArrayList<>());
        Integer order = 1;
        ExamPaperBuildConfig buildConfig = practiceBuild.getBuildConfig();
        for (ExamPaperBuildTitle examPaperBuildTitle : buildConfig.getExamPaperBuildTitleList()) {
            ExamPaperItemFrame examPaperItemFrame = examPaperMapping.toExamPaperItemFrame(examPaperBuildTitle);
            examPaperItemFrame.setExamPaperItemQuestionFrames(new ArrayList<>());
            for (ExamPaperBuildQuestion examPaperBuildQuestion : examPaperBuildTitle.getQuestionItems()) {
                ExamPaperItemQuestionFrame examPaperItemQuestionFrame = examPaperMapping.toExamPaperItemQuestionFrame(examPaperBuildQuestion);
                examPaperItemQuestionFrame.setItemOrder(order);
                examPaperItemFrame.getExamPaperItemQuestionFrames().add(examPaperItemQuestionFrame);
                ++order;
            }
            examPaperFrame.getExamPaperItemFrames().add(examPaperItemFrame);
        }
        practiceExamPaperJson.setContent(examPaperFrame);
        practiceExamPaperJsonMapper.updateById(practiceExamPaperJson);
        practiceBuildMapping.mapPracticeExamPaper(practiceBuild, practiceExamPaper);
        practiceExamPaperMapper.updateById(practiceExamPaper);
    }

}
