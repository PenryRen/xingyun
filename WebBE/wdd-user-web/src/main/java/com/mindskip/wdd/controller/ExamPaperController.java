package com.mindskip.wdd.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.PaperTypeEnum;
import com.mindskip.wdd.mapping.ExamPaperMapping;
import com.mindskip.wdd.repository.ExamPaperAnswerMapper;
import com.mindskip.wdd.service.ExamPaperService;
import com.mindskip.wdd.service.FileUploadService;
import com.mindskip.wdd.service.PaperSessionService;
import com.mindskip.wdd.service.SystemService;
import com.mindskip.wdd.service.enums.SessionEnum;
import com.mindskip.wdd.utility.JsonUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.utility.RestUtil;
import com.mindskip.wdd.viewmodel.exam.answer.SelectByUserVM;
import com.mindskip.wdd.viewmodel.exam.face.FaceCompareRequest;
import com.mindskip.wdd.viewmodel.exam.paper.*;
import com.mindskip.wdd.viewmodel.upload.CameraRequest;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.iai.v20200303.IaiClient;
import com.tencentcloudapi.iai.v20200303.models.CompareFaceRequest;
import com.tencentcloudapi.iai.v20200303.models.CompareFaceResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 试卷列表
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@RestController
@RequestMapping(value = "/api/exam/paper")
@RequiredArgsConstructor
public class ExamPaperController extends BaseApiController implements InitializingBean {

    @Value("${system.resource.qcloud.region}")
    private String region;
    @Value("${system.resource.qcloud.secret-id}")
    private String secretId;
    @Value("${system.resource.qcloud.secret-key}")
    private String secretKey;
    private static IaiClient client;
    private final ExamPaperService examPaperService;
    private final SystemService systemService;
    private final ExamPaperMapping examPaperMapping;
    private final PaperSessionService paperSessionService;
    private final FileUploadService fileUploadService;
    private static final Logger logger = LoggerFactory.getLogger(ExamPaperController.class);


    /**
     * 试卷分类
     *
     * @return {@link RestResponse}<{@link List}<{@link ExamPaperArchiveVM}>>
     */
    @PostMapping("/archive/list")
    public RestResponse<List<ExamPaperArchiveVM>> archiveList() {
        List<ExamPaperArchive> rootTree = examPaperService.selectRootTree();
        List<ExamPaperArchiveVM> rootVM = examPaperMapping.toExamPaperArchiveVMList(rootTree);
        return RestResponse.ok(rootVM);
    }


