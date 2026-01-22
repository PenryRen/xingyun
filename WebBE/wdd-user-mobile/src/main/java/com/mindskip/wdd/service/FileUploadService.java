package com.mindskip.wdd.service;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @version 1.7.0
 * @description: 文件上传
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
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
}
