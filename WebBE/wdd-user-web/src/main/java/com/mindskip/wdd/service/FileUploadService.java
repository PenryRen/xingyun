package com.mindskip.wdd.service;


import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @version 1.7.0
 * @description: 文件上传
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public interface FileUploadService {

    /**
     * 文件上传，根据文件
     *
     * @param file     the file
     * @param fileName the file name
     * @param folder   the folder
     * @return the string
     * @throws IOException the io exception
     */
    String fileUpload(File file, String fileName, String folder, Boolean timeSplit, Boolean randomSplit) throws IOException;

    /**
     * 文件上传，根据文件流
     *
     * @param inputStream the input stream
     * @param fileSize    the file size
     * @param fileName    the file name
     * @param folder      the folder
     * @return the string
     * @throws IOException the io exception
     */
    String fileUploadOriginal(InputStream inputStream, long fileSize, String fileName, String folder, Boolean timeSplit, Boolean randomSplit) throws IOException;

    /**
     * 文件上传，根据文件流
     *
     * @param inputStream the input stream
     * @param fileSize    the file size
     * @param fileName    the file name
     * @param folder      the folder
     * @return the string
     * @throws IOException the io exception
     */
    String fileUpload(InputStream inputStream, long fileSize, String fileName, String folder, Boolean timeSplit, Boolean randomSplit) throws IOException;

    /**
     * 流式输出 OSS 文件到响应，强制 Content-Disposition: inline，支持 Range 请求
     *
     * @param key         OSS 对象 key
     * @param response    HTTP 响应
     * @param rangeHeader Range 请求头
     * @throws IOException IO 异常
     */
    void streamOssFile(String key, HttpServletResponse response, String rangeHeader) throws IOException;
}
