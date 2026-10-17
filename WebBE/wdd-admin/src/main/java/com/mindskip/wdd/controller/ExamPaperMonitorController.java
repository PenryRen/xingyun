package com.mindskip.wdd.controller;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.ExamPaperBuildTypeEnum;
import com.mindskip.wdd.domain.enums.QuestionTypeEnum;
import com.mindskip.wdd.domain.frame.ExamPaperAnswerFrame;
import com.mindskip.wdd.mapping.ExamPaperAnswerMapping;
import com.mindskip.wdd.mapping.ExamPaperBuildMapping;
import com.mindskip.wdd.service.*;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.answer.CameraInfoVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerEditResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.monitor.PaperMonitorPageRequestVM;
import com.mindskip.wdd.viewmodel.monitor.PaperMonitorPageResponseVM;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version 6.5.0
 * @description: 答卷监考
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/25 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/exam/paper/monitor")
public class ExamPaperMonitorController extends BaseApiController {

    private final ExamPaperBuildService examPaperBuildService;
    private final ExamPaperAnswerService examPaperAnswerService;
    private final ExamPaperAnswerMapping examPaperAnswerMapping;
    private final DepartmentService departmentService;
    private final UserService userService;
    private final ExamPaperMonitorService examPaperMonitorService;
    private final ExamPaperArchiveService examPaperArchiveService;
    private final ExamPaperBuildMapping examPaperBuildMapping;
    private final ExamPaperService examPaperService;

