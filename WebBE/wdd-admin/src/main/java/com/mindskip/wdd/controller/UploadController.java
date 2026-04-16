package com.mindskip.wdd.controller;


import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.service.FileUploadService;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareFileVM;
import com.mindskip.wdd.viewmodel.upload.UploadItemVM;
import com.mindskip.wdd.viewmodel.upload.UploadResultVM;
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
import java.util.UUID;


/**
 * @version 1.7.0
 * @description: 文件上传接口
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/upload")
public class UploadController extends BaseApiController {

    private final FileUploadService fileUploadService;
    private static final Logger logger = LoggerFactory.getLogger(com.mindskip.wdd.controller.UploadController.class);
    private static final String STATE = "SUCCESS";


    /**
     * 图片上传
     *
     * @param request the request
     * @return the upload result vm
     */
    @RequestMapping("/image")
    @ResponseBody
    public UploadResultVM image(HttpServletRequest request) {
        String filePath = null;
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("imageFile");
        String fileName = multipartFile.getOriginalFilename();
        long attachSize = multipartFile.getSize();
        try (InputStream inputStream = multipartFile.getInputStream()) {
            filePath = fileUploadService.fileUpload(inputStream, attachSize, fileName, "editor/image", true, true);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return UploadResultVM.ok(filePath, fileName);
    }


    /**
     * 视频上传
     *
     * @param request the request
     * @return the upload result vm
     */
    @RequestMapping("/video")
    @ResponseBody
    public UploadResultVM video(HttpServletRequest request) {
        UploadResultVM uploadResultVM = new UploadResultVM();
        MultipartHttpServletRequest videoMultipartHttpServletRequest = (MultipartHttpServletRequest) request;
        videoMultipartHttpServletRequest.getFileMap().forEach((key, videoMultipartFile) -> {
            String videoFileName = videoMultipartFile.getOriginalFilename();
            videoFileName = "video" + videoFileName.substring(videoFileName.lastIndexOf("."));  //文件重命名，解决微信小程序 音频特殊名字不能播放问题
            long videoAttachSize = videoMultipartFile.getSize();
            try (InputStream inputStream = videoMultipartFile.getInputStream()) {
                String filePath = fileUploadService.fileUpload(inputStream, videoAttachSize, videoFileName, "editor/video", true, true);
                uploadResultVM.setData(new UploadItemVM(filePath));
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        });
        return uploadResultVM;
    }


    /**
     * 附件上传
     *
     * @param request the request
     * @return the upload result vm
     */
    @RequestMapping("/attachment")
    @ResponseBody
    public UploadResultVM file(HttpServletRequest request) {
        UploadResultVM uploadResultVM = new UploadResultVM();
        MultipartHttpServletRequest attachmentMultipartHttpServletRequest = (MultipartHttpServletRequest) request;
        attachmentMultipartHttpServletRequest.getFileMap().forEach((key, attachmentMultipartFile) -> {
            String attachmentFileName = attachmentMultipartFile.getOriginalFilename();
            attachmentFileName = UUID.randomUUID() + attachmentFileName.substring(attachmentFileName.lastIndexOf("."));
            long attachmentAttachSize = attachmentMultipartFile.getSize();
            try (InputStream inputStream = attachmentMultipartFile.getInputStream()) {
                String filePath = fileUploadService.fileUpload(inputStream, attachmentAttachSize, attachmentFileName, "editor/attachment", true, false);
                uploadResultVM.setData(new UploadItemVM(filePath, attachmentMultipartFile.getOriginalFilename()));
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        });
        return uploadResultVM;
    }


    /**
     * 音频
     *
     * @param request the request
     * @return the upload result vm
     */
    @RequestMapping("/audio")
    @ResponseBody
    public UploadResultVM audio(HttpServletRequest request) {
        UploadResultVM uploadResultVM = new UploadResultVM();
        MultipartHttpServletRequest audioMultipartHttpServletRequest = (MultipartHttpServletRequest) request;
        audioMultipartHttpServletRequest.getFileMap().forEach((key, audioMultipartFile) -> {
            String audioFileName = audioMultipartFile.getOriginalFilename();
            audioFileName = "audio" + audioFileName.substring(audioFileName.lastIndexOf("."));  //文件重命名，解决微信小程序 音频特殊名字不能播放问题
            long audioAttachSize = audioMultipartFile.getSize();
            try (InputStream inputStream = audioMultipartFile.getInputStream()) {
                String filePath = fileUploadService.fileUpload(inputStream, audioAttachSize, audioFileName, "editor/audio", true, true);
                uploadResultVM.setData(new UploadItemVM(filePath));
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        });
        return uploadResultVM;
    }


    /**
     * 文件上传，自定义文件夹
     *
     * @param request the request
     * @return the rest response
     */
    @RequestMapping("/folder/file")
    @ResponseBody
    public RestResponse folderFile(HttpServletRequest request) {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        String fileName = multipartFile.getOriginalFilename();
        long fileSize = multipartFile.getSize();
        String filePath = null;
        String folder = request.getParameter("folder");
        try (InputStream inputStream = multipartFile.getInputStream()) {
            filePath = fileUploadService.fileUploadOriginal(inputStream, fileSize, fileName, folder, true, true);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return RestResponse.fail(2, "文件上传失败");
        }
        CourseWareFileVM courseWareFileVM = new CourseWareFileVM();
        courseWareFileVM.setPath(filePath);
        courseWareFileVM.setFileName(fileName);
        return RestResponse.ok(courseWareFileVM);
    }


    /**
     * 图片上传，自定义文件夹
     *
     * @param request the request
     * @return the upload result vm
     */
    @RequestMapping("/folder/image")
    @ResponseBody
    public UploadResultVM folderImage(HttpServletRequest request) {
        String filePath = null;
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("upFile");
        String fileName = multipartFile.getOriginalFilename();
        long attachSize = multipartFile.getSize();
        String folder = request.getParameter("folder");
        try (InputStream inputStream = multipartFile.getInputStream()) {
            filePath = fileUploadService.fileUpload(inputStream, attachSize, fileName, folder, true, true);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return new UploadResultVM(fileName, fileName, filePath, multipartFile.getSize(), fileName, STATE);
    }

}
