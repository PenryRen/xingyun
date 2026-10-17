package com.mindskip.wdd.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.ExamPaperTypeEnum;
import com.mindskip.wdd.domain.enums.PaperTypeEnum;
import com.mindskip.wdd.domain.enums.QuestionTypeEnum;
import com.mindskip.wdd.domain.frame.*;
import com.mindskip.wdd.domain.other.QuestionRandomItem;
import com.mindskip.wdd.mapping.ExamPaperMapping;
import com.mindskip.wdd.mapping.QuestionMapping;
import com.mindskip.wdd.repository.*;
import com.mindskip.wdd.service.ExamPaperService;
import com.mindskip.wdd.service.UserService;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoPaperVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoResponseVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoTitle;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperPageRequestVM;
import com.mindskip.wdd.viewmodel.question.QuestionRandom;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 试卷
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@AllArgsConstructor
public class ExamPaperServiceImpl extends ServiceImpl<ExamPaperMapper, ExamPaper> implements ExamPaperService {

    private final static String CACHE_NAME = "ueit:paper";
    private final ExamPaperMapper examPaperMapper;
    private final ExamPaperMapping examPaperMapping;
    private final ExamPaperJsonMapper examPaperJsonMapper;
    private final ExamPaperChildMapper examPaperChildMapper;
    private final QuestionJsonMapper questionJsonMapper;
    private final QuestionMapping questionMapping;
    private final ExamPaperUserCameraMapper examPaperUserCameraMapper;
    private final ExamPaperArchiveMapper examPaperArchiveMapper;
    private final ExamPaperUserMapper examPaperUserMapper;
    private final ExamPaperBuildMapper examPaperBuildMapper;
    private final QuestionMapper questionMapper;
    private final UserService userService;


    @Override
    public List<ExamPaperArchive> selectRootTree() {
        return examPaperArchiveMapper.selectRootTree();
    }

