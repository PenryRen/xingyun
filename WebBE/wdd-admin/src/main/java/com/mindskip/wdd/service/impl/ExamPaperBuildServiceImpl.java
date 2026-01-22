package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.ExamPaper;
import com.mindskip.wdd.domain.ExamPaperBuild;
import com.mindskip.wdd.domain.ExamPaperChild;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.enums.ExamPaperBuildStatusEnum;
import com.mindskip.wdd.domain.enums.QuestionTypeEnum;
import com.mindskip.wdd.domain.enums.RangeTypeEnum;
import com.mindskip.wdd.domain.frame.*;
import com.mindskip.wdd.domain.other.QuestionRandomItem;
import com.mindskip.wdd.mapping.ExamPaperBuildMapping;
import com.mindskip.wdd.mapping.ExamPaperMapping;
import com.mindskip.wdd.repository.ExamPaperBuildMapper;
import com.mindskip.wdd.repository.UserApplyMapper;
import com.mindskip.wdd.service.ExamPaperBuildService;
import com.mindskip.wdd.service.ExamPaperService;
import com.mindskip.wdd.service.QuestionService;
import com.mindskip.wdd.service.UserService;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildEditRequestVM;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildPageRequestVM;
import com.mindskip.wdd.viewmodel.question.QuestionRandom;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 组卷规则
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Service
@AllArgsConstructor
public class ExamPaperBuildServiceImpl extends ServiceImpl<ExamPaperBuildMapper, ExamPaperBuild> implements ExamPaperBuildService {

    private final static String CACHE_NAME = "ueit:practice";
    private final ExamPaperBuildMapper examPaperBuildMapper;
    private final ExamPaperBuildMapping examPaperBuildMapping;
    private final ExamPaperMapping examPaperMapping;
    private final ExamPaperService examPaperService;
    private final UserService userService;
    private final QuestionService questionService;
    private final UserApplyMapper userApplyMapper;


