package com.mindskip.wdd.controller;

import cn.hutool.core.io.file.FileWriter;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.wdd.domain.enums.ExamPaperBuildTypeEnum;
import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import com.mindskip.wdd.mapping.ExamPaperBuildMapping;
import com.mindskip.wdd.service.*;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.answer.*;
import com.mindskip.wdd.viewmodel.common.SeriesItem;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerEditResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerInfoResponseVM;
import com.mindskip.wdd.viewmodel.exam.answer.ExamPaperAnswerRequest;
import com.mindskip.wdd.viewmodel.exam.build.ExamPaperBuildPageRequestVM;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.OfficeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @version 1.7.0
 * @description: 答卷接口
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@RestController
@RequestMapping(value = "/api/exam/paper/answer")
@RequiredArgsConstructor
public class ExamPaperAnswerController extends BaseApiController {

    @Autowired(required = false)
    private DocumentConverter converter;
    private final ExamPaperBuildService examPaperBuildService;
    private final ExamPaperArchiveService examPaperArchiveService;
    private final ExamPaperBuildMapping examPaperBuildMapping;
    private final ExamPaperAnswerService examPaperAnswerService;
    private final DepartmentService departmentService;
    private final ExamPaperService examPaperService;
    private final UserService userService;
    private final FileUploadService fileUploadService;
    private final CredentialService credentialService;
    private final ExamPaperExportService examPaperExportService;
    private final static Integer MAXSIZE = 3000;
    private static final Logger logger = LoggerFactory.getLogger(ExamPaperAnswerController.class);