    /**
     * 试卷分页查询
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/page")
    public RestResponse<PageInfo<ExamPaperPageResponseVM>> pageList(@RequestBody ExamPaperPageRequestVM model) {
        if (model.getExamPaperArchiveId() != null) {
            ExamPaperArchive rootExamPaperArchive = examPaperService.getExamPaperArchiveById(model.getExamPaperArchiveId());
            List<ExamPaperArchive> examPaperArchiveList = examPaperService.getExamPaperArchiveByLevel(rootExamPaperArchive.getLevel());
            List<Integer> examPaperArchiveIdList = examPaperArchiveList.stream().map(item -> item.getId())
                    .collect(Collectors.toList());
            model.setExamPaperArchiveIdList(examPaperArchiveIdList);
        }

        User user = getCurrentUser();
        model.setUserId(user.getId());
        model.setDepartmentId(user.getDepartmentId());
        model.setNow(new Date());
        PaperTypeEnum paperTypeEnum = PaperTypeEnum.fromCode(model.getPaperType());
        PageInfo<ExamPaper> pageInfo = null;
        switch (paperTypeEnum) {
            case Official:
                pageInfo = examPaperService.page(model);
                break;
            case Resit:
                pageInfo = examPaperService.resitPage(model);
                break;
        }
        PageInfo<ExamPaperPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, a -> examPaperMapping.toExamPaperPageResponseVM(a));
        return RestResponse.ok(page);
    }

    private final ExamPaperAnswerMapper examPaperAnswerMapper;

    /**
     * 试卷详情
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/{type}/{id}")
    public RestResponse select(@PathVariable Integer type, @PathVariable Long id) {
        User user = getCurrentUser();
        PaperTypeEnum paperTypeEnum = PaperTypeEnum.fromCode(type);
        ExamPaperCache examPaperCache = examPaperService.getExamPaperCache(id, user.getId());
        RestResponse paperPermissionResult = examPaperService.paperPermissionCheck(paperTypeEnum, examPaperCache, getCurrentUser());
        if (paperPermissionResult.getCode() != SystemCode.OK.getCode()) {
            return paperPermissionResult;
        }

        if (examPaperCache.getFaceCheck()) { //人脸识别检查
            Boolean faceCheck = paperSessionService.getPaperFace(examPaperCache.getId(), user.getId());
            if (faceCheck == null || !faceCheck) {
                return RestResponse.fail(2, "请先进行人脸识别");
            }
        }
        SelectByUserVM selectByUserVM = new SelectByUserVM();
        selectByUserVM.setPaperId(id);
        selectByUserVM.setUserId(user.getId());
        ExamPaperAnswer examPaperAnswer = examPaperAnswerMapper.selectByPaperUserId(selectByUserVM);
        if (null != examPaperAnswer) {
            return RestResponse.fail(2, "该试卷只能做一次");
        }

        ExamPaperDoResponseVM examPaperDoResponseVM = examPaperService.toExamPaperDoResponseVM(examPaperCache, user);
        paperSessionService.initPaperSession(examPaperDoResponseVM, user, SessionEnum.Paper);
        examPaperDoResponseVM.setInitFirst(examPaperDoResponseVM.getRemainTime().equals(examPaperCache.getSuggestTime() * 60));
        return systemService.pairTwoObjectEncrypt(examPaperDoResponseVM);
    }


    /**
     * 防作弊+1
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/cheat/{id}")
    public RestResponse cheat(@PathVariable Long id) {
        paperSessionService.incrementCheat(id, getCurrentUser(), SessionEnum.Paper);
        return RestResponse.ok();
    }


    /**
     * 人脸识别
     *
     * @return {@link RestResponse}
     */
    @PostMapping("/face/check1")
    public RestResponse faceCheck(@RequestBody @Valid FaceCheckVM faceCheckVM) {
        try {
            User user = getCurrentUser();
            PaperTypeEnum paperTypeEnum = PaperTypeEnum.fromCode(faceCheckVM.getPaperType());
            ExamPaperCache examPaperCache = examPaperService.getExamPaperCache(faceCheckVM.getId(), user.getId());

            RestResponse paperPermissionResult = examPaperService.paperPermissionCheck(paperTypeEnum, examPaperCache, user);
            if (paperPermissionResult.getCode() != SystemCode.OK.getCode()) {
                return paperPermissionResult;
            }
            if (!examPaperCache.getFaceCheck()) {
                return RestResponse.fail(2, "该试卷无需人脸识别");
            }
            if (StringUtils.isBlank(user.getImagePath())) {
                return RestResponse.fail(2, "请先完善个人信息中的头像");
            }
            Boolean faceCheck = paperSessionService.getPaperFace(examPaperCache.getId(), user.getId());
            if (faceCheck != null && faceCheck) {
                return RestResponse.ok();
            }
            CompareFaceRequest compareFaceRequest = new CompareFaceRequest();
            compareFaceRequest.setImageA(faceCheckVM.getImageBase());
            compareFaceRequest.setUrlB(user.getImagePath());
            CompareFaceResponse compareFaceResponse = client.CompareFace(compareFaceRequest);
            if (compareFaceResponse.getScore() > 50.0) {
                paperSessionService.paperFace(examPaperCache.getId(), user.getId());
                return RestResponse.ok();
            } else {
                return RestResponse.fail(2, "人脸识别不正确");
            }
        } catch (TencentCloudSDKException e) {
            logger.error(e.getMessage(), e);
            return RestResponse.fail(2, "人脸识别错误");
        }
    }

    @Value("${system.resource.file.location}")
    private String fileLocation;
    @Value("${system.resource.file.url}")
    private String fileUrl;

