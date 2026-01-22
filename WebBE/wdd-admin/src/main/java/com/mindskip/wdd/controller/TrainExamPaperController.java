package com.mindskip.wdd.controller;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.wdd.domain.frame.ExamPaperFrame;
import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import com.mindskip.wdd.mapping.TrainExamPaperMapping;
import com.mindskip.wdd.service.*;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerEditResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperTitleItemVM;
import com.mindskip.wdd.viewmodel.question.QuestionEditRequestVM;
import com.mindskip.wdd.viewmodel.train.exam.answer.NextAnswerRequestVM;
import com.mindskip.wdd.viewmodel.train.exam.answer.TrainPaperAnswerPageRequestVM;
import com.mindskip.wdd.viewmodel.train.exam.answer.TrainPaperAnswerPageResponseVM;
import com.mindskip.wdd.viewmodel.train.exam.paper.TrainExamPaperEditVM;
import com.mindskip.wdd.viewmodel.train.exam.paper.TrainExamPaperPageRequestVM;
import com.mindskip.wdd.viewmodel.train.exam.paper.TrainExamPaperPageResponseVM;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 9.0.0
 * @description: 培训试卷
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/16 10:45
 */
@RestController
@RequestMapping(value = "/api/train/exam/paper")
@AllArgsConstructor
public class TrainExamPaperController extends BaseApiController {


    private final TrainExamPaperService trainExamPaperService;
    private final ExamPaperArchiveService examPaperArchiveService;
    private final TrainExamPaperMapping trainExamPaperMapping;
    private final QuestionService questionService;
    private final DepartmentService departmentService;
    private final UserService userService;


    /**
     * 培训试卷分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/page")
    @PreAuthorize("train:exam:paper:page")
    public RestResponse<PageInfo<TrainExamPaperPageResponseVM>> page(@RequestBody TrainExamPaperPageRequestVM model) {
        initPermission(model);
        PageInfo<TrainExamPaper> pageInfo = trainExamPaperService.page(model);
        PageInfo<TrainExamPaperPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> {
            TrainExamPaperPageResponseVM trainExamPaperPageResponseVM = trainExamPaperMapping.toTrainExamPaperPageResponseVM(d);
            if (d.getExamPaperArchiveId() != null) {
                ExamPaperArchive trainExamPaperArchive = examPaperArchiveService.getById(d.getExamPaperArchiveId());
                trainExamPaperPageResponseVM.setLevel(trainExamPaperArchive.getLevel());
            }
            return trainExamPaperPageResponseVM;
        });
        return RestResponse.ok(page);
    }

    /**
     * 试卷查询
     *
     * @param id the id
     * @return rest response
     */
    @PostMapping("/select/{id}")
    @PreAuthorize({"train:exam:paper:create", "train:exam:paper:update"})
    public RestResponse<TrainExamPaperEditVM> select(@PathVariable Integer id) {
        TrainExamPaper trainExamPaper = trainExamPaperService.getById(id);
        TrainExamPaperEditVM trainExamPaperEditVM = trainExamPaperMapping.toTrainExamPaperEditVM(trainExamPaper);
        ExamPaperFrame examPaperFrame = trainExamPaperService.getExamPaperFrame(trainExamPaper.getPaperFrameId());
        List<ExamPaperTitleItemVM> examPaperTitleItemVMList = examPaperFrame.getExamPaperItemFrames().stream().map(examPaperItemFrame -> {
            ExamPaperTitleItemVM examPaperTitleItemVM = new ExamPaperTitleItemVM();
            examPaperTitleItemVM.setName(examPaperItemFrame.getName());
            List<QuestionEditRequestVM> questionEditRequestVMList = examPaperItemFrame.getExamPaperItemQuestionFrames().stream()
                    .map(examPaperItemQuestionFrame -> {
                        QuestionEditRequestVM questionEditRequestVM = questionService.selectQuestionEditRequestVM(examPaperItemQuestionFrame.getId());
                        questionEditRequestVM.setScore(examPaperItemQuestionFrame.getTrickScore());
                        questionEditRequestVM.setItemOrder(examPaperItemQuestionFrame.getItemOrder());
                        return questionEditRequestVM;
                    })
                    .collect(Collectors.toList());
            examPaperTitleItemVM.setQuestionItems(questionEditRequestVMList);
            return examPaperTitleItemVM;
        }).collect(Collectors.toList());
        trainExamPaperEditVM.setTitleItems(examPaperTitleItemVMList);
        return RestResponse.ok(trainExamPaperEditVM);
    }