    /**
     * 考试监考分页
     *
     * @param paperMonitorPageRequestVM the paper answer page request vm
     * @return the rest response
     */
    @PostMapping("/page")
    @PreAuthorize("exam:paper:monitor:page")
    public RestResponse monitorPage(@RequestBody PaperMonitorPageRequestVM paperMonitorPageRequestVM) {
        initPermission(paperMonitorPageRequestVM);
        PageInfo<ExamPaperAnswerMonitor> pageInfo = examPaperMonitorService.monitorPage(paperMonitorPageRequestVM);
        PageInfo<PaperMonitorPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, paperAnswerMonitor -> {
            PaperMonitorPageResponseVM paperMonitorPageResponseVM = examPaperAnswerMapping.toPaperMonitorPageResponseVM(paperAnswerMonitor);
            ExamPaperBuild examPaperBuild = examPaperBuildService.getById(paperAnswerMonitor.getExamPaperBuildId());
            if (null != examPaperBuild) {
                paperMonitorPageResponseVM.setPaperName(examPaperBuild.getName());
                if (null != examPaperBuild.getBuildConfig()) {
                    paperMonitorPageResponseVM.setCapture(examPaperBuild.getBuildConfig().getCapture());
                }
            }
            User user = userService.getById(paperAnswerMonitor.getCreateUser());
            if (null != user) {
                paperMonitorPageResponseVM.setUserName(user.getUserName());
                paperMonitorPageResponseVM.setRealName(user.getRealName());
                paperMonitorPageResponseVM.setIdCard(user.getIdCard());
                paperMonitorPageResponseVM.setImagePath(user.getImagePath());
            }
            if (null != paperAnswerMonitor.getCreateDepartmentId()) {
                Department department = departmentService.getById(paperAnswerMonitor.getCreateDepartmentId());
                if (null != department) {
                    paperMonitorPageResponseVM.setDepartmentLevel(department.getLevel());
                }
            }
            return paperMonitorPageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 监考抓拍
     *
     * @param id id
     * @return {@link RestResponse}<{@link CameraInfoVM}>
     */
    @PostMapping("/camera/{id}")
    @PreAuthorize("exam:paper:monitor:camera")
    public RestResponse<CameraInfoVM> camera(@PathVariable Long id) {
        ExamPaperAnswerMonitor examPaperAnswerMonitor = examPaperMonitorService.monitorSelect(id);
        ExamPaperBuild examPaperBuild = examPaperBuildService.getById(examPaperAnswerMonitor.getExamPaperBuildId());
        CameraInfoVM cameraInfoVM = examPaperBuildMapping.toCameraInfoVM(examPaperBuild);
        cameraInfoVM.setCheatCount(examPaperAnswerMonitor.getCheatCount());
        ExamPaperBuildTypeEnum examPaperBuildTypeEnum = ExamPaperBuildTypeEnum.fromCode(examPaperBuild.getBuildType());
        cameraInfoVM.setBuildTypeStr(examPaperBuildTypeEnum.getName());
        ExamPaperArchive examPaperArchive = examPaperArchiveService.getById(examPaperBuild.getExamPaperArchiveId());
        if (null != examPaperArchive) {
            cameraInfoVM.setExamPaperArchiveStr(examPaperArchive.getLevel());
        }

        User user = userService.getById(examPaperAnswerMonitor.getCreateUser());
        cameraInfoVM.setAnswerUserName(String.format("%s - %s", user.getRealName(), user.getUserName()));
        examPaperBuildMapping.mapCameraInfoVM(examPaperAnswerMonitor, cameraInfoVM);

        List<String> imageList = examPaperAnswerService.examPaperUserCamera(examPaperAnswerMonitor.getCreateUser(), examPaperAnswerMonitor.getExamPaperId());
        cameraInfoVM.setImageList(imageList);
        return RestResponse.ok(cameraInfoVM);
    }


    /**
     * 答卷查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/read/select/{id}")
    @PreAuthorize("exam:paper:monitor:read")
    public RestResponse read(@PathVariable Long id) {
        ExamPaperAnswerMonitor examPaperAnswerMonitor = examPaperMonitorService.monitorSelect(id);
        if (null == examPaperAnswerMonitor) {
            return RestResponse.fail(2, "未找到答卷");
        }
        ExamPaperAnswerInfoResponseVM examPaperAnswerInfoResponseVM = examPaperAnswerMapping.toExamPaperAnswerInfoResponseVM(examPaperAnswerMonitor);
        ExamPaperCache examPaperCache = examPaperService.getExamPaperCache(examPaperAnswerMonitor.getExamPaperId(), examPaperAnswerMonitor.getExamPaperChildId());
        ExamPaperAnswerEditResponseVM examPaperAnswerEditResponseVM = examPaperAnswerService.toExamPaperAnswerEditResponseVM(examPaperAnswerInfoResponseVM, examPaperCache, examPaperAnswerMonitor.getAnswerFrameContent());
        User user = userService.getById(examPaperAnswerMonitor.getCreateUser());
        examPaperAnswerEditResponseVM.setUserName(user.getUserName());
        examPaperAnswerEditResponseVM.setRealName(user.getRealName());
        examPaperAnswerEditResponseVM.setWorkNo(user.getWorkNo());
        examPaperAnswerEditResponseVM.setJobTitle(user.getJobTitle());
        examPaperAnswerEditResponseVM.setIdCard(user.getIdCard());
        examPaperAnswerEditResponseVM.setQuestionCorrect(examPaperAnswerMonitor.getQuestionCorrect());
        examPaperAnswerEditResponseVM.setQuestionCount(examPaperAnswerMonitor.getQuestionCount());
        if (null != user.getDepartmentId()) {
            Department department = departmentService.getById(user.getDepartmentId());
            examPaperAnswerEditResponseVM.setDepartmentLevel(department.getLevel());
        }
        ExamPaperAnswer examPaperAnswer = examPaperAnswerService.getUserAnswer(examPaperAnswerMonitor.getCreateUser(), examPaperAnswerMonitor.getExamPaperId());
        examPaperAnswerEditResponseVM.setCanSubmit(null == examPaperAnswer);
        //清理未完成题目状态
        examPaperAnswerEditResponseVM.getAnswer().getQuestionAnswerFrameList().forEach(item -> {
            QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(item.getQuestionType());
            switch (questionTypeEnum) {
                case SingleChoice:
                case TrueFalse:
                    if (null == item.getContentKey()) {
                        item.setDoRight(null);
                    }
                    break;
                case MultipleChoice:
                case UncertainMultipleChoice:
                    if (null == item.getContentArrayKey() || item.getContentArrayKey().size() == 0) {
                        item.setDoRight(null);
                    }
                    break;
                case GapFilling:
                    if (null == item.getContentArray() || item.getContentArray().size() == 0) {
                        item.setDoRight(null);
                    }
                    break;
                case ShortAnswer:
                    if (null == item.getContent()) {
                        item.setDoRight(null);
                    }
                    break;
            }
        });
        return RestResponse.ok(examPaperAnswerEditResponseVM);
    }


    /**
     * 监考试卷提交
     *
     * @param id id
     * @return {@link RestResponse}
     */
    @PostMapping("/submit/{id}")
    @PreAuthorize("exam:paper:monitor:submit")
    public RestResponse submit(@PathVariable Long id) {
        ExamPaperAnswerMonitor examPaperAnswerMonitor = examPaperMonitorService.monitorSelect(id);
        ExamPaperAnswer examPaperAnswer = examPaperAnswerService.getUserAnswer(examPaperAnswerMonitor.getCreateUser(), examPaperAnswerMonitor.getExamPaperId());
        if (null != examPaperAnswer) {
            return RestResponse.fail(2, "试卷已提交");
        }
        ExamPaperAnswerFrame examPaperAnswerFrame = examPaperAnswerMonitor.getAnswerFrameContent();
        User user = userService.getById(examPaperAnswerMonitor.getCreateUser());
        ExamPaperCache examPaperCache = examPaperService.getExamPaperCache(examPaperAnswerMonitor.getExamPaperId(), examPaperAnswerMonitor.getExamPaperChildId());
        examPaperAnswerService.submit(examPaperCache, examPaperAnswerFrame, user);
        return RestResponse.ok();
    }


}