    @Override
    public List<ExamPaperArchive> getByParentId(Integer parentId) {
        return examPaperArchiveMapper.getByParentId(parentId);
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "#paperId", unless = "#result == null || #result.allowCache == false")
    public ExamPaperCache getExamPaperCache(Long paperId, Integer userId) {
        ExamPaper examPaper = examPaperMapper.selectById(paperId);
        if (null == examPaper) {
            return null;
        }
        String paperFrameId = null;
        ExamPaperCache examPaperCache = examPaperMapping.toExamPaperCache(examPaper);
        if (ExamPaperTypeEnum.RANDOM == ExamPaperTypeEnum.fromCode(examPaper.getPaperType())) {//随机组卷不需要缓存
            ExamPaperChild examPaperChild = examPaperChildMapper.selectChildExamPaper(paperId, userId);
            if (null == examPaperChild) { //未创建试卷，生成随机卷
                examPaperChild = randomPaper(examPaper, userId);
            }
            paperFrameId = examPaperChild.getPaperFrameId();
            examPaperCache.setChildExamPaperId(examPaperChild.getId());
            examPaperCache.setAllowCache(false);
        } else {
            paperFrameId = examPaper.getPaperFrameId();
        }
        ExamPaperFrame examPaperFrame = examPaperJsonMapper.selectById(paperFrameId).getContent();
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


    /**
     * 根据题型随机组卷
     *
     * @param examPaperBuildConfig
     * @param examPaperFrame
     */
    @Override
    public void randomExamPaperBuild(ExamPaperBuildConfig examPaperBuildConfig, ExamPaperFrame examPaperFrame) {
        examPaperBuildConfig.setQuestionCount(0);
        Map<Integer, List<ExamPaperBuildRandom>> questionTypeKG = examPaperBuildConfig.getExamPaperBuildRandomList()
                .stream()
                .collect(Collectors.groupingBy(ExamPaperBuildRandom::getQuestionType));
        questionTypeKG.forEach((key, group) -> {
            QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(key);
            ExamPaperItemFrame examPaperItemFrame = new ExamPaperItemFrame();
            examPaperItemFrame.setName(questionTypeEnum.getName());
            examPaperItemFrame.setExamPaperItemQuestionFrames(new ArrayList<>());
            randomByQuestionType(examPaperBuildConfig, questionTypeEnum, group, examPaperItemFrame);
            examPaperFrame.getExamPaperItemFrames().add(examPaperItemFrame);
        });
    }


    @Override
    public ExamPaperDoResponseVM toExamPaperDoResponseVM(ExamPaperCache examPaperCache, User user) {
        ExamPaperDoResponseVM examPaperDoResponseVM = new ExamPaperDoResponseVM();
        ExamPaperFrame examPaperFrame = examPaperCache.getExamPaperFrame();
        List<QuestionFrame> questionFrameList = examPaperCache.getQuestionFrameList();
        List<QuestionAnswerFrame> questionAnswerFrameList = new ArrayList<>(questionFrameList.size());


        ExamPaperAnswerFrame examPaperAnswerFrame = new ExamPaperAnswerFrame();
        examPaperAnswerFrame.setDoTime(0);


        //题序打乱
        if (examPaperCache.getQuestionMess() != null && examPaperCache.getQuestionMess()) {
            messQuestion(examPaperFrame);
        }
        Boolean questionItemMess = examPaperCache.getQuestionItemMess() != null && examPaperCache.getQuestionItemMess();


        ExamPaperDoPaperVM examPaperDoPaperVM = examPaperMapping.toExamPaperDoResponseVM(examPaperCache);
        List<ExamPaperDoTitle> examPaperDoTitleList = new ArrayList<>(examPaperFrame.getExamPaperItemFrames().size());
        examPaperFrame.getExamPaperItemFrames().stream().forEach(pt -> {
            ExamPaperDoTitle title = examPaperMapping.toExamPaperDoTitle(pt);
            List<QuestionFrame> questionFrameVMList = new ArrayList<>(pt.getExamPaperItemQuestionFrames().size());
            pt.getExamPaperItemQuestionFrames().stream()
                    .forEach(paperItemQuestionFrame -> {
                        QuestionFrame questionFrame = questionFrameList.stream().filter(qf -> qf.getId().equals(paperItemQuestionFrame.getQuestionFrameId())).findFirst().get();
                        questionFrame.setTrickScore(paperItemQuestionFrame.getTrickScore());
                        questionFrame.setQuestionId(paperItemQuestionFrame.getId());
                        questionFrame.setItemOrder(paperItemQuestionFrame.getItemOrder());
                        QuestionAnswerFrame questionAnswerFrame = questionFrameToAnswer(questionFrame, questionItemMess);
                        questionFrameVMList.add(questionFrame);
                        questionAnswerFrameList.add(questionAnswerFrame);
                    });
            title.setQuestionFrameList(questionFrameVMList);
            examPaperDoTitleList.add(title);
        });
        examPaperDoPaperVM.setExamPaperDoTitleList(examPaperDoTitleList);
        examPaperDoResponseVM.setPaper(examPaperDoPaperVM);


        examPaperAnswerFrame.setPaperId(examPaperDoPaperVM.getPaperId());
        examPaperAnswerFrame.setQuestionAnswerFrameList(questionAnswerFrameList);
        examPaperAnswerFrame.setChildPaperId(examPaperCache.getChildExamPaperId());
        examPaperDoResponseVM.setAnswer(examPaperAnswerFrame);

        return examPaperDoResponseVM;
    }


    @Override
    public PageInfo<ExamPaper> page(ExamPaperPageRequestVM examPaperPageRequestVM) {
        return PageHelper.startPage(examPaperPageRequestVM.getPageIndex(), examPaperPageRequestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                examPaperMapper.page(examPaperPageRequestVM));
    }

    @Override
    public PageInfo<ExamPaper> resitPage(ExamPaperPageRequestVM examPaperPageRequestVM) {
        return PageHelper.startPage(examPaperPageRequestVM.getPageIndex(), examPaperPageRequestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                examPaperMapper.resitPage(examPaperPageRequestVM));
    }


    @Override
    public RestResponse paperPermissionCheck(PaperTypeEnum paperTypeEnum, ExamPaperCache examPaperCache, User user) {
        if (null == examPaperCache) {
            return RestResponse.fail(2, "试卷未找到！");
        }

        if (examPaperCache.getDeleted()) {
            return RestResponse.fail(4, "试卷未找到");
        }

        Date now = new Date();
        switch (paperTypeEnum) {
            case Official:
                if (now.before(examPaperCache.getLimitStartTime())) {
                    return RestResponse.fail(5, "考试未开始");
                }
                if (now.after(examPaperCache.getLimitEndTime())) {
                    return RestResponse.fail(5, "考试已结束");
                }
                Long paperId = examPaperMapper.selectExamPaperById(examPaperCache.getId(), user.getId(), user.getDepartmentId());
                if (null == paperId) {
                    return RestResponse.fail(6, "没有权限访问试卷");
                }
                break;
            case Resit:
                ExamPaperUser examPaperUser = examPaperUserMapper.getResitExamPaperUser(examPaperCache.getId(), user.getId());
                if (null == examPaperUser) {
                    return RestResponse.fail(6, "没有权限访问试卷");
                }
                if (now.before(examPaperUser.getLimitStartTime())) {
                    return RestResponse.fail(5, "补考未开始");
                }
                if (now.after(examPaperUser.getLimitEndTime())) {
                    return RestResponse.fail(5, "补考已结束");
                }
                break;
        }
        return RestResponse.ok();
    }

    @Override
    public void insertExamPaperUserCamera(ExamPaperUserCamera examPaperUserCamera) {
        examPaperUserCameraMapper.insert(examPaperUserCamera);
    }

    @Override
    public ExamPaperArchive getExamPaperArchiveById(Integer id) {
        return examPaperArchiveMapper.selectById(id);
    }

    @Override
    public List<ExamPaperArchive> getExamPaperArchiveByLevel(String level) {
        return examPaperArchiveMapper.getExamPaperArchiveByLevel(level);
    }


    @Override
    public QuestionAnswerFrame questionFrameToAnswer(QuestionFrame questionFrame, Boolean questionItemMess) {
        QuestionAnswerFrame questionAnswerFrame = questionMapping.toQuestionAnswerFrame(questionFrame);
        questionAnswerFrame.setCompleted(false);
        questionAnswerFrame.setContentArray(new ArrayList<>());
        questionAnswerFrame.setContentArrayKey(new ArrayList<>());
        questionAnswerFrame.setCorrectPrefix(questionFrame.getCorrectPrefix());
        questionAnswerFrame.setQuestionFrameId(questionFrame.getId());
        //题目选项扰乱
        if (questionItemMess) {
            messQuestionItem(questionFrame, questionAnswerFrame);
        }
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(questionFrame.getQuestionType());
        if (questionTypeEnum == QuestionTypeEnum.GapFilling) {
            List<String> emptyAnswer = questionFrame.getQuestionItemFrames().stream().map(item -> "").collect(Collectors.toList());
            questionAnswerFrame.setContentArray(emptyAnswer);
        }
        return questionAnswerFrame;
    }

    @Override
    public QuestionFrame getQuestionFrame(String id) {
        return questionJsonMapper.selectById(id).getContent();
    }

    /**
     * 创建随机试卷
     *
     * @param parentExamPaper
     * @param userId
     * @return {@link ExamPaperChild}
     */
    private ExamPaperChild randomPaper(ExamPaper parentExamPaper, Integer userId) {
        User user = userService.getById(userId);
        ExamPaperBuild examPaperBuild = examPaperBuildMapper.selectById(parentExamPaper.getExamPaperBuildId());
        ExamPaperFrame examPaperFrame = new ExamPaperFrame();
        examPaperFrame.setExamPaperItemFrames(new ArrayList<>());
        randomExamPaperBuild(examPaperBuild.getBuildConfig(), examPaperFrame);
        ExamPaperChild examPaperChild = examPaperMapping.toExamPaperChild(parentExamPaper);
        examPaperChild.setCreateUser(user.getId());
        examPaperChild.setCreateDepartmentId(user.getDepartmentId());
        examPaperChild.setExamPaperId(parentExamPaper.getId());
        examPaperJsonMapper.insert(new ExamPaperJson(examPaperFrame));
        examPaperChild.setPaperFrameId(examPaperFrame.getId());
        examPaperChildMapper.insert(examPaperChild);
        return examPaperChild;
    }

    /**
     * 根据题型，随机抽题，组卷
     *
     * @param examPaperBuildConfig
     * @param questionTypeEnum
     * @param examPaperBuildRandomList
     * @param examPaperItemFrame
     */
    private void randomByQuestionType(ExamPaperBuildConfig examPaperBuildConfig, QuestionTypeEnum questionTypeEnum, List<ExamPaperBuildRandom> examPaperBuildRandomList, ExamPaperItemFrame examPaperItemFrame) {
        examPaperBuildRandomList.forEach(item -> {
            if (null != item.getNumber() && !item.getNumber().equals(0)) {
                QuestionRandom questionRandom = new QuestionRandom(Arrays.asList(item.getQuestionArchiveId()), questionTypeEnum.getCode(), item.getNumber(), item.getDifficult());
                List<QuestionRandomItem> randomQuestionList = questionMapper.questionRandom(questionRandom);
                for (QuestionRandomItem questionRandomItem : randomQuestionList) {
                    examPaperBuildConfig.setQuestionCount(examPaperBuildConfig.getQuestionCount() + 1);
                    ExamPaperItemQuestionFrame examPaperItemQuestionFrame = examPaperMapping.toExamPaperItemQuestionFrame(questionRandomItem);
                    examPaperItemQuestionFrame.setItemOrder(examPaperBuildConfig.getQuestionCount());
                    examPaperItemQuestionFrame.setTrickScore(item.getScore());
                    examPaperItemFrame.getExamPaperItemQuestionFrames().add(examPaperItemQuestionFrame);
                }
            }
        });
    }

    /**
     * 题目打乱
     *
     * @param examPaperFrame
     */
    private void messQuestion(ExamPaperFrame examPaperFrame) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        examPaperFrame.getExamPaperItemFrames().forEach(title -> {
            List<ExamPaperItemQuestionFrame> examPaperItemQuestionFrames = title.getExamPaperItemQuestionFrames();
            int size = examPaperItemQuestionFrames.size();
            List<ExamPaperItemQuestionFrame> randomExamPaperItemQuestionFrames = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                ExamPaperItemQuestionFrame examPaperItemQuestionFrame = RandomUtil.randomEle(examPaperItemQuestionFrames);
                examPaperItemQuestionFrame.setItemOrder(atomicInteger.getAndIncrement());
                examPaperItemQuestionFrames.remove(examPaperItemQuestionFrame);
                randomExamPaperItemQuestionFrames.add(examPaperItemQuestionFrame);
            }
            title.setExamPaperItemQuestionFrames(randomExamPaperItemQuestionFrames);
        });
    }

    /**
     * 选项打乱
     *
     * @param questionFrame
     * @param questionAnswerFrame
     */
    private void messQuestionItem(QuestionFrame questionFrame, QuestionAnswerFrame questionAnswerFrame) {
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(questionFrame.getQuestionType());
        if (questionTypeEnum == QuestionTypeEnum.SingleChoice || questionTypeEnum == QuestionTypeEnum.MultipleChoice || questionTypeEnum == QuestionTypeEnum.UncertainMultipleChoice || questionTypeEnum == QuestionTypeEnum.TrueFalse) {
            List<QuestionItemFrame> questionItemFrames = questionFrame.getQuestionItemFrames();
            if (questionItemFrames.size() >= 2) {
                Integer size = questionItemFrames.size();
                List<QuestionItemFrame> randomQuestionItemFrame = new ArrayList<>(size);
                List<Integer> keyOrder = new ArrayList<>(size);
                List<String> preFixList = questionItemFrames.stream().map(d -> d.getPrefix()).collect(Collectors.toList());
                List<String> correctPrefixList = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    QuestionItemFrame questionItemFrame = RandomUtil.randomEle(questionItemFrames);
                    questionItemFrame.setPrefix(preFixList.get(i));
                    questionItemFrames.remove(questionItemFrame);
                    randomQuestionItemFrame.add(questionItemFrame);
                    keyOrder.add(questionItemFrame.getKey());

                    //获取正确答案的prefix，并保存在answer中
                    switch (questionTypeEnum) {
                        case SingleChoice:
                            if (questionItemFrame.getKey().equals(questionFrame.getCorrectKey())) {
                                correctPrefixList.add(questionItemFrame.getPrefix());
                            }
                            break;
                        case UncertainMultipleChoice:
                        case MultipleChoice:
                            if (questionFrame.getCorrectArrayKey().contains(questionItemFrame.getKey())) {
                                correctPrefixList.add(questionItemFrame.getPrefix());
                            }
                            break;
                        case TrueFalse:
                            if (questionItemFrame.getKey().equals(questionFrame.getCorrectKey())) {
                                correctPrefixList.add(questionItemFrame.getPrefix());
                                questionAnswerFrame.setCorrectContent(questionItemFrame.getContent());
                            }
                            break;
                    }
                }
                questionAnswerFrame.setQuestionItemKeyOrder(keyOrder);
                String correctPrefix = correctPrefixList.stream().collect(Collectors.joining(" "));
                questionAnswerFrame.setCorrectPrefix(correctPrefix);
                questionFrame.setQuestionItemFrames(randomQuestionItemFrame);
            }
        }
    }

}
