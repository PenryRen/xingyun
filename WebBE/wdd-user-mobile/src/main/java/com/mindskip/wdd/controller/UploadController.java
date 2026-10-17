package com.mindskip.wdd.controller;

import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.service.FileUploadService;
import com.mindskip.wdd.service.UserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;


/**
 * @version 1.7.0
 * @description: 头像上传
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/upload")
public class UploadController extends BaseApiController {


    private final FileUploadService fileUploadService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);


    /**
     * 用户头像修改
     *
     * @param request the request
     * @return the rest response
     */
    @RequestMapping("/picture")
    @ResponseBody
    public RestResponse picture(HttpServletRequest request) {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        String fileName = multipartFile.getOriginalFilename();
        long fileSize = multipartFile.getSize();
        String filePath = null;
        try (InputStream inputStream = multipartFile.getInputStream()) {
            filePath = fileUploadService.fileUpload(inputStream, fileSize, fileName, "profile", true, true);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return RestResponse.fail(2, "文件上传失败");
        }
        User user = userService.getById(getCurrentUser().getId());
        user.setImagePath(filePath);
        userService.updateById(user);
        return RestResponse.ok(filePath);
    }


    /**
     * 人脸识别头像上传
     *
     * @param request the request
     * @return the rest response
     */
    @RequestMapping("/face")
    @ResponseBody
    public RestResponse face(HttpServletRequest request) {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        String fileName = multipartFile.getOriginalFilename();
        long fileSize = multipartFile.getSize();
        try (InputStream inputStream = multipartFile.getInputStream()) {
            String filePath = fileUploadService.fileUpload(inputStream, fileSize, fileName, "face", true, true);
            return RestResponse.ok(filePath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return RestResponse.fail(2, "文件上传失败");
        }
    }

}
