package com.mindskip.wdd.controller;

import cn.hutool.core.io.IoUtil;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.CourseWare;
import com.mindskip.wdd.domain.CourseWareArchive;
import com.mindskip.wdd.domain.CourseWareQuestion;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.enums.CourseWareFileTypeEnum;
import com.mindskip.wdd.mapping.CourseWareMapping;
import com.mindskip.wdd.service.CourseWareArchiveService;
import com.mindskip.wdd.service.CourseWareService;
import com.mindskip.wdd.service.FileUploadService;
import com.mindskip.wdd.service.QuestionService;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.course.ware.*;
import com.mindskip.wdd.viewmodel.question.QuestionEditRequestVM;
import lombok.RequiredArgsConstructor;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.office.OfficeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.info.MultimediaInfo;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 课件接口
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@RestController
@RequestMapping(value = "/api/course/ware")
@RequiredArgsConstructor
public class CourseWareController extends BaseApiController {

    @Autowired(required = false)
    private DocumentConverter converter;
    private final CourseWareService courseWareService;
    private final CourseWareArchiveService courseWareArchiveService;
    private final CourseWareMapping courseWareMapping;
    private final FileUploadService fileUploadService;
    private final QuestionService questionService;
    private static final Logger logger = LoggerFactory.getLogger(CourseWareController.class);