    /**
     * 试卷创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create")
    @PreAuthorize("train:exam:paper:create")
    public RestResponse create(@RequestBody @Valid TrainExamPaperEditVM model) {
        RestResponse valid = paperValid(model);
        if (valid.getCode() != SystemCode.OK.getCode()) {
            return valid;
        }
        trainExamPaperService.insertTrainExamPaper(model, getCurrentUser());
        return RestResponse.ok();
    }


    /**
     * 试卷编辑
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update")
    @PreAuthorize("train:exam:paper:update")
    public RestResponse update(@RequestBody @Valid TrainExamPaperEditVM model) {
        RestResponse valid = paperValid(model);
        if (valid.getCode() != SystemCode.OK.getCode()) {
            return valid;
        }

        trainExamPaperService.updateTrainExamPaper(model, getCurrentUser());
        return RestResponse.ok();
    }

    /**
     * 培训试卷删除
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("train:exam:paper:delete")
    public RestResponse delete(@PathVariable Integer id) {
        TrainExamPaper trainExamPaper = trainExamPaperService.getById(id);
        trainExamPaper.setDeleted(true);
        trainExamPaperService.updateById(trainExamPaper);
        return RestResponse.ok();
    }


    /**
     * 培训试卷用户分页
     *
     * @param model
     * @return {@link RestResponse}<{@link PageInfo}<{@link TrainPaperAnswerPageResponseVM}>>
     */
    @PostMapping("/answer/page")
    @PreAuthorize("train:course:detail:select")
    public RestResponse<PageInfo<TrainPaperAnswerPageResponseVM>> answerPage(@RequestBody @Valid TrainPaperAnswerPageRequestVM model) {
        if (StringUtils.isNotBlank(model.getMinScoreStr())) {
            model.setMinScore(ExamUtil.scoreFromVM(model.getMinScoreStr()));
        }
        if (StringUtils.isNotBlank(model.getMaxScoreStr())) {
            model.setMaxScore(ExamUtil.scoreFromVM(model.getMaxScoreStr()));
        }
        initPermission(model);
        PageInfo<TrainExamPaperAnswer> pageInfo = trainExamPaperService.answerPage(model);
        PageInfo<TrainPaperAnswerPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, item -> {
            TrainPaperAnswerPageResponseVM examPaperPageResponseVM = trainExamPaperMapping.toTrainPaperAnswerPageResponseVM(item);
            if (null != item.getPaperScore()) {
                examPaperPageResponseVM.setPaperScoreStr(ExamUtil.scoreToVM(item.getPaperScore()));
            }
            if (null != item.getUserScore()) {
                examPaperPageResponseVM.setUserScoreStr(ExamUtil.scoreToVM(item.getUserScore()));
            }
            if (null != item.getDoTime()) {
                examPaperPageResponseVM.setDoTimeStr(ExamUtil.secondToVM(item.getDoTime()));
            }
            if (null != item.getStatus()) {
                examPaperPageResponseVM.setStatusStr(ExamPaperAnswerStatusEnum.fromCode(item.getStatus()).getName());
            }
            if (null != item.getPassed()) {
                examPaperPageResponseVM.setPassStr(item.getPassed() ? "是" : "否");
            }
            if (null != item.getCreateTime()) {
                examPaperPageResponseVM.setCreateTimeStr(DateTimeUtil.dateTimeFullFormat(item.getCreateTime()));
            }
            if (null != item.getCreateDepartmentId()) {
                Department department = departmentService.getById(item.getCreateDepartmentId());
                examPaperPageResponseVM.setDepartmentLevel(department.getLevel());
            }
            User user = userService.getById(item.getCreateUser());
            examPaperPageResponseVM.setUserName(user.getUserName());
            examPaperPageResponseVM.setRealName(user.getRealName());
            return examPaperPageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 培训答卷读取
     *
     * @param id
     * @return {@link RestResponse}
     */
    @PostMapping("/answer/read/{id}")
    public RestResponse read(@PathVariable Long id) {
        TrainExamPaperAnswer trainExamPaperAnswer = trainExamPaperService.trainExamPaperAnswerById(id);
        ExamPaperCache examPaperCache = trainExamPaperService.getExamPaperCache(trainExamPaperAnswer.getTrainExamPaperId());
        ExamPaperAnswerEditResponseVM examPaperAnswerEditResponseVM = trainExamPaperService.toExamPaperAnswerEditResponseVM(examPaperCache, trainExamPaperAnswer);

        User user = userService.getById(trainExamPaperAnswer.getCreateUser());
        examPaperAnswerEditResponseVM.setUserName(user.getUserName());
        examPaperAnswerEditResponseVM.setRealName(user.getRealName());
        examPaperAnswerEditResponseVM.setWorkNo(user.getWorkNo());
        examPaperAnswerEditResponseVM.setJobTitle(user.getJobTitle());
        examPaperAnswerEditResponseVM.setIdCard(user.getIdCard());
        if (null != user.getDepartmentId()) {
            Department department = departmentService.getById(user.getDepartmentId());
            examPaperAnswerEditResponseVM.setDepartmentLevel(department.getLevel());
        }
        if (null != trainExamPaperAnswer.getJudgeUser()) {
            User judgeUser = userService.getById(trainExamPaperAnswer.getJudgeUser());
            examPaperAnswerEditResponseVM.setJudgeUser(String.format("%s（%s）", judgeUser.getRealName(), judgeUser.getUserName()));
        }
        return RestResponse.ok(examPaperAnswerEditResponseVM);
    }


    /**
     * 培训答卷批改
     *
     * @param examPaperAnswerRequestVM the exam paper answer request vm
     * @return the rest response
     */
    @PostMapping("/answer/edit")
    @PreAuthorize("train:course:detail:judge")
    public RestResponse answerEdit(@RequestBody ExamPaperAnswerInfoResponseVM examPaperAnswerRequestVM) {
        for (QuestionAnswerFrame questionAnswerFrame : examPaperAnswerRequestVM.getQuestionAnswerFrameList()) {
            if (questionAnswerFrame.getDoRight() == null && StringUtils.isEmpty(questionAnswerFrame.getJudgeScoreVM())) {
                return RestResponse.fail(2, "有未批改题目！");
            }
        }
        TrainExamPaperAnswer trainExamPaperAnswer = trainExamPaperService.trainExamPaperAnswerById(examPaperAnswerRequestVM.getId());
        ExamPaperAnswerStatusEnum examPaperAnswerStatusEnum = ExamPaperAnswerStatusEnum.fromCode(trainExamPaperAnswer.getStatus());
        if (ExamPaperAnswerStatusEnum.WaitJudge != examPaperAnswerStatusEnum) {
            return RestResponse.fail(3, "该试卷状态不能批改");
        }
        User user = getCurrentUser();
        examPaperAnswerRequestVM.setJudgeUser(user.getId());
        examPaperAnswerRequestVM.setJudgeUserName(user.getUserName());
        Integer resultScore = trainExamPaperService.judge(examPaperAnswerRequestVM);
        return RestResponse.ok(ExamUtil.scoreToVM(resultScore));
    }


    /**
     * 获取下一张批改试卷id
     *
     * @param id id
     * @return {@link RestResponse}
     */
    @PostMapping("/next/judge/{id}")
    public RestResponse answerNextAnswerId(@PathVariable Long id) {
        TrainExamPaperAnswer trainExamPaperAnswer = trainExamPaperService.trainExamPaperAnswerById(id);
        NextAnswerRequestVM paperAnswerPageRequestVM = new NextAnswerRequestVM();
        paperAnswerPageRequestVM.setId(id);
        paperAnswerPageRequestVM.setTrainId(trainExamPaperAnswer.getTrainId());
        initPermission(paperAnswerPageRequestVM);
        Long nextId = trainExamPaperService.nextJudgeAnswerId(paperAnswerPageRequestVM);
        if (null != nextId) {
            return RestResponse.ok(nextId);
        } else {
            return RestResponse.fail(2, "没有可批改的试卷了");
        }
    }

    /**
     * 试卷参数校验
     *
     * @param model
     * @return
     */
    private RestResponse paperValid(TrainExamPaperEditVM model) {
        if (null != model.getCheat() && model.getCheat()) {
            if (null == model.getMaxCheatCount()) {
                return RestResponse.fail(2, "防作弊次数不能为空");
            }
        } else {
            model.setMaxCheatCount(null);
        }
        return RestResponse.ok();
    }

}