    @Override
    public PageInfo<ExamPaperBuild> page(ExamPaperBuildPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), null).doSelectPageInfo(() ->
                examPaperBuildMapper.page(requestVM)
        );
    }

    @Override
    public void insertExamPaperBuild(ExamPaperBuildEditRequestVM examPaperBuildEditRequestVM, User createUser) {
        ExamPaperBuild examPaperBuild = examPaperBuildMapping.toExamPaperBuild(examPaperBuildEditRequestVM);
        examPaperBuild.setCreateTime(new Date());
        examPaperBuild.setCreateUser(createUser.getId());
        examPaperBuild.setCreateDepartmentId(createUser.getDepartmentId());
        examPaperBuild.setDeleted(false);
        examPaperBuild.setBuildConfig(examPaperBuildEditRequestVM.getBuildConfig());
        RangeTypeEnum rangeTypeEnum = RangeTypeEnum.fromCode(examPaperBuildEditRequestVM.getRangeType());
        if (rangeTypeEnum == RangeTypeEnum.Customize) {
            examPaperBuild.setLimitStartTime(DateTimeUtil.parse(examPaperBuildEditRequestVM.getLimitDateTime().get(0)));
            examPaperBuild.setLimitEndTime(DateTimeUtil.parse(examPaperBuildEditRequestVM.getLimitDateTime().get(1)));
        } else if (rangeTypeEnum == RangeTypeEnum.Apply) {
            ExamPaperApplySelect examPaperApplySelect = examPaperBuildEditRequestVM.getBuildConfig().getExamPaperApplySelect();
            examPaperBuild.setLimitStartTime(DateTimeUtil.parse(examPaperApplySelect.getLimitStartTime()));
            examPaperBuild.setLimitEndTime(DateTimeUtil.parse(examPaperApplySelect.getLimitEndTime()));
        }
        examPaperBuild.setBuildStatus(ExamPaperBuildStatusEnum.WaitPublish.getCode());
        examPaperBuildMapper.insert(examPaperBuild);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "#examPaperBuildEditRequestVM.id")
    public void updateExamPaperBuild(ExamPaperBuildEditRequestVM examPaperBuildEditRequestVM, Integer userId) {
        ExamPaperBuild examPaperBuild = examPaperBuildMapper.selectById(examPaperBuildEditRequestVM.getId());
        examPaperBuildMapping.mapExamPaperBuild(examPaperBuildEditRequestVM, examPaperBuild);
        examPaperBuild.setBuildConfig(examPaperBuildEditRequestVM.getBuildConfig());
        RangeTypeEnum rangeTypeEnum = RangeTypeEnum.fromCode(examPaperBuildEditRequestVM.getRangeType());
        if (rangeTypeEnum == RangeTypeEnum.Customize) {
            examPaperBuild.setLimitStartTime(DateTimeUtil.parse(examPaperBuildEditRequestVM.getLimitDateTime().get(0)));
            examPaperBuild.setLimitEndTime(DateTimeUtil.parse(examPaperBuildEditRequestVM.getLimitDateTime().get(1)));
        } else if (rangeTypeEnum == RangeTypeEnum.Apply) {
            ExamPaperApplySelect examPaperApplySelect = examPaperBuildEditRequestVM.getBuildConfig().getExamPaperApplySelect();
            examPaperBuild.setLimitStartTime(DateTimeUtil.parse(examPaperApplySelect.getLimitStartTime()));
            examPaperBuild.setLimitEndTime(DateTimeUtil.parse(examPaperApplySelect.getLimitEndTime()));
        }
        examPaperBuildMapper.updateById(examPaperBuild);
    }

    @Override
    @Transactional
    public void publishExamPaper(ExamPaperBuild examPaperBuild, User createUser) {
        ExamPaperBuildConfig buildConfig = examPaperBuild.getBuildConfig();
        ExamPaperFrame examPaperFrame = new ExamPaperFrame();
        examPaperFrame.setExamPaperItemFrames(new ArrayList<>());
        Integer order = 1;
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

        RangeTypeEnum rangeTyp = RangeTypeEnum.fromCode(examPaperBuild.getRangeType());
        if (rangeTyp == RangeTypeEnum.Apply) {
            ExamPaperBuildConfig examPaperBuildConfig = examPaperBuild.getBuildConfig();
            List<ExamPaperUserSelect> examPaperUserSelectList = userApplyMapper.userIdListByApplyId(examPaperBuildConfig.getExamPaperApplySelect().getId())
                    .stream().map(u -> new ExamPaperUserSelect(u))
                    .collect(Collectors.toList());
            examPaperBuildConfig.setExamPaperUserSelectList(examPaperUserSelectList);
        }
        examPaperService.insertExamPaper(examPaperBuild, examPaperFrame, createUser);
        examPaperBuild.setBuildStatus(ExamPaperBuildStatusEnum.Publish.getCode());
        examPaperBuild.setPublishTime(new Date());
        examPaperBuildMapper.updateById(examPaperBuild);
    }

    @Override
    @Transactional
    public void publishRandomExamPaper(ExamPaperBuild examPaperBuild, User createUser) {
        ExamPaperBuildConfig examPaperBuildConfig = examPaperBuild.getBuildConfig();
        RangeTypeEnum rangeTyp = RangeTypeEnum.fromCode(examPaperBuild.getRangeType());
        List<Integer> userIds = new ArrayList<>();
        if (rangeTyp == RangeTypeEnum.Apply) {
            List<ExamPaperUserSelect> examPaperUserSelectList = userApplyMapper.userIdListByApplyId(examPaperBuildConfig.getExamPaperApplySelect().getId())
                    .stream().map(u -> new ExamPaperUserSelect(u))
                    .collect(Collectors.toList());
            examPaperBuildConfig.setExamPaperUserSelectList(examPaperUserSelectList);
            userIds = userApplyMapper.userIdListByApplyId(examPaperBuildConfig.getExamPaperApplySelect().getId());
        } else if (rangeTyp == RangeTypeEnum.Customize) {
            List<Integer> userIdList = examPaperBuildConfig.getExamPaperUserSelectList().stream()
                    .map(eu -> eu.getId()).collect(Collectors.toList());
            if (examPaperBuildConfig.getDepartmentIdList().size() > 0) {
                List<Integer> departmentUserIdList = userService.getIdByDepartmentIdList(examPaperBuildConfig.getDepartmentIdList());
                userIdList.addAll(departmentUserIdList);
            }
            userIds = userIdList.stream().distinct().collect(Collectors.toList());
        }

        ExamPaper parentExamPaper = examPaperService.insertExamPaper(examPaperBuild, null, createUser);

        //给每个用户创建子随机试卷
        userIds.forEach(userId -> {
            User user = userService.getUserById(userId);
            examPaperBuildConfig.setQuestionCount(0);
            ExamPaperFrame examPaperFrame = new ExamPaperFrame();
            examPaperFrame.setExamPaperItemFrames(new ArrayList<>());
            randomExamPaperBuild(examPaperBuildConfig, examPaperFrame);
            ExamPaperChild examPaperChild = examPaperMapping.toExamPaperChild(parentExamPaper);
            examPaperChild.setCreateUser(user.getId());
            examPaperChild.setCreateDepartmentId(user.getDepartmentId());
            examPaperChild.setExamPaperId(parentExamPaper.getId());
            examPaperService.insertExamPaperChild(examPaperChild, examPaperFrame);
        });
        examPaperBuild.setBuildStatus(ExamPaperBuildStatusEnum.Publish.getCode());
        examPaperBuild.setPublishTime(new Date());
        examPaperBuildMapper.updateById(examPaperBuild);
    }

    /**
     * 根据题型随机组卷
     *
     * @param examPaperBuildConfig
     * @param examPaperFrame
     */
    private void randomExamPaperBuild(ExamPaperBuildConfig examPaperBuildConfig, ExamPaperFrame examPaperFrame) {
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
                List<QuestionRandomItem> randomQuestionList = questionService.questionRandom(new QuestionRandom(Arrays.asList(item.getQuestionArchiveId()), questionTypeEnum.getCode(), item.getNumber(), item.getDifficult()));
                for (QuestionRandomItem questionRandomItem : randomQuestionList) {
                    examPaperBuildConfig.setQuestionCount(examPaperBuildConfig.getQuestionCount() + 1);
                    ExamPaperItemQuestionFrame examPaperItemQuestionFrame = examPaperBuildMapping.toExamPaperItemQuestionFrame(questionRandomItem);
                    examPaperItemQuestionFrame.setItemOrder(examPaperBuildConfig.getQuestionCount());
                    examPaperItemQuestionFrame.setTrickScore(item.getScore());
                    examPaperItemFrame.getExamPaperItemQuestionFrames().add(examPaperItemQuestionFrame);
                }
            }
        });
    }
}