    /**
     * 课件分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/page")
    @PreAuthorize("train:course:ware:page")
    public RestResponse<PageInfo<CourseWarePageResponseVM>> page(@RequestBody CourseWarePageRequestVM model) {
        initPermission(model);
        PageInfo<CourseWare> pageInfo = courseWareService.page(model);
        PageInfo<CourseWarePageResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> {
            CourseWarePageResponseVM courseWarePageResponseVM = courseWareMapping.toCourseWareResponseVM(d);
            courseWarePageResponseVM.setFileTypeStr(CourseWareFileTypeEnum.fromCode(d.getFileType()).getName());
            courseWarePageResponseVM.setMaxLengthStr(DateTimeUtil.secondToChineseTime(d.getMaxLength()));
            if (d.getCourseWareArchiveId() != null) {
                CourseWareArchive courseWareArchive = courseWareArchiveService.getById(d.getCourseWareArchiveId());
                courseWarePageResponseVM.setLevel(courseWareArchive.getLevel());
            }
            return courseWarePageResponseVM;
        });
        return RestResponse.ok(page);
    }

    /**
     * 课件查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/{id}")
    @PreAuthorize("train:course:ware:update")
    public RestResponse<CourseWareEditRequestVM> select(@PathVariable Integer id) {
        CourseWare courseWare = courseWareService.getById(id);
        CourseWareEditRequestVM courseWareEditRequestVM = courseWareMapping.toCourseWareEditRequestVM(courseWare);
        if (CourseWareFileTypeEnum.fromCode(courseWare.getFileType()) == CourseWareFileTypeEnum.DOCUMENT) {
            courseWareEditRequestVM.setMaxLengthStr(DateTimeUtil.secondToTime(courseWare.getMaxLength()));
        }
        List<CourseWareQuestion> courseWareQuestionList = courseWareService.getCourseWareQuestion(id);
        List<CourseWareQuestionVM> courseWareQuestionVMList = courseWareQuestionList.stream().map(item -> {
            CourseWareQuestionVM courseWareQuestionVM = courseWareMapping.toCourseWareQuestionVM(item);
            QuestionEditRequestVM questionEditRequestVM = questionService.selectQuestionEditRequestVM(item.getQuestionId());
            courseWareQuestionVM.setTitle(questionEditRequestVM.getTitle());
            return courseWareQuestionVM;
        }).collect(Collectors.toList());
        courseWareEditRequestVM.setQuestionList(courseWareQuestionVMList);
        return RestResponse.ok(courseWareEditRequestVM);
    }


    /**
     * 课件创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create")
    @PreAuthorize("train:course:ware:create")
    public RestResponse create(@RequestBody @Valid CourseWareEditRequestVM model) {
        RestResponse response = courseWareValid(model);
        if (response.getCode() != SystemCode.OK.getCode()) {
            return response;
        }
        User user = getCurrentUser();
        CourseWare newCourseWare = courseWareMapping.toCourseWare(model);
        newCourseWare.setDeleted(false);
        newCourseWare.setCreateTime(new Date());
        newCourseWare.setCreateUser(user.getId());
        newCourseWare.setCreateDepartmentId(user.getDepartmentId());
        courseWareService.save(newCourseWare);
        model.getQuestionList().forEach(item -> {
            CourseWareQuestion courseWareQuestion = courseWareMapping.toCourseWareQuestion(item);
            courseWareQuestion.setCourseWareId(newCourseWare.getId());
            courseWareQuestion.setDeleted(false);
            courseWareQuestion.setCreateUser(user.getId());
            courseWareQuestion.setCreateDepartmentId(user.getDepartmentId());
            courseWareQuestion.setCreateTime(new Date());
            courseWareService.insertCourseWareQuestion(courseWareQuestion);
        });
        return RestResponse.ok();
    }


    /**
     * 课件更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update")
    @PreAuthorize("train:course:ware:update")
    public RestResponse update(@RequestBody @Valid CourseWareEditRequestVM model) {
        RestResponse response = courseWareValid(model);
        if (response.getCode() != SystemCode.OK.getCode()) {
            return response;
        }
        User user = getCurrentUser();
        CourseWare oldCourseWare = courseWareService.getById(model.getId());
        courseWareMapping.mapCourseWare(model, oldCourseWare);
        courseWareService.updateById(oldCourseWare);
        courseWareService.deleteCourseWareQuestionByCourseWareId(model.getId());
        model.getQuestionList().forEach(item -> {
            CourseWareQuestion courseWareQuestion = courseWareMapping.toCourseWareQuestion(item);
            courseWareQuestion.setCourseWareId(oldCourseWare.getId());
            courseWareQuestion.setDeleted(false);
            courseWareQuestion.setCreateUser(user.getId());
            courseWareQuestion.setCreateDepartmentId(user.getDepartmentId());
            courseWareQuestion.setCreateTime(new Date());
            courseWareService.insertCourseWareQuestion(courseWareQuestion);
        });
        return RestResponse.ok();
    }


    /**
     * 课件删除
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("train:course:ware:delete")
    public RestResponse delete(@PathVariable Integer id) {
        CourseWare courseWare = courseWareService.getById(id);
        courseWare.setDeleted(true);
        courseWareService.updateById(courseWare);
        return RestResponse.ok();
    }


    /**
     * 课件视频上传
     *
     * @param request
     * @return {@link RestResponse}
     */
    @RequestMapping("/video/file")
    @ResponseBody
    public RestResponse videoFile(HttpServletRequest request) throws IOException, EncoderException {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        String fileName = multipartFile.getOriginalFilename();
        String filePath = null;
        File localTempFile = File.createTempFile(String.format("%s_%s", UUID.randomUUID(), multipartFile.getOriginalFilename()), "");
        try (InputStream inputStream = multipartFile.getInputStream()) {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            try (OutputStream outStream = new FileOutputStream(localTempFile)) {
                outStream.write(buffer);
                filePath = fileUploadService.fileUpload(localTempFile, fileName, "course/video", true, true);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return RestResponse.fail(2, "文件上传失败");
        }
        CourseWareFileVM courseWareFileVM = new CourseWareFileVM();
        courseWareFileVM.setPath(filePath);
        courseWareFileVM.setFileName(fileName);
        String oldTmpDir = System.getProperty("java.io.tmpdir");
        logger.info("当前java临时目录：[{}]",oldTmpDir);
        System.setProperty("java.io.tmpdir", System.getProperty("user.dir"));
        logger.info("修改当前java临时目录：[{}]",System.getProperty("java.io.tmpdir"));
        MultimediaObject multimediaObject = new MultimediaObject(localTempFile);
        MultimediaInfo multimediaInfo = multimediaObject.getInfo();
        Long duration = multimediaInfo.getDuration();
        courseWareFileVM.setVideoLength(duration.intValue() / 1000);
        logger.info("上传视频文件后，删除临时文件");
        if(localTempFile.delete()){
            logger.info("删除临时文件-成功");
        }else {
            logger.info("删除临时文件-失败");
        }
        System.setProperty("java.io.tmpdir", oldTmpDir);
        logger.info("还原当前java临时目录：[{}]",System.getProperty("java.io.tmpdir"));
        return RestResponse.ok(courseWareFileVM);
    }


    /**
     * 课件文档上传
     *
     * @param request the request
     * @return the rest response
     * @throws IOException     the io exception
     * @throws OfficeException the office exception
     */
    @RequestMapping("/document/file")
    @ResponseBody
    public RestResponse documentFile(HttpServletRequest request) throws IOException, OfficeException {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        String fileName = multipartFile.getOriginalFilename();
        File tempFile = File.createTempFile(UUID.randomUUID().toString(), "");
        try (InputStream inputStream = multipartFile.getInputStream()) {
            try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {
                IoUtil.copy(inputStream, fileOutputStream);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return RestResponse.fail(2, "文件上传失败");
        }

        String originalFilePath = fileUploadService.fileUpload(tempFile, fileName, "course/document/original", true, true);
        String previewFilePath;
        if (fileName.endsWith(".pdf")) {
            previewFilePath = originalFilePath;
        } else {
            return RestResponse.fail(SystemCode.InnerError.getCode(),"请上传pdf文件");
//            File pdfFile = File.createTempFile(UUID.randomUUID().toString(), ".pdf");
//            converter.convert(tempFile).to(pdfFile).execute();
//            previewFilePath = fileUploadService.fileUpload(pdfFile, pdfFile.getName(), "course/document/preview", true, false);
        }

        CourseWareFileVM courseWareFileVM = new CourseWareFileVM();
        courseWareFileVM.setPath(originalFilePath);
        courseWareFileVM.setFileName(fileName);
        courseWareFileVM.setOriginalPath(originalFilePath);
        courseWareFileVM.setPreviewPath(previewFilePath);
        return RestResponse.ok(courseWareFileVM);
    }

    /**
     * 课件参数验证
     *
     * @param model
     * @return {@link RestResponse}
     */
    private RestResponse courseWareValid(CourseWareEditRequestVM model) {
        CourseWareFileTypeEnum courseWareFileTypeEnum = CourseWareFileTypeEnum.fromCode(model.getFileType());
        try {
            if (courseWareFileTypeEnum == CourseWareFileTypeEnum.DOCUMENT) {
                if (null == model.getMaxLengthStr()) {
                    return RestResponse.fail(2, "请输入课件时长");
                } else {
                    model.setMaxLength(DateTimeUtil.timeToSecond(model.getMaxLengthStr()));
                }
            } else {
                model.setPreviewPath(model.getOriginalPath());
            }
            if (model.getMaxLength() == null) {
                return RestResponse.fail(2, "请重新上传课件");
            }
            for (CourseWareQuestionVM courseWareQuestionVM : model.getQuestionList()) {
                courseWareQuestionVM.setAnchorSecond(DateTimeUtil.timeToSecond(courseWareQuestionVM.getAnchorFormat()));
                if (courseWareFileTypeEnum == CourseWareFileTypeEnum.VIDEO && courseWareQuestionVM.getAnchorSecond() > model.getMaxLength()) {
                    return RestResponse.fail(2, "题目出现时间大于视频总长度");
                }
            }
        } catch (Exception exception) {
            return RestResponse.fail(2, "题目出现时间填写不正确");
        }
        return RestResponse.ok();
    }
}