    /**
     * openvc 人脸对比
     *
     * @return {@link RestResponse}
     */
    @PostMapping("/face/check")
    public RestResponse faceOpencv(@RequestBody @Valid FaceCheckVM faceCheckVM) {
        try {
            User user = getCurrentUser();
            PaperTypeEnum paperTypeEnum = PaperTypeEnum.fromCode(faceCheckVM.getPaperType());
            ExamPaperCache examPaperCache = examPaperService.getExamPaperCache(faceCheckVM.getId(), user.getId());

            RestResponse paperPermissionResult = examPaperService.paperPermissionCheck(paperTypeEnum, examPaperCache, user);
            if (paperPermissionResult.getCode() != SystemCode.OK.getCode()) {
                return paperPermissionResult;
            }
            if (!examPaperCache.getFaceCheck()) {
                return RestResponse.fail(2, "该试卷无需人脸识别");
            }
            if (StringUtils.isBlank(user.getImagePath())) {
                return RestResponse.fail(2, "请先完善个人信息中的头像");
            }
            Boolean faceCheck = paperSessionService.getPaperFace(examPaperCache.getId(), user.getId());
            if (faceCheck != null && faceCheck) {
                return RestResponse.ok();
            }

            byte[] imageData = Base64.decode(faceCheckVM.getImageBase());
            InputStream inputStream = new ByteArrayInputStream(Base64.decode(faceCheckVM.getImageBase()));
            String facePath = fileUploadService.fileUpload(inputStream, imageData.length, UUID.randomUUID().toString() + ".jpg", "face", true, false);
            facePath = fileLocation + facePath.replace(fileUrl, "");
            String profile = fileLocation + File.separator + "profile" + StrUtil.subAfter(user.getImagePath(), "profile", true);

            FaceCompareRequest faceCompareRequest = new FaceCompareRequest(facePath, profile);
            String parameterStr = JsonUtil.toJsonStr(faceCompareRequest);
            String responseStr = RestUtil.httpClientPost("http://127.0.0.1:6009/api/face/compare", parameterStr);
            RestResponse<Double> response = JsonUtil.toJsonObject(responseStr, RestResponse.class);
            if (response.getCode() == SystemCode.OK.getCode()) {
                if (response.getResponse() > 0.50) {
                    paperSessionService.paperFace(examPaperCache.getId(), user.getId());
                    return RestResponse.ok();
                } else {
                    return RestResponse.fail(2, "人脸识别不正确");
                }
            } else {
                return RestResponse.fail(2, "人脸系统错误");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RestResponse.fail(2, "人脸识别错误");
        }
    }


    /**
     * openvc 人脸对比测试
     *
     * @return {@link RestResponse}
     */
    @PostMapping("/face/opencvtest")
    public RestResponse faceOpencv() {
        FaceCompareRequest faceCompareRequest = new FaceCompareRequest("/usr/local/face/3.jpg", "/usr/local/face/7.jpg");
        String parameterStr = JsonUtil.toJsonStr(faceCompareRequest);
        String responseStr = RestUtil.httpClientPost("http://192.168.0.171:6009/api/face/compare", parameterStr);
        RestResponse<Double> response = JsonUtil.toJsonObject(responseStr, RestResponse.class);
        if (response.getCode() == SystemCode.OK.getCode()) {
            if (response.getResponse() > 0.85) {
                return RestResponse.ok();
            } else {
                return RestResponse.fail(2, "人脸识别不正确");
            }
        } else {
            return RestResponse.fail(2, "人脸系统错误");
        }
    }


    /**
     * 考试抓拍
     *
     * @param cameraRequest the camera request
     * @return the rest response
     */
    @RequestMapping("/camera")
    @ResponseBody
    public RestResponse camera(@RequestBody CameraRequest cameraRequest) {
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
     * 腾讯云人脸识别初始化
     */
    @Override
    public void afterPropertiesSet() {
        //人脸识别初始化
        Credential credential = new Credential(secretId, secretKey);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("iai.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        client = new IaiClient(credential, region, clientProfile);
    }

}
