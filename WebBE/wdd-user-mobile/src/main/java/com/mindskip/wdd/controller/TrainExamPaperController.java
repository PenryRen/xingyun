package com.mindskip.wdd.controller;

import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.domain.ExamPaperCache;
import com.mindskip.wdd.domain.TrainItemUser;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.enums.TrainStatusEnum;
import com.mindskip.wdd.domain.frame.ExamPaperAnswerFrame;
import com.mindskip.wdd.service.*;
import com.mindskip.wdd.service.enums.ResultEnum;
import com.mindskip.wdd.service.enums.SessionEnum;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerResult;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoResponseVM;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @version 9.0.0
 * @description: 培训试卷
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@RestController
@RequestMapping(value = "/api/train/exam/paper")
@AllArgsConstructor
public class TrainExamPaperController extends BaseApiController {

    private final SystemService systemService;
    private final PaperSessionService paperSessionService;
    private final TrainService trainService;
    private final TrainExamPaperService trainExamPaperService;
    private final ExamPaperService examPaperService;


    /**
     * 试卷查看
     *
     * @param uid 培训用户id
     * @param pId 课件Id
     * @return the rest response
     */
    @PostMapping("/select/{uid}/{pId}")
    public RestResponse select(@PathVariable @NotNull Long uid, @PathVariable @NotNull Integer pId) {
        User user = getCurrentUser();
        RestResponse response = paperVerify(uid);
        if (response.getCode() != SystemCode.OK.getCode()) {
            return response;
        }
        ExamPaperCache examPaperCache = trainExamPaperService.getExamPaperCache(pId);
        ExamPaperDoResponseVM examPaperDoResponseVM = examPaperService.toExamPaperDoResponseVM(examPaperCache, user);
        paperSessionService.initPaperSession(examPaperDoResponseVM, user, SessionEnum.TrainPaper);
        examPaperDoResponseVM.setInitFirst(examPaperDoResponseVM.getRemainTime().equals(examPaperCache.getSuggestTime() * 60));
        return RestResponse.ok(systemService.aesEncrypt(examPaperDoResponseVM));
    }


    /**
     * 试卷提交
     *
     * @param uid
     * @param encryptV
     * @return the rest response
     */
    @PostMapping("/submit/{uid}")
    public RestResponse answerSubmit(@PathVariable @NotNull Long uid, @RequestBody String encryptV) {
        User user = getCurrentUser();
        RestResponse response = paperVerify(uid);
        if (response.getCode() != SystemCode.OK.getCode()) {
            return response;
        }
        TrainItemUser trainItemUser = (TrainItemUser) response.getResponse();
        ExamPaperAnswerFrame examPaperAnswerFrame = systemService.aesDecrypt(encryptV, ExamPaperAnswerFrame.class);
        Long paperId = examPaperAnswerFrame.getPaperId();
        ExamPaperCache examPaperCache = trainExamPaperService.getExamPaperCache(paperId.intValue());
        paperSessionService.clearPaperSession(paperId, user, SessionEnum.TrainPaper);

        ExamPaperAnswerResult examPaperAnswerResult = trainExamPaperService.submit(examPaperCache, examPaperAnswerFrame, user, trainItemUser);
        if (ResultEnum.FAIL == examPaperAnswerResult.getResultEnum()) {
            return RestResponse.fail(2, examPaperAnswerResult.getMessage());
        }
        return RestResponse.ok(examPaperAnswerResult.getMessage());
    }


    /**
     * 防作弊+1
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/cheat/{id}")
    public RestResponse cheat(@PathVariable Long id) {
        paperSessionService.incrementCheat(id, getCurrentUser(), SessionEnum.TrainPaper);
        return RestResponse.ok();
    }


    /**
     * 培训试卷权限校验
     *
     * @param uid
     * @return {@link RestResponse}
     */
    private RestResponse paperVerify(Long uid) {
        User user = getCurrentUser();
        TrainItemUser trainItemUser = trainService.getTrainItemUser(uid, user.getId(), null);
        if (null == trainItemUser) {
            return RestResponse.fail(2, "试卷未找到");
        }
        TrainStatusEnum trainStatusEnum = TrainStatusEnum.fromCode(trainItemUser.getStatus());
        if (trainStatusEnum != TrainStatusEnum.Going) {
            return RestResponse.fail(2, "试卷已完成");
        }
        Integer goingCount = trainService.trainCourseWareGoingCount(trainItemUser.getTrainId(), user.getId());
        if (null != goingCount && !goingCount.equals(0)) {
            return RestResponse.fail(2, "请先完成课程");
        }
        if (trainItemUser.getAllowCount() != null && trainItemUser.getAllowCount() > 0) {
            Integer doCount = trainExamPaperService.paperDoCount(trainItemUser.getId());
            if (doCount >= trainItemUser.getAllowCount()) {
                return RestResponse.fail(2, "已超过最大考试次数");
            }
        }
        return RestResponse.ok(trainItemUser);
    }


}
