package com.mindskip.wdd.controller;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.wdd.domain.enums.ExamPaperBuildTypeEnum;
import com.mindskip.wdd.domain.enums.QuestionTypeEnum;
import com.mindskip.wdd.domain.enums.RangeTypeEnum;
import com.mindskip.wdd.domain.frame.*;
import com.mindskip.wdd.mapping.ExamPaperBuildMapping;
import com.mindskip.wdd.mapping.PracticeBuildMapping;
import com.mindskip.wdd.service.*;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.answer.PaperAnswerPageRequestVM;
import com.mindskip.wdd.viewmodel.answer.PaperAnswerUserPageResponseVM;
import com.mindskip.wdd.viewmodel.answer.PaperInfoVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerEditResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerRequest;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildEditRequestVM;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildPageRequestVM;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildPageResponseVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperTitleItemVM;
import com.mindskip.wdd.viewmodel.question.QuestionEditRequestVM;
import com.mindskip.wdd.viewmodel.question.QuestionRandom;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 9.5.0
 * @description: 模拟练习考试
 * Copyright (C), 2024, 麒技团队
 * @date 2024/09/11 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/practice")
public class PracticeController extends BaseApiController {

    private final PracticeService practiceService;
    private final ExamPaperArchiveService examPaperArchiveService;
    private final PracticeBuildMapping practiceBuildMapping;
    private final QuestionArchiveService questionArchiveService;
    private final QuestionService questionService;
    private final ExamPaperBuildMapping examPaperBuildMapping;
    private final DepartmentService departmentService;
    private final UserService userService;

