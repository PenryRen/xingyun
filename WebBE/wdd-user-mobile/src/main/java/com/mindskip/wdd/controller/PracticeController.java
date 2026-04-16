package com.mindskip.wdd.controller;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.wdd.domain.frame.ExamPaperAnswerFrame;
import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import com.mindskip.wdd.mapping.PracticeBuildMapping;
import com.mindskip.wdd.service.ExamPaperService;
import com.mindskip.wdd.service.PracticeService;
import com.mindskip.wdd.service.SystemService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerEditResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerPageRequestVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerPageResponseVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoResponseVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperPageRequestVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperPageResponseVM;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 9.5.0
 * @description: 模拟练习考试
 * Copyright (C), 2025, 麟航团队
 * @date 2025/09/11 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/practice")
public class PracticeController extends BaseApiController {

    private final SystemService systemService;
    private final ExamPaperService examPaperService;
    private final PracticeService practiceService;
    private final PracticeBuildMapping practiceBuildMapping;


    /**
     * 模拟练习分页
     *
     * @param model
     * @return {@link RestResponse}<{@link PageInfo}<{@link ExamPaperPageResponseVM}>>
     */
    @PostMapping("/page")
    public RestResponse<PageInfo<ExamPaperPageResponseVM>> pageList(@RequestBody ExamPaperPageRequestVM model) {
        if (model.getExamPaperArchiveId() != null) {
            ExamPaperArchive rootExamPaperArchive = examPaperService.getExamPaperArchiveById(model.getExamPaperArchiveId());
            List<ExamPaperArchive> examPaperArchiveList = examPaperService.getExamPaperArchiveByLevel(rootExamPaperArchive.getLevel());
            List<Integer> examPaperArchiveIdList = examPaperArchiveList.stream().map(item -> item.getId()).collect(Collectors.toList());
            model.setExamPaperArchiveIdList(examPaperArchiveIdList);
        }
        User user = getCurrentUser();
        model.setUserId(user.getId());
        model.setDepartmentId(user.getDepartmentId());
        model.setNow(new Date());
        PageInfo<PracticeBuild> pageInfo = practiceService.page(model);
        PageInfo<ExamPaperPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, item -> practiceBuildMapping.toExamPaperPageResponseVM(item));
        return RestResponse.ok(page);
    }



    /**
     * 模拟练习答卷分页
     *
     * @param model
     * @return {@link RestResponse}<{@link PageInfo}<{@link ExamPaperAnswerPageResponseVM}>>
     */
    @PostMapping("/answer/page")
    public RestResponse<PageInfo<ExamPaperAnswerPageResponseVM>> answerPageList(@RequestBody @Valid ExamPaperAnswerPageRequestVM model) {
        if (model.getExamPaperArchiveId() != null) {
            ExamPaperArchive rootExamPaperArchive = examPaperService.getExamPaperArchiveById(model.getExamPaperArchiveId());
            List<ExamPaperArchive> examPaperArchiveList = examPaperService.getExamPaperArchiveByLevel(rootExamPaperArchive.getLevel());
            List<Integer> examPaperArchiveIdList = examPaperArchiveList.stream().map(item -> item.getId())
                    .collect(Collectors.toList());
            model.setExamPaperArchiveIdList(examPaperArchiveIdList);
        }

        model.setCreateUser(getCurrentUser().getId());
        PageInfo<PracticeExamPaperAnswer> pageInfo = practiceService.answerPage(model);
        PageInfo<ExamPaperAnswerPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, a -> {
            ExamPaperAnswerPageResponseVM examPaperAnswerPageResponseVM = practiceBuildMapping.toExamPaperAnswerPageResponseVM(a);
            examPaperAnswerPageResponseVM.setStatusStr(ExamPaperAnswerStatusEnum.fromCode(a.getStatus()).getName());
            return examPaperAnswerPageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 试卷详情
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/{id}")
    public RestResponse select(@PathVariable Long id) {
        ExamPaperCache examPaperCache = practiceService.getExamPaperCache(id);
        User user = getCurrentUser();
        ExamPaperDoResponseVM examPaperDoResponseVM = examPaperService.toExamPaperDoResponseVM(examPaperCache, user);
        return RestResponse.ok(systemService.aesEncrypt(examPaperDoResponseVM));
    }


    /**
     * 随机试卷生成
     *
     * @param id
     * @return {@link RestResponse}
     */
    @PostMapping("/random/build/{id}")
    public RestResponse randomBuild(@PathVariable Long id) {
        User user = getCurrentUser();
        PracticeExamPaper randomPaper = practiceService.getWaiteAnswer(id, user.getId());
        if (null == randomPaper) {
            randomPaper = practiceService.randomBuild(id, user);
            if (null == randomPaper) {
                return RestResponse.fail(2, "模拟练练试卷生成失败");
            }
        }
        return RestResponse.ok(randomPaper.getId());
    }


    /**
     * 模拟试卷提交
     *
     * @param encryptV
     * @return the rest response
     */
    @PostMapping("/submit")
    public RestResponse answerSubmit(@RequestBody String encryptV) {
        ExamPaperAnswerFrame examPaperAnswerFrame = systemService.aesDecrypt(encryptV, ExamPaperAnswerFrame.class);
        User user = getCurrentUser();
        ExamPaperCache examPaperCache = practiceService.getExamPaperCache(examPaperAnswerFrame.getPaperId());
        return practiceService.submit(examPaperCache, examPaperAnswerFrame, user);
    }


    /**
     * 模拟试卷查看
     *
     * @param id
     * @return {@link RestResponse}
     */
    @PostMapping("/read/{id}")
    public RestResponse read(@PathVariable Long id) {
        PracticeExamPaperAnswer practiceExamPaperAnswer = practiceService.getPracticeExamPaperAnswer(id);
        if (null == practiceExamPaperAnswer) {
            return RestResponse.fail(2, "试卷未找到！");
        }
        if (!practiceExamPaperAnswer.getCreateUser().equals(getCurrentUser().getId())) {
            return RestResponse.fail(3, "没权限访问试卷！");
        }
        ExamPaperCache examPaperCache = practiceService.getExamPaperCache(practiceExamPaperAnswer.getPracticeExamPaperId());
        ExamPaperAnswerEditResponseVM examPaperAnswerEditResponseVM = practiceService.toExamPaperAnswerEditResponseVM(examPaperCache, practiceExamPaperAnswer);
        return RestResponse.ok(systemService.aesEncrypt(examPaperAnswerEditResponseVM));
    }


    /**
     * 模拟试卷批改
     *
     * @param encryptV
     * @return {@link RestResponse}
     */
    @PostMapping("/edit")
    public RestResponse edit(@RequestBody String encryptV) {
        ExamPaperAnswerInfoResponseVM examPaperAnswerRequestVM = systemService.aesDecrypt(encryptV, ExamPaperAnswerInfoResponseVM.class);
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
        Integer resultScore = practiceService.judge(examPaperAnswerRequestVM, user);
        return RestResponse.ok(ExamUtil.scoreToVM(resultScore));
    }

}
