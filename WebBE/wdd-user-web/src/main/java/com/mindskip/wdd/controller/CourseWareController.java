package com.mindskip.wdd.controller;

import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.TrainStatusEnum;
import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import com.mindskip.wdd.mapping.CourseWareMapping;
import com.mindskip.wdd.service.*;
import com.mindskip.wdd.viewmodel.common.EncryptKV;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareQuestionRequestVM;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareQuestionVM;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareVM;
import com.mindskip.wdd.viewmodel.course.ware.WatchRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 课件
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/course/ware")
public class CourseWareController extends BaseApiController {

    private final CourseWareService courseWareService;
    private final CourseWareMapping courseWareMapping;
    private final UserService userService;
    private final ExamPaperService examPaperService;
    private final ExamPaperAnswerService examPaperAnswerService;
    private final TrainService trainService;
    private final SystemService systemService;
    private final AsyncService asyncService;


    /**
     * 课件详情
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/{id}")
    public RestResponse select(@PathVariable Integer id) {
        CourseWareWatch courseWareWatch = courseWareService.getCourseWareWatch(getCurrentUser().getId(), id);
        CourseWare course = courseWareService.getById(id);
        CourseWareVM courseWareVM = courseWareMapping.toCourseWareVm(course);
        courseWareVM.setCurrentTime(courseWareWatch == null || courseWareWatch.getWatchCurrentTime() == null ? 0 : courseWareWatch.getWatchCurrentTime());
        User create = userService.getById(course.getCreateUser());
        courseWareVM.setCreateUserName(create.getUserName());
        courseWareVM.setCreateRealName(create.getRealName());
        courseWareVM.setCreateImagePath(create.getImagePath());
        List<CourseWareQuestionVM> courseWareQuestionVMList = courseWareService.getCourseWareQuestion(id);
        courseWareVM.setCourseWareQuestionVMList(courseWareQuestionVMList);
        return systemService.pairTwoObjectEncrypt(courseWareVM);
    }


    /**
     * 课件题目提交
     *
     * @param encryptKV
     * @return {@link RestResponse}
     */
    @PostMapping("/question/submit")
    public RestResponse questionSubmit(@RequestBody EncryptKV encryptKV) {
        CourseWareQuestionRequestVM courseWareQuestionRequestVM = systemService.pairOneObjectDecrypt(encryptKV, CourseWareQuestionRequestVM.class);
        QuestionAnswerFrame questionAnswerFrame = courseWareQuestionRequestVM.getAnswer();
        QuestionFrame questionFrame = examPaperService.getQuestionFrame(questionAnswerFrame.getQuestionFrameId());
        examPaperAnswerService.questionAnswerJudge(questionAnswerFrame, questionFrame);
        return systemService.pairTwoObjectEncrypt(questionAnswerFrame);
    }


    /**
     * 课件观看记录
     *
     * @param requestVM
     * @return the rest response
     */
    @PostMapping("/watch")
    public RestResponse watch(@RequestBody @Valid WatchRequestVM requestVM) {
        Date date = new Date();
        User user = getCurrentUser();
        RestResponse response = new RestResponse(2, "ok");

        CourseWareWatchDetail lastWatchItem = courseWareService.getLastItem(user.getId(), requestVM.getCourseWareId());
        CourseWareWatch courseWareWatch = courseWareService.getCourseWareWatch(user.getId(), requestVM.getCourseWareId());
        Boolean checkWatch = true;
        if (lastWatchItem == null || courseWareWatch == null) {
            checkWatch = false;
        } else {
            if (requestVM.getWatchEnd() && (courseWareWatch.getWatchTotalLength() + courseWareService.getWatchInterval() >= requestVM.getWatchTime())) {
                checkWatch = false;
            }
        }
        if (checkWatch) {
            long watchInterval = (date.getTime() - lastWatchItem.getCreateTime().getTime()) / 1000;
            if (watchInterval <= (courseWareService.getWatchInterval() - 10)) {
                return new RestResponse(3, "观看时间间隔不正确");
            }
        }

        if (null == courseWareWatch) {
            courseWareWatch = new CourseWareWatch();
            courseWareWatch.setCourseWareId(requestVM.getCourseWareId());
            courseWareWatch.setCreateUser(user.getId());
            courseWareWatch.setCreateDepartmentId(user.getDepartmentId());
            courseWareWatch.setCreateTime(date);
            courseWareWatch.setWatchTotalLength(Long.parseLong(courseWareService.getWatchInterval().toString()));
            courseWareWatch.setWatchCurrentTime(requestVM.getWatchTime());
            courseWareService.courseWareWatchInsert(courseWareWatch);
        } else {
            courseWareWatch.setWatchTotalLength(courseWareWatch.getWatchTotalLength() + courseWareService.getWatchInterval());
            courseWareWatch.setWatchCurrentTime(requestVM.getWatchEnd() ? 0 : requestVM.getWatchTime());
            courseWareService.courseWareWatchUpdate(courseWareWatch);
        }

        CourseWareWatchDetail courseWareWatchDetail = new CourseWareWatchDetail();
        courseWareWatchDetail.setCourseWareId(requestVM.getCourseWareId());
        courseWareWatchDetail.setCreateUser(user.getId());
        courseWareWatchDetail.setCreateDepartmentId(user.getDepartmentId());
        courseWareWatchDetail.setCreateTime(date);
        courseWareWatchDetail.setWatchInterval(courseWareService.getWatchInterval());
        courseWareService.courseWareWatchDetailInsert(courseWareWatchDetail);

        //培训课程更新
        TrainItemUser trainItemUser = trainService.getTrainItemUser(requestVM.getTrainItemUserId(), user.getId(), TrainStatusEnum.Going.getCode());
        if (null != trainItemUser) {
            Date trainCreateTime = trainItemUser.getCreateTime();
            Integer watchSum = courseWareService.watchUserSum(requestVM.getCourseWareId(), user.getId(), trainCreateTime);
            trainItemUser.setCurrentNumber(watchSum);
            if (watchSum >= trainItemUser.getPassNumber()) {
                trainItemUser.setCurrentNumber(trainItemUser.getPassNumber());
                trainItemUser.setStatus(TrainStatusEnum.Pass.getCode());
                trainItemUser.setCompleteTime(new Date());
                trainService.updateTrainItemUser(trainItemUser);
                trainService.trainComplete(trainItemUser);
                response = new RestResponse(SystemCode.OK.getCode(), "该课件观看时长已合格", trainItemUser.getTrainId());
            } else {
                trainService.updateTrainItemUser(trainItemUser);
            }
        }
        return response;
    }


}