    /**
     * 模拟考试规则分页
     *
     * @param model
     * @return {@link RestResponse}<{@link PageInfo}<{@link ExamPaperBuildPageResponseVM}>>
     */
    @PreAuthorize("practice:build:page")
    @PostMapping("/build/page")
    public RestResponse<PageInfo<ExamPaperBuildPageResponseVM>> buildPage(@RequestBody ExamPaperBuildPageRequestVM model) {
        initPermission(model);
        PageInfo<PracticeBuild> pageInfo = practiceService.page(model);
        PageInfo<ExamPaperBuildPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, examPaperBuild -> {
            ExamPaperBuildPageResponseVM examPaperPageResponseVM = practiceBuildMapping.toExamPaperBuildPageResponseVM(examPaperBuild);
            examPaperPageResponseVM.setBuildTypeStr(ExamPaperBuildTypeEnum.fromCode(examPaperBuild.getBuildType()).getName());
            ExamPaperArchive examPaperArchive = examPaperArchiveService.getById(examPaperBuild.getExamPaperArchiveId());
            if (null != examPaperArchive) {
                examPaperPageResponseVM.setExamPaperArchive(examPaperArchive.getLevel());
            }
            return examPaperPageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 模拟规则查询
     *
     * @param id the id
     * @return rest response
     */
    @PostMapping("/build/select/{id}")
    @PreAuthorize({"practice:build:create", "practice:build:update"})
    public RestResponse<ExamPaperBuildEditRequestVM> buildSelect(@PathVariable Long id) {
        PracticeBuild practiceBuild = practiceService.getById(id);
        ExamPaperBuildEditRequestVM examPaperBuildEditRequestVM = practiceBuildMapping.toExamPaperBuildEditRequestVM(practiceBuild);
        List<String> limitDateTime = Arrays.asList(DateTimeUtil.dateTimeFullFormat(practiceBuild.getLimitStartTime()), DateTimeUtil.dateTimeFullFormat(practiceBuild.getLimitEndTime()));
        examPaperBuildEditRequestVM.setLimitDateTime(limitDateTime);
        examPaperBuildEditRequestVM.setBuildConfig(practiceBuild.getBuildConfig());
        examPaperBuildEditRequestVM.setRangeType(RangeTypeEnum.Customize.getCode());
        List<ExamPaperTitleItemVM> titleItems = practiceBuild.getBuildConfig().getExamPaperBuildTitleList().stream()
                .map(bt -> {
                    ExamPaperTitleItemVM examPaperTitleItemVM = examPaperBuildMapping.toExamPaperTitleItemVM(bt);
                    List<QuestionEditRequestVM> questionItems = bt.getQuestionItems().stream()
                            .map(qi -> {
                                QuestionEditRequestVM questionEditRequestVM = questionService.selectQuestionEditRequestVM(qi.getId());
                                questionEditRequestVM.setScore(qi.getScore());
                                return questionEditRequestVM;
                            }).collect(Collectors.toList());
                    examPaperTitleItemVM.setQuestionItems(questionItems);
                    return examPaperTitleItemVM;
                }).collect(Collectors.toList());
        examPaperBuildEditRequestVM.setTitleItems(titleItems);
        return RestResponse.ok(examPaperBuildEditRequestVM);
    }

    /**
     * 创建模拟规则
     *
     * @param model
     * @return {@link RestResponse}
     */
    @PostMapping("/build/create")
    @PreAuthorize("practice:build:create")
    public RestResponse buildCreate(@RequestBody @Valid ExamPaperBuildEditRequestVM model) {
        RestResponse valid = paperBuildValid(model);
        if (valid.getCode() != SystemCode.OK.getCode()) {
            return valid;
        }
        practiceService.insertBuild(model, getCurrentUser());
        return RestResponse.ok();
    }


    @PostMapping("/build/update")
    @PreAuthorize("practice:build:update")
    public RestResponse buildUpdate(@RequestBody @Valid ExamPaperBuildEditRequestVM model) {
        RestResponse valid = paperBuildValid(model);
        if (valid.getCode() != SystemCode.OK.getCode()) {
            return valid;
        }
        practiceService.updateBuild(model, getCurrentUser());
        return RestResponse.ok();
    }


    @PostMapping("/build/delete/{id}")
    @PreAuthorize("practice:build:delete")
    public RestResponse delete(@PathVariable Long id) {
        practiceService.delete(id);
        return RestResponse.ok();
    }


    /**
     * 模拟练习转正式考试
     *
     * @param id
     * @return {@link RestResponse}<{@link ExamPaperBuildEditRequestVM}>
     */
    @PostMapping("/build/covert/{id}")
    public RestResponse<ExamPaperBuildEditRequestVM> covert(@PathVariable Long id) {
        PracticeBuild practiceBuild = practiceService.getById(id);
        ExamPaperBuildEditRequestVM examPaperBuildEditRequestVM = practiceBuildMapping.toExamPaperBuildEditRequestVM(practiceBuild);
        examPaperBuildEditRequestVM.setLimitDateTime(new ArrayList<>());
        examPaperBuildEditRequestVM.setBuildConfig(practiceBuild.getBuildConfig());
        practiceBuild.getBuildConfig().setDepartmentIdList(new ArrayList<>());
        List<ExamPaperTitleItemVM> titleItems = practiceBuild.getBuildConfig().getExamPaperBuildTitleList().stream()
                .map(bt -> {
                    ExamPaperTitleItemVM examPaperTitleItemVM = examPaperBuildMapping.toExamPaperTitleItemVM(bt);
                    List<QuestionEditRequestVM> questionItems = bt.getQuestionItems().stream()
                            .map(qi -> {
                                QuestionEditRequestVM questionEditRequestVM = questionService.selectQuestionEditRequestVM(qi.getId());
                                questionEditRequestVM.setScore(qi.getScore());
                                return questionEditRequestVM;
                            }).collect(Collectors.toList());
                    examPaperTitleItemVM.setQuestionItems(questionItems);
                    return examPaperTitleItemVM;
                }).collect(Collectors.toList());
        examPaperBuildEditRequestVM.setTitleItems(titleItems);
        examPaperBuildEditRequestVM.setId(null);
        return RestResponse.ok(examPaperBuildEditRequestVM);
    }


    /**
     * 练习详情
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/answer/info/{id}")
    @PreAuthorize("practice:answer:detail")
    public RestResponse info(@PathVariable Long id) {
        PracticeBuild practiceBuild = practiceService.getById(id);
        if (null == practiceBuild) {
            return RestResponse.fail(2, "模拟练习未找到");
        }
        PaperInfoVM paperInfoVM = practiceBuildMapping.toPaperInfoVM(practiceBuild);
        ExamPaperBuildTypeEnum examPaperBuildTypeEnum = ExamPaperBuildTypeEnum.fromCode(practiceBuild.getBuildType());
        paperInfoVM.setBuildTypeStr(examPaperBuildTypeEnum.getName());
        ExamPaperArchive examPaperArchive = examPaperArchiveService.getById(practiceBuild.getExamPaperArchiveId());
        if (null != examPaperArchive) {
            paperInfoVM.setExamPaperArchiveStr(examPaperArchive.getLevel());
        }
        User createUser = userService.getById(practiceBuild.getCreateUser());
        paperInfoVM.setCreateUserStr(String.format("%s - %s", createUser.getRealName(), createUser.getUserName()));
        Integer allUserCount = 0;
        Integer completeCount = practiceService.getAnswerCount(new ExamPaperAnswerRequest(practiceBuild.getId()));
        Integer judgeCount = practiceService.getAnswerCount(new ExamPaperAnswerRequest(practiceBuild.getId(), ExamPaperAnswerStatusEnum.WaitJudge.getCode()));
        Integer passCount = practiceService.getAnswerCount(new ExamPaperAnswerRequest(practiceBuild.getId(), ExamPaperAnswerStatusEnum.Complete.getCode(), true));
        Integer passScoreCount = practiceService.getAnswerCount(new ExamPaperAnswerRequest("sum(user_score)", practiceBuild.getId(), ExamPaperAnswerStatusEnum.Complete.getCode()));
        Integer questionCorrectCount = practiceService.getAnswerCount(new ExamPaperAnswerRequest("sum(question_correct)", practiceBuild.getId(), ExamPaperAnswerStatusEnum.Complete.getCode()));
        paperInfoVM.setPassCount(passCount);
        if (null != completeCount && !completeCount.equals(0)) {
            if (null != passCount) {
                paperInfoVM.setPassPercent(ExamUtil.percentFormat(passCount / (completeCount * 1.0)));
            }
            if (null != questionCorrectCount) {
                paperInfoVM.setCorrectPercent(ExamUtil.percentFormat(questionCorrectCount / (practiceBuild.getQuestionCount() * completeCount * 1.0)));
            }
            if (null != passScoreCount) {
                paperInfoVM.setScorePercent(ExamUtil.percentFormat(passScoreCount / (practiceBuild.getScore() * completeCount * 1.0)));
            }
        }
        paperInfoVM.setAllCount(allUserCount);
        paperInfoVM.setJudgeCount(judgeCount);
        paperInfoVM.setCompleteCount(completeCount);
        return RestResponse.ok(paperInfoVM);
    }


    /**
     * 练习答卷用户成绩
     *
     * @param paperAnswerPageRequestVM the paper answer page request vm
     * @return the rest response
     */
    @PostMapping("/answer/page")
    @PreAuthorize("practice:answer:detail")
    public RestResponse userAnswerPage(@RequestBody PaperAnswerPageRequestVM paperAnswerPageRequestVM) {
        if (StringUtils.isNotBlank(paperAnswerPageRequestVM.getMinScoreStr())) {
            paperAnswerPageRequestVM.setMinScore(ExamUtil.scoreFromVM(paperAnswerPageRequestVM.getMinScoreStr()));
        }
        if (StringUtils.isNotBlank(paperAnswerPageRequestVM.getMaxScoreStr())) {
            paperAnswerPageRequestVM.setMaxScore(ExamUtil.scoreFromVM(paperAnswerPageRequestVM.getMaxScoreStr()));
        }
        PracticeBuild practiceBuild = practiceService.getById(paperAnswerPageRequestVM.getId());
        if (null == practiceBuild) {
            return RestResponse.fail(2, "模拟练习未找到");
        }
        initPermission(paperAnswerPageRequestVM);
        PageInfo<PaperAnswerUserPageResponseVM> pageInfo = practiceService.answerPage(paperAnswerPageRequestVM);
        pageInfo.getList().forEach(ua -> {
            if (null != ua.getPaperScore()) {
                ua.setPaperScoreStr(ExamUtil.scoreToVM(ua.getPaperScore()));
            }
            if (null != ua.getUserScore()) {
                ua.setUserScoreStr(ExamUtil.scoreToVM(ua.getUserScore()));
            }
            if (null != ua.getDoTime()) {
                ua.setDoTimeStr(ExamUtil.secondToVM(ua.getDoTime()));
            }
            if (null != ua.getStatus()) {
                ua.setStatusStr(ExamPaperAnswerStatusEnum.fromCode(ua.getStatus()).getName());
            }
            if (null != ua.getPassed()) {
                ua.setPassStr(ua.getPassed() ? "是" : "否");
            }
            if (null != ua.getCreateTime()) {
                ua.setCreateTimeStr(DateTimeUtil.dateTimeFullFormat(ua.getCreateTime()));
            }
            if (null != ua.getDepartmentId()) {
                Department department = departmentService.getById(ua.getDepartmentId());
                ua.setDepartmentLevel(department.getLevel());
            }
        });
        return RestResponse.ok(pageInfo);
    }


    /**
     * 模拟练习试卷查看
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/answer/read/{id}")
    @PreAuthorize("practice:answer:detail")
    public RestResponse answerRead(@PathVariable Long id) {
        PracticeExamPaperAnswer practiceExamPaperAnswer = practiceService.getPracticeExamPaperAnswer(id);
        ExamPaperCache examPaperCache = practiceService.getExamPaperCache(practiceExamPaperAnswer.getPracticeExamPaperId());
        ExamPaperAnswerEditResponseVM examPaperAnswerEditResponseVM = practiceService.toExamPaperAnswerEditResponseVM(examPaperCache, practiceExamPaperAnswer);
        User user = userService.getById(practiceExamPaperAnswer.getCreateUser());
        examPaperAnswerEditResponseVM.setUserName(user.getUserName());
        examPaperAnswerEditResponseVM.setRealName(user.getRealName());
        examPaperAnswerEditResponseVM.setWorkNo(user.getWorkNo());
        examPaperAnswerEditResponseVM.setJobTitle(user.getJobTitle());
        examPaperAnswerEditResponseVM.setIdCard(user.getIdCard());
        if (null != user.getDepartmentId()) {
            Department department = departmentService.getById(user.getDepartmentId());
            examPaperAnswerEditResponseVM.setDepartmentLevel(department.getLevel());
        }
        if (null != practiceExamPaperAnswer.getJudgeUser()) {
            User judgeUser = userService.getById(practiceExamPaperAnswer.getJudgeUser());
            examPaperAnswerEditResponseVM.setJudgeUser(String.format("%s（%s）", judgeUser.getRealName(), judgeUser.getUserName()));
        }
        return RestResponse.ok(examPaperAnswerEditResponseVM);
    }


    /**
     * 模拟练习批改
     *
     * @param examPaperAnswerRequestVM the exam paper answer request vm
     * @return the rest response
     */
    @PostMapping("/answer/edit")
    @PreAuthorize("practice:answer:detail")
    public RestResponse answerEdit(@RequestBody ExamPaperAnswerInfoResponseVM examPaperAnswerRequestVM) {
        for (QuestionAnswerFrame questionAnswerFrame : examPaperAnswerRequestVM.getQuestionAnswerFrameList()) {
            if (questionAnswerFrame.getDoRight() == null && StringUtils.isEmpty(questionAnswerFrame.getJudgeScoreVM())) {
                return RestResponse.fail(2, "有未批改题目！");
            }
        }
        PracticeExamPaperAnswer practiceExamPaperAnswer = practiceService.getPracticeExamPaperAnswer(examPaperAnswerRequestVM.getId());
        ExamPaperAnswerStatusEnum examPaperAnswerStatusEnum = ExamPaperAnswerStatusEnum.fromCode(practiceExamPaperAnswer.getStatus());
        if (ExamPaperAnswerStatusEnum.WaitJudge != examPaperAnswerStatusEnum) {
            return RestResponse.fail(3, "该试卷状态不能批改");
        }
        User user = getCurrentUser();
        examPaperAnswerRequestVM.setJudgeUser(user.getId());
        examPaperAnswerRequestVM.setJudgeUserName(user.getUserName());
        Integer resultScore = practiceService.judge(examPaperAnswerRequestVM, user);
        return RestResponse.ok(ExamUtil.scoreToVM(resultScore));
    }

    /**
     * 试卷参数检查
     *
     * @param model
     * @return
     */
    private RestResponse paperBuildValid(ExamPaperBuildEditRequestVM model) {
        if (model.getBuildConfig().getDepartmentIdList().size() == 0) {
            return RestResponse.fail(2, "发布部门不能为空");
        }

        if (null == model.getLimitDateTime() || 2 != model.getLimitDateTime().size()) {
            return RestResponse.fail(2, "考试时间不能为空");
        }
        Date end = DateTimeUtil.parse(model.getLimitDateTime().get(1));
        if (new Date().after(end)) {
            return RestResponse.fail(2, "考试结束时间不能小于当前时间");
        }

        model.getBuildConfig().setDepartmentIdList(selectDepartmentFilter(model.getBuildConfig().getDepartmentIdList()));
        List<ExamPaperBuildTitle> examPaperBuildTitleList = model.getTitleItems().stream()
                .map(ti -> {
                    List<ExamPaperBuildQuestion> questionItems = ti.getQuestionItems().stream()
                            .map(qu -> examPaperBuildMapping.toExamPaperBuildQuestion(qu))
                            .collect(Collectors.toList());
                    ExamPaperBuildTitle examPaperBuildTitle = examPaperBuildMapping.toExamPaperBuildTitle(ti);
                    examPaperBuildTitle.setName(ti.getName());
                    examPaperBuildTitle.setQuestionItems(questionItems);
                    return examPaperBuildTitle;
                }).collect(Collectors.toList());
        model.getBuildConfig().setExamPaperBuildTitleList(examPaperBuildTitleList);

        ExamPaperBuildTypeEnum examPaperBuildTypeEnum = ExamPaperBuildTypeEnum.fromCode(model.getBuildType());
        ExamPaperBuildConfig buildConfig = model.getBuildConfig();
        switch (examPaperBuildTypeEnum) {
            case MANUAL:
            case EXTRACT:
                List<ExamPaperBuildTitle> titleList = buildConfig.getExamPaperBuildTitleList();
                if (titleList.size() == 0) {
                    return RestResponse.fail(3, "请添加试卷标题");
                }
                for (ExamPaperBuildTitle title : titleList) {
                    if (StringUtils.isBlank(title.getName())) {
                        return RestResponse.fail(4, "试卷标题不能为空");
                    }
                    if (title.getQuestionItems().size() == 0) {
                        return RestResponse.fail(5, "试卷题目不能为空");
                    }
                }
                Integer questionCount = titleList.stream().mapToInt(title -> title.getQuestionItems().size()).sum();
                model.setQuestionCount(questionCount);
                break;
            case RANDOM:
                if (buildConfig.getExamPaperBuildRandomList().size() == 0) {
                    return RestResponse.fail(2, "出题策略不能为空");
                }
                Integer success = SystemCode.OK.getCode();
                for (ExamPaperBuildRandom examPaperBuildRandom : buildConfig.getExamPaperBuildRandomList()) {
                    RestResponse restResponse = randomCheck(examPaperBuildRandom);
                    if (success != restResponse.getCode()) {
                        return restResponse;
                    }
                }
        }
        return RestResponse.ok();
    }


    /**
     * 随机组卷，参数检查
     *
     * @param examPaperBuildRandom
     * @return
     */
    private RestResponse randomCheck(ExamPaperBuildRandom examPaperBuildRandom) {
        if (examPaperBuildRandom.getDifficult() != null && examPaperBuildRandom.getDifficult().equals(0)) {
            examPaperBuildRandom.setDifficult(null);
        }
        Integer number = examPaperBuildRandom.getNumber();
        String score = examPaperBuildRandom.getScore();

        QuestionArchive questionArchive = questionArchiveService.getById(examPaperBuildRandom.getQuestionArchiveId());
        String errorMsg = String.format("【%s】【%s】【%d】", questionArchive.getName(), QuestionTypeEnum.fromCode(examPaperBuildRandom.getQuestionType()).getName(), examPaperBuildRandom.getDifficult());
        if (number == null || number.equals(0)) {
            if (StringUtils.isEmpty(score) || ExamUtil.scoreFromVM(score).equals(0)) {
                return RestResponse.ok();
            } else {
                return RestResponse.fail(6, errorMsg + "数量输入不正确");
            }
        } else {
            if (StringUtils.isEmpty(score) || ExamUtil.scoreFromVM(score).equals(0)) {
                return RestResponse.fail(6, errorMsg + "分数输入不正确");
            } else {
                QuestionRandom questionRandom = new QuestionRandom(Arrays.asList(examPaperBuildRandom.getQuestionArchiveId()), examPaperBuildRandom.getQuestionType(), examPaperBuildRandom.getDifficult());
                initPermission(questionRandom);
                Long randomCount = questionService.randomQuestionCount(questionRandom);
                if (examPaperBuildRandom.getNumber() > randomCount) {
                    return RestResponse.fail(7, errorMsg + "数量不足，可用题数为：" + randomCount);
                } else {
                    return RestResponse.ok();
                }
            }
        }
    }

}
