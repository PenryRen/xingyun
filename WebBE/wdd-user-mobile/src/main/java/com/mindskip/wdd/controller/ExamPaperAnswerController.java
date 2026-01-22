package com.mindskip.wdd.controller;

import cn.hutool.core.codec.Base64;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.wdd.domain.frame.ExamPaperAnswerFrame;
import com.mindskip.wdd.mapping.ExamPaperAnswerMapping;
import com.mindskip.wdd.service.*;
import com.mindskip.wdd.service.enums.ResultEnum;
import com.mindskip.wdd.service.enums.SessionEnum;
import com.mindskip.wdd.utility.JsonUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.common.EncryptKV;
import com.mindskip.wdd.viewmodel.exam.answer.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 考试答卷
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/exam/paper/answer")
public class ExamPaperAnswerController extends BaseApiController {


    private final ExamPaperAnswerService examPaperAnswerService;
    private final ExamPaperService examPaperService;
    private final MessageQueueService messageQueueService;
    private final ExamPaperAnswerMapping examPaperAnswerMapping;
    private final SystemService systemService;
    private final PaperSessionService paperSessionService;
    private final FileUploadService fileUploadService;
    private static final Logger logger = LoggerFactory.getLogger(ExamPaperAnswerController.class);

    /**
     * 答卷分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/page")
    public RestResponse<PageInfo<ExamPaperAnswerPageResponseVM>> pageList(@RequestBody @Valid ExamPaperAnswerPageRequestVM model) {
        if (model.getExamPaperArchiveId() != null) {
            ExamPaperArchive rootExamPaperArchive = examPaperService.getExamPaperArchiveById(model.getExamPaperArchiveId());
            List<ExamPaperArchive> examPaperArchiveList = examPaperService.getExamPaperArchiveByLevel(rootExamPaperArchive.getLevel());
            List<Integer> examPaperArchiveIdList = examPaperArchiveList.stream().map(item -> item.getId())
                    .collect(Collectors.toList());
            model.setExamPaperArchiveIdList(examPaperArchiveIdList);
        }

        model.setCreateUser(getCurrentUser().getId());
        PageInfo<ExamPaperAnswer> pageInfo = examPaperAnswerService.page(model);
        PageInfo<ExamPaperAnswerPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, a -> {
            ExamPaperAnswerPageResponseVM examPaperAnswerPageResponseVM = examPaperAnswerMapping.toExamPaperAnswerPageResponseVM(a);
            examPaperAnswerPageResponseVM.setStatusStr(ExamPaperAnswerStatusEnum.fromCode(a.getStatus()).getName());
            return examPaperAnswerPageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 试卷提交
     *
     * @param encryptV
     * @return the rest response
     */
    @PostMapping("/submit")
    public RestResponse answerSubmit(@RequestBody String encryptV) {
        ExamPaperAnswerFrame examPaperAnswerFrame = systemService.aesDecrypt(encryptV, ExamPaperAnswerFrame.class);
        User user = getCurrentUser();
        Long paperId = examPaperAnswerFrame.getPaperId();
        ExamPaperCache examPaperCache = examPaperService.getExamPaperCache(examPaperAnswerFrame.getPaperId(), user.getId());
        paperSessionService.clearPaperSession(paperId, user, SessionEnum.Paper);
        ExamPaperAnswerResult examPaperAnswerResult = examPaperAnswerService.submit(examPaperCache, examPaperAnswerFrame, user);
        if (ResultEnum.FAIL == examPaperAnswerResult.getResultEnum()) {
            return RestResponse.fail(2, examPaperAnswerResult.getMessage());
        }
        messageQueueService.answerSend(new AnswerMQ(user.getId(), user.getUserName(), user.getDepartmentId(), examPaperAnswerFrame, new Date()));
        return RestResponse.ok(examPaperAnswerResult.getMessage());
    }


