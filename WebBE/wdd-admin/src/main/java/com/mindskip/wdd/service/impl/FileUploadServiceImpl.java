package com.mindskip.wdd.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReUtil;
import com.mindskip.wdd.service.FileUploadService;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * @version 1.7.0
 * @description: 文件上传
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${system.resource.local-store}")
    private Boolean localStore;
    @Value("${system.resource.file.location}")
    private String fileLocation;
    @Value("${system.resource.file.url}")
    private String fileUrl;
    @Value("${system.resource.file.allow}")
    private String allow;
    @Value("${system.resource.aliyun.endpoint}")
    private String endpoint;
    @Value("${system.resource.aliyun.url}")
    private String url;
    @Value("${system.resource.aliyun.bucket}")
    private String bucket;
    @Value("${system.resource.aliyun.access-key-id}")
    private String accessKeyId;
    @Value("${system.resource.aliyun.access-key-secret}")
    private String accessKeySecret;
    private final static String FOLDER_NAME_REGEX = "[A-Za-z0-9/-]{1,30}";

    @Override
    public String fileUpload(File file, String fileName, String folder, Boolean timeSplit, Boolean randomSplit) {
        if (!ReUtil.isMatch(FOLDER_NAME_REGEX, folder)) {
            return null;
        }
        if (!Arrays.stream(allow.split("\\|")).anyMatch(fe -> fileName.endsWith(fe))) {
            return null;
        }
        if (localStore) {
            String directory = folder;
            if (timeSplit) {
                directory += ("/" + DateTimeUtil.dateTimeNumberFormat(new Date()));
            }
            if (randomSplit) {
                directory += ("/" + UUID.randomUUID());
            }
            FileUtil.mkdir(String.format("%s/%s", fileLocation, directory));
            File destFile = new File(String.format("%s/%s/%s", fileLocation, directory, fileName));
            FileUtil.copyFile(file, destFile);
            return String.format("%s/%s/%s", fileUrl, directory, fileName);
        } else {
            String filePath = folder;
            if (timeSplit) {
                filePath += ("/" + DateTimeUtil.dateTimeNumberFormat(new Date()));
            }
            if (randomSplit) {
                filePath += ("/" + UUID.randomUUID());
            }
            filePath += ("/" + fileName);
            OSS ossClient = getOSSClient();
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, filePath, file);
            ossClient.putObject(putObjectRequest);
            ossClient.shutdown();
            return String.format("%s/%s", url, filePath);
        }
    }

    @Override
    public String fileUpload(InputStream inputStream, long fileSize, String fileName, String folder, Boolean timeSplit, Boolean randomSplit) throws IOException {
        if (localStore) {
            return localUpload(inputStream, fileName, folder, timeSplit, randomSplit);
        } else {
            return ossUpload(url, inputStream, fileSize, fileName, folder, timeSplit, randomSplit);
        }
    }


    @Override
    public String fileUploadOriginal(InputStream inputStream, long fileSize, String fileName, String folder, Boolean timeSplit, Boolean randomSplit) throws IOException {
        if (localStore) {
            return localUpload(inputStream, fileName, folder, timeSplit, randomSplit);
        } else {
            return ossUpload(url, inputStream, fileSize, fileName, folder, timeSplit, randomSplit);
        }
    }


    /**
     * 文件本地存储
     *
     * @param inputStream
     * @param fileName
     * @param folder
     * @param timeSplit
     * @param randomSplit
     * @return {@link String}
     * @throws IOException
     */
    private String localUpload(InputStream inputStream, String fileName, String folder, Boolean timeSplit, Boolean randomSplit) throws IOException {
        if (!ReUtil.isMatch(FOLDER_NAME_REGEX, folder)) {
            return null;
        }
        if (!Arrays.stream(allow.split("\\|")).anyMatch(fe -> fileName.endsWith(fe))) {
            return null;
        }
        String directory = folder;
        if (timeSplit) {
            directory += ("/" + DateTimeUtil.dateTimeNumberFormat(new Date()));
        }
        if (randomSplit) {
            directory += ("/" + UUID.randomUUID());
        }
        FileUtil.mkdir(String.format("%s/%s", fileLocation, directory));
        File destFile = new File(String.format("%s/%s/%s", fileLocation, directory, fileName));
        try (FileOutputStream fileOutputStream = new FileOutputStream(destFile)) {
            IoUtil.copy(inputStream, fileOutputStream);
        }
        return String.format("%s/%s/%s", fileUrl, directory, fileName);
    }

    /**
     * 文件腾讯云对象存储
     *
     * @param url
     * @param inputStream
     * @param fileSize
     * @param fileName
     * @param folder
     * @param timeSplit
     * @param randomSplit
     * @return {@link String}
     */
    private String ossUpload(String url, InputStream inputStream, long fileSize, String fileName, String folder, Boolean timeSplit, Boolean randomSplit) {
        if (!ReUtil.isMatch(FOLDER_NAME_REGEX, folder)) {
            return null;
        }
        if (!Arrays.stream(allow.split("\\|")).anyMatch(fe -> fileName.endsWith(fe))) {
            return null;
        }
        OSS ossClient = getOSSClient();
        String filePath = folder;
        if (timeSplit) {
            filePath += ("/" + DateTimeUtil.dateTimeNumberFormat(new Date()));
        }
        if (randomSplit) {
            filePath += ("/" + UUID.randomUUID());
        }
        filePath += ("/" + fileName);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(fileSize);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, filePath, inputStream, objectMetadata);
        ossClient.putObject(putObjectRequest);
        ossClient.shutdown();
        return String.format("%s/%s", url, filePath);
    }

    /**
     * 获取阿里云对象存储信息
     *
     * @return {@link OSS}
     */
    private OSS getOSSClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

}