    /**
     * 答卷分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/page")
    @PreAuthorize("exam:paper:answer:page")
    public RestResponse<PageInfo<ExamPaperPageResponseVM>> page(@RequestBody ExamPaperBuildPageRequestVM model) {
        initPermission(model);
        PageInfo<ExamPaperBuild> pageInfo = examPaperBuildService.page(model);
        PageInfo<ExamPaperPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, examPaperBuild -> {
            ExamPaperPageResponseVM examPaperPageResponseVM = examPaperBuildMapping.toExamPaperPageResponseVM(examPaperBuild);
            ExamPaperBuildTypeEnum examPaperBuildTypeEnum = ExamPaperBuildTypeEnum.fromCode(examPaperBuild.getBuildType());
            examPaperPageResponseVM.setBuildTypeStr(examPaperBuildTypeEnum.getName());
            ExamPaperArchive examPaperArchive = examPaperArchiveService.getById(examPaperBuild.getExamPaperArchiveId());
            if (null != examPaperArchive) {
                examPaperPageResponseVM.setExamPaperArchive(examPaperArchive.getLevel());
            }
            Integer allUserCount = examPaperService.paperAllUserCount(examPaperBuild.getExamPaperId());
            Integer completeCount = examPaperAnswerService.getAnswerCount(new ExamPaperAnswerRequest(examPaperBuild.getId()));
            Integer judgeCount = examPaperAnswerService.getAnswerCount(new ExamPaperAnswerRequest(examPaperBuild.getId(), ExamPaperAnswerStatusEnum.WaitJudge.getCode()));
            examPaperPageResponseVM.setAllCount(allUserCount);
            examPaperPageResponseVM.setJudgeCount(judgeCount);
            examPaperPageResponseVM.setCompleteCount(completeCount);
            return examPaperPageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 答卷详情
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/info/{id}")
    @PreAuthorize("exam:paper:answer:info")
    public RestResponse info(@PathVariable Long id) {
        ExamPaperBuild examPaperBuild = examPaperBuildService.getById(id);
        if (null == examPaperBuild) {
            return RestResponse.fail(2, "试卷未找到");
        }
        PaperInfoVM paperInfoVM = examPaperBuildMapping.toPaperInfoVM(examPaperBuild);
        paperInfoVM.setCapture(examPaperBuild.getBuildConfig().getCapture());
        ExamPaperBuildTypeEnum examPaperBuildTypeEnum = ExamPaperBuildTypeEnum.fromCode(examPaperBuild.getBuildType());
        paperInfoVM.setBuildTypeStr(examPaperBuildTypeEnum.getName());
        ExamPaperArchive examPaperArchive = examPaperArchiveService.getById(examPaperBuild.getExamPaperArchiveId());
        if (null != examPaperArchive) {
            paperInfoVM.setExamPaperArchiveStr(examPaperArchive.getLevel());
        }
        User createUser = userService.getById(examPaperBuild.getCreateUser());
        paperInfoVM.setCreateUserStr(String.format("%s - %s", createUser.getRealName(), createUser.getUserName()));
        Integer allUserCount = examPaperService.paperAllUserCount(examPaperBuild.getExamPaperId());
        Integer completeCount = examPaperAnswerService.getAnswerCount(new ExamPaperAnswerRequest(examPaperBuild.getId()));
        Integer judgeCount = examPaperAnswerService.getAnswerCount(new ExamPaperAnswerRequest(examPaperBuild.getId(), ExamPaperAnswerStatusEnum.WaitJudge.getCode()));
        Integer passCount = examPaperAnswerService.getAnswerCount(new ExamPaperAnswerRequest(examPaperBuild.getId(), ExamPaperAnswerStatusEnum.Complete.getCode(), true));
        Integer passScoreCount = examPaperAnswerService.getAnswerCount(new ExamPaperAnswerRequest("sum(user_score)", examPaperBuild.getId(), ExamPaperAnswerStatusEnum.Complete.getCode()));
        Integer questionCorrectCount = examPaperAnswerService.getAnswerCount(new ExamPaperAnswerRequest("sum(question_correct)", examPaperBuild.getId(), ExamPaperAnswerStatusEnum.Complete.getCode()));
        paperInfoVM.setPassCount(passCount);
        if (null != completeCount && !completeCount.equals(0)) {
            if (null != passCount) {
                paperInfoVM.setPassPercent(ExamUtil.percentFormat(passCount / (completeCount * 1.0)));
            }
            if (null != questionCorrectCount) {
                paperInfoVM.setCorrectPercent(ExamUtil.percentFormat(questionCorrectCount / (examPaperBuild.getQuestionCount() * completeCount * 1.0)));
            }
            if (null != passScoreCount) {
                paperInfoVM.setScorePercent(ExamUtil.percentFormat(passScoreCount / (examPaperBuild.getScore() * completeCount * 1.0)));
            }
        }
        paperInfoVM.setAllCount(allUserCount);
        paperInfoVM.setJudgeCount(judgeCount);
        paperInfoVM.setCompleteCount(completeCount);

        List<Integer> passScoreList = examPaperAnswerService.getAnswerScore(new ExamPaperAnswerRequest(examPaperBuild.getId(), ExamPaperAnswerStatusEnum.Complete.getCode()));
        Integer paperScore = ExamUtil.scoreFromVM(paperInfoVM.getScore());
        Integer passScore = examPaperBuild.getPassScore();
        for (int i = 1; i <= 11; i++) {
            String pieX = null;
            String x = null;
            Object y = null;
            int count = 0;
            if (i < 11) {
                int min = (int) ((i - 1) * 0.1 * paperScore);
                int max = (int) (i * 0.1 * paperScore);
                count = (int) passScoreList.stream().filter(item -> min <= item && item < max).count();
                pieX = String.format("%s - %s", ExamUtil.scoreToVM(min), ExamUtil.scoreToVM(max));
                x = String.format("%s\nㅣ\n%s", ExamUtil.scoreToVM(min), ExamUtil.scoreToVM(max));
                y = min <= passScore && passScore < max ? new SeriesItem(count, "#13ce66") : count;
            } else if (i == 11) {
                count = (int) passScoreList.stream().filter(item -> item.equals(paperScore)).count();
                pieX = paperInfoVM.getScore();
                x = paperInfoVM.getScore();
                y = passScore.equals(paperScore) ? new SeriesItem(count, "#13ce66") : count;
            }
            paperInfoVM.getPieList().add(new PieKeyValue(pieX, count));
            paperInfoVM.getX().add(x);
            paperInfoVM.getY().add(y);
        }
        return RestResponse.ok(paperInfoVM);
    }


    /**
     * 答卷用户成绩
     *
     * @param paperAnswerPageRequestVM the paper answer page request vm
     * @return the rest response
     */
    @PostMapping("/user/page")
    @PreAuthorize("exam:paper:answer:user")
    public RestResponse userAnswerPage(@RequestBody PaperAnswerPageRequestVM paperAnswerPageRequestVM) {
        if (StringUtils.isNotBlank(paperAnswerPageRequestVM.getMinScoreStr())) {
            paperAnswerPageRequestVM.setMinScore(ExamUtil.scoreFromVM(paperAnswerPageRequestVM.getMinScoreStr()));
        }
        if (StringUtils.isNotBlank(paperAnswerPageRequestVM.getMaxScoreStr())) {
            paperAnswerPageRequestVM.setMaxScore(ExamUtil.scoreFromVM(paperAnswerPageRequestVM.getMaxScoreStr()));
        }
        ExamPaperBuild examPaperBuild = examPaperBuildService.getById(paperAnswerPageRequestVM.getId());
        if (null == examPaperBuild) {
            return RestResponse.fail(2, "试卷未找到");
        }
        initPermission(paperAnswerPageRequestVM);
        paperAnswerPageRequestVM.setExamPaperId(examPaperBuild.getExamPaperId());
        PageInfo<PaperAnswerUserPageResponseVM> pageInfo = examPaperAnswerService.userAnswerPage(paperAnswerPageRequestVM);
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
     * 用户成绩导出
     *
     * @param paperAnswerPageRequestVM the paper answer page request vm
     * @return the rest response
     * @throws IOException the io exception
     */
    @PostMapping("/export")
    @PreAuthorize("exam:paper:answer:result:export")
    public RestResponse export(@RequestBody PaperAnswerPageRequestVM paperAnswerPageRequestVM) throws IOException {
        paperAnswerPageRequestVM.setPageSize(MAXSIZE);
        RestResponse restResponse = userAnswerPage(paperAnswerPageRequestVM);
        if (restResponse.getCode() == SystemCode.OK.getCode()) {
            @SuppressWarnings("unchecked") List<PaperAnswerUserPageResponseVM> exportList = ((RestResponse<PageInfo<PaperAnswerUserPageResponseVM>>) restResponse).getResponse().getList();
            ExamPaperBuild examPaperBuild = examPaperBuildService.getById(paperAnswerPageRequestVM.getId());
            //结果回写
            File excelTemp = File.createTempFile(UUID.randomUUID().toString(), ".xlsx");
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
            EasyExcel.write(excelTemp, PaperAnswerUserPageResponseVM.class).sheet(examPaperBuild.getName()).registerWriteHandler(horizontalCellStyleStrategy).doWrite(exportList);
            String filePath = fileUploadService.fileUpload(excelTemp, String.format("%s - 考试结果 - %s.xlsx", examPaperBuild.getName(), DateTimeUtil.dateTimeFullNumberFormat(new Date())), "export/excel", true, true);
            return RestResponse.ok(filePath);
        } else {
            return restResponse;
        }
    }


    /**
     * 答卷查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/read/select/{id}")
    @PreAuthorize({"exam:paper:answer:read:select", "exam:paper:answer:read:edit"})
    public RestResponse read(@PathVariable Long id) {
        ExamPaperAnswer examPaperAnswer = examPaperAnswerService.getById(id);
        ExamPaperCache examPaperCache = examPaperService.getExamPaperCache(examPaperAnswer.getExamPaperId(), examPaperAnswer.getExamPaperChildId());
        ExamPaperAnswerEditResponseVM examPaperAnswerEditResponseVM = examPaperAnswerService.toExamPaperAnswerEditResponseVM(examPaperCache, examPaperAnswer);
        User user = userService.getById(examPaperAnswer.getCreateUser());
        examPaperAnswerEditResponseVM.setUserName(user.getUserName());
        examPaperAnswerEditResponseVM.setRealName(user.getRealName());
        examPaperAnswerEditResponseVM.setWorkNo(user.getWorkNo());
        examPaperAnswerEditResponseVM.setJobTitle(user.getJobTitle());
        examPaperAnswerEditResponseVM.setIdCard(user.getIdCard());
        if (null != user.getDepartmentId()) {
            Department department = departmentService.getById(user.getDepartmentId());
            examPaperAnswerEditResponseVM.setDepartmentLevel(department.getLevel());
        }
        if (null != examPaperAnswer.getJudgeUser()) {
            User judgeUser = userService.getById(examPaperAnswer.getJudgeUser());
            examPaperAnswerEditResponseVM.setJudgeUser(String.format("%s（%s）", judgeUser.getRealName(), judgeUser.getUserName()));
        }
        return RestResponse.ok(examPaperAnswerEditResponseVM);
    }


    /**
     * 答卷批改
     *
     * @param examPaperAnswerRequestVM the exam paper answer request vm
     * @return the rest response
     */
    @PostMapping("/edit")
    @PreAuthorize("exam:paper:answer:read:edit")
    public RestResponse edit(@RequestBody ExamPaperAnswerInfoResponseVM examPaperAnswerRequestVM) {
        for (QuestionAnswerFrame questionAnswerFrame : examPaperAnswerRequestVM.getQuestionAnswerFrameList()) {
            if (questionAnswerFrame.getDoRight() == null && StringUtils.isEmpty(questionAnswerFrame.getJudgeScoreVM())) {
                return RestResponse.fail(2, "有未批改题目！");
            }
        }
        ExamPaperAnswerStatusEnum examPaperAnswerStatusEnum = examPaperAnswerService.getStatus(examPaperAnswerRequestVM.getId());
        if (ExamPaperAnswerStatusEnum.WaitJudge != examPaperAnswerStatusEnum) {
            return RestResponse.fail(3, "该试卷状态不能批改");
        }
        User user = getCurrentUser();
        examPaperAnswerRequestVM.setJudgeUser(user.getId());
        examPaperAnswerRequestVM.setJudgeUserName(user.getUserName());
        Integer resultScore = examPaperAnswerService.judge(examPaperAnswerRequestVM);
        return RestResponse.ok(ExamUtil.scoreToVM(resultScore));
    }


    /**
     * 获取下一张批改试卷id
     *
     * @param id id
     * @return {@link RestResponse}
     */
    @PostMapping("/next/judge/{id}")
    public RestResponse nextPaperAnswerId(@PathVariable Long id) {
        ExamPaperAnswer examPaperAnswer = examPaperAnswerService.getById(id);
        PaperAnswerPageRequestVM paperAnswerPageRequestVM = new PaperAnswerPageRequestVM();
        paperAnswerPageRequestVM.setId(id);
        paperAnswerPageRequestVM.setExamPaperBuildId(examPaperAnswer.getExamPaperBuildId());
        initPermission(paperAnswerPageRequestVM);
        Long nextId = examPaperAnswerService.getNextJudgeId(paperAnswerPageRequestVM);
        if (null != nextId) {
            return RestResponse.ok(nextId);
        } else {
            return RestResponse.fail(2, "没有可批改的试卷了");
        }
    }


    /**
     * 答卷抓拍记录
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/camera/{id}")
    @PreAuthorize("exam:paper:answer:camera")
    public RestResponse<CameraInfoVM> camera(@PathVariable Long id) {
        ExamPaperAnswer examPaperAnswer = examPaperAnswerService.getById(id);
        ExamPaperBuild examPaperBuild = examPaperBuildService.getById(examPaperAnswer.getExamPaperBuildId());
        CameraInfoVM cameraInfoVM = examPaperBuildMapping.toCameraInfoVM(examPaperBuild);
        ExamPaperBuildTypeEnum examPaperBuildTypeEnum = ExamPaperBuildTypeEnum.fromCode(examPaperBuild.getBuildType());
        cameraInfoVM.setBuildTypeStr(examPaperBuildTypeEnum.getName());
        ExamPaperArchive examPaperArchive = examPaperArchiveService.getById(examPaperBuild.getExamPaperArchiveId());
        if (null != examPaperArchive) {
            cameraInfoVM.setExamPaperArchiveStr(examPaperArchive.getLevel());
        }

        User user = userService.getById(examPaperAnswer.getCreateUser());
        cameraInfoVM.setAnswerUserName(String.format("%s - %s", user.getRealName(), user.getUserName()));
        examPaperBuildMapping.mapCameraInfoVM(examPaperAnswer, cameraInfoVM);
        cameraInfoVM.setStatusStr(ExamPaperAnswerStatusEnum.fromCode(examPaperAnswer.getStatus()).getName());

        List<String> imageList = examPaperAnswerService.examPaperUserCamera(examPaperAnswer.getCreateUser(), examPaperAnswer.getExamPaperId());
        cameraInfoVM.setImageList(imageList);
        return RestResponse.ok(cameraInfoVM);
    }


    /**
     * 用户答卷导出
     *
     * @param answerId 答卷id
     * @return {@link RestResponse}<{@link String}>
     * @throws IOException ioexception
     */
    @PostMapping("/user/export/{answerId}")
    @PreAuthorize("exam:paper:answer:result:export")
    public RestResponse<String> userExport(@PathVariable Long answerId) throws IOException {
        ExamPaperAnswer examPaperAnswer = examPaperAnswerService.getById(answerId);
        ExamPaperCache examPaperCache = examPaperService.getExamPaperCache(examPaperAnswer.getExamPaperId(), examPaperAnswer.getExamPaperChildId());
        ExamPaperAnswerEditResponseVM examPaperAnswerEditResponseVM = examPaperAnswerService.toExamPaperAnswerEditResponseVM(examPaperCache, examPaperAnswer);
        User user = userService.getById(examPaperAnswerEditResponseVM.getAnswer().getCreateUser());
        String content = examPaperExportService.answerExport(examPaperAnswerEditResponseVM);
        File htmlFile = File.createTempFile(UUID.randomUUID().toString(), ".html");
        FileWriter writer = new FileWriter(htmlFile);
        writer.write(content);
        try {
            File pdfFile = File.createTempFile(UUID.randomUUID().toString(), ".pdf");
            converter.convert(htmlFile).to(pdfFile).execute();
            String fileName = user.getRealName() == null ? user.getUserName() : user.getRealName();
            String previewFilePath = fileUploadService.fileUpload(pdfFile, String.format("%s.pdf", fileName), "export/pdf", true, true);
            examPaperAnswer.setPreviewFilePath(previewFilePath);
            examPaperAnswerService.updateExamPaperAnswer(examPaperAnswer);
            return RestResponse.ok(previewFilePath);
        } catch (OfficeException e) {
            logger.error(e.getMessage(), e);
            return RestResponse.fail(2, "文档转换失败");
        }
    }


    /**
     * 答卷证书统计
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/credential/info/{id}")
    @PreAuthorize("exam:paper:answer:credential")
    public RestResponse answerCredentialInfo(@PathVariable Long id) {
        ExamPaperBuild examPaperBuild = examPaperBuildService.getById(id);
        if (null == examPaperBuild) {
            return RestResponse.fail(2, "试卷未找到");
        }
        PaperCredentialInfoVM paperCredentialInfoVM = new PaperCredentialInfoVM();
        Integer allCredential = credentialService.userCredentialCount(id, null);
        Integer buildCredential = credentialService.userCredentialCount(id, true);
        Integer waitCredential = allCredential - buildCredential;
        paperCredentialInfoVM.setAllCredential(allCredential);
        paperCredentialInfoVM.setBuildCredential(buildCredential);
        paperCredentialInfoVM.setWaitCredential(waitCredential);
        return RestResponse.ok(paperCredentialInfoVM);
    }

    /**
     * 答卷证书展示
     *
     * @param paperCredentialPageRequestVM the paper credential page request vm
     * @return the rest response
     */
    @PostMapping("/credential/page")
    @PreAuthorize("exam:paper:answer:credential")
    public RestResponse answerCredentialPage(@RequestBody PaperCredentialPageRequestVM paperCredentialPageRequestVM) {
        ExamPaperBuild examPaperBuild = examPaperBuildService.getById(paperCredentialPageRequestVM.getId());
        if (null == examPaperBuild) {
            return RestResponse.fail(2, "试卷未找到");
        }
        initPermission(paperCredentialPageRequestVM);
        PageInfo<UserCredential> pageInfo = credentialService.page(paperCredentialPageRequestVM);
        PageInfo<String> page = PageInfoHelper.copyMap(pageInfo, a -> {
            if (null == a.getCredentialImagePath()) {
                String credentialPath = null;
                try {
                    credentialPath = credentialService.buildCredential(a);
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
                return credentialPath;
            } else {
                return a.getCredentialImagePath();
            }
        });
        return RestResponse.ok(page);
    }


    /**
     * 补考
     *
     * @param model 模型
     * @return {@link RestResponse}
     */
    @PostMapping("/resit")
    @PreAuthorize("exam:paper:answer:resit")
    public RestResponse resit(@RequestBody @Valid ExamResitVM model) {
        if (null == model.getLimitDateTime() || 2 != model.getLimitDateTime().size()) {
            return RestResponse.fail(2, "补考时间不能为空");
        }
        Date start = DateTimeUtil.parse(model.getLimitDateTime().get(0));
        Date end = DateTimeUtil.parse(model.getLimitDateTime().get(1));
        if (new Date().after(end)) {
            return RestResponse.fail(2, "补考结束时间不能小于当前时间");
        }

        ExamPaperAnswer examPaperAnswer = examPaperAnswerService.getById(model.getPaperAnswerId());
        User user = getCurrentUser();
        ExamPaperUser examPaperUser = new ExamPaperUser();
        examPaperUser.setExamPaperId(examPaperAnswer.getExamPaperId());
        examPaperUser.setUserId(examPaperAnswer.getCreateUser());
        examPaperUser.setDeleted(false);
        examPaperUser.setCreateUserId(user.getId());
        examPaperUser.setCreateDepartmentId(user.getDepartmentId());
        examPaperUser.setExamPaperAnswerId(examPaperAnswer.getId());
        examPaperUser.setLimitStartTime(start);
        examPaperUser.setLimitEndTime(end);
        examPaperService.clearPaperResit(examPaperAnswer.getExamPaperId(), examPaperAnswer.getCreateUser());
        examPaperService.insertExamPaperUser(examPaperUser);

        examPaperAnswer.setDeleted(true);
        examPaperAnswerService.updateExamPaperAnswer(examPaperAnswer);
        return RestResponse.ok();
    }


}