    /**
     * 试卷监考
     *
     * @param encryptKV
     * @return the rest response
     */
    @PostMapping("/monitor")
    public RestResponse answerMonitor(@RequestBody EncryptKV encryptKV) {
        ExamPaperAnswerFrame examPaperAnswerFrame = JsonUtil.toJsonObject(Base64.decodeStr(encryptKV.getValue()), ExamPaperAnswerFrame.class);
        User user = getCurrentUser();
        ExamPaperCache examPaperCache = examPaperService.getExamPaperCache(examPaperAnswerFrame.getPaperId(), user.getId());
        messageQueueService.monitor(examPaperCache, examPaperAnswerFrame, user);
        return RestResponse.ok();
    }


    @RequestMapping("/camera/{paperId}")
    @ResponseBody
    public RestResponse camera(@PathVariable Long paperId, HttpServletRequest request) {
        User user = getCurrentUser();
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        long fileSize = multipartFile.getSize();
        try (InputStream inputStream = multipartFile.getInputStream()) {
            String uuidName = UUID.randomUUID().toString();
            String fileName = uuidName + ".png";
            String folder = String.format("camera/%s/%s", paperId, user.getId());
            String filePath = fileUploadService.fileUpload(inputStream, fileSize, fileName, folder, false, false);
            ExamPaperUserCamera examPaperUserCamera = new ExamPaperUserCamera();
            examPaperUserCamera.setUserId(user.getId());
            examPaperUserCamera.setCreateDepartmentId(user.getDepartmentId());
            examPaperUserCamera.setExamPaperId(paperId);
            examPaperUserCamera.setImagePath(filePath);
            examPaperUserCamera.setCreateTime(new Date());
            examPaperService.insertExamPaperUserCamera(examPaperUserCamera);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return RestResponse.fail(2, "文件上传失败");
        }
        return RestResponse.ok();
    }


    @RequestMapping("/camera/h5")
    @ResponseBody
    public RestResponse cameraH5(@RequestBody CameraRequest cameraRequest) {
        User user = getCurrentUser();
        String uuidName = UUID.randomUUID().toString();
        try {
            File file = File.createTempFile(uuidName, ".png");
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                byte[] buff = Base64.decode(cameraRequest.getImageBase64());
                fileOutputStream.write(buff);
                String folder = String.format("camera/%s/%s", cameraRequest.getPaperId(), user.getId());
                String filePath = fileUploadService.fileUpload(file, uuidName + ".png", folder, false, false);
                ExamPaperUserCamera examPaperUserCamera = new ExamPaperUserCamera();
                examPaperUserCamera.setUserId(user.getId());
                examPaperUserCamera.setCreateDepartmentId(user.getDepartmentId());
                examPaperUserCamera.setExamPaperId(cameraRequest.getPaperId());
                examPaperUserCamera.setImagePath(filePath);
                examPaperUserCamera.setCreateTime(new Date());
                examPaperService.insertExamPaperUserCamera(examPaperUserCamera);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return RestResponse.ok();
    }


    /**
     * 试卷查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/read/select/{id}")
    public RestResponse read(@PathVariable Long id) {
        ExamPaperAnswer examPaperAnswer = examPaperAnswerService.getById(id);
        if (null == examPaperAnswer) {
            return RestResponse.fail(2, "试卷未找到！");
        }
        if (!examPaperAnswer.getCreateUser().equals(getCurrentUser().getId())) {
            return RestResponse.fail(3, "没权限访问试卷！");
        }
        ExamPaperCache examPaperCache = examPaperService.getExamPaperCache(examPaperAnswer.getExamPaperId(), examPaperAnswer.getCreateUser());
        if (null == examPaperCache) {
            return RestResponse.fail(2, "试卷未找到！");
        }
        if (!examPaperCache.getWatch()) {
            return RestResponse.fail(2, "试卷不允许查看！");
        }

        ExamPaperAnswerEditResponseVM examPaperAnswerEditResponseVM = examPaperAnswerService.toExamPaperAnswerEditResponseVM(examPaperCache, examPaperAnswer);
        return RestResponse.ok(systemService.aesEncrypt(examPaperAnswerEditResponseVM));
    }


}
