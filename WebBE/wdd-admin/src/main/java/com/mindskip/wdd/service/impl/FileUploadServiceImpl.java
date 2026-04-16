package com.mindskip.wdd.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ReUtil;
import com.mindskip.wdd.service.FileUploadService;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
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
    @Value("${system.resource.qcloud.region}")
    private String region;
    @Value("${system.resource.qcloud.url}")
    private String url;
    @Value("${system.resource.qcloud.bucket}")
    private String bucket;
    @Value("${system.resource.qcloud.secret-id}")
    private String secretId;
    @Value("${system.resource.qcloud.secret-key}")
    private String secretKey;
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
            COSClient cosClient = getCOSClient();
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, filePath, file);
            cosClient.putObject(putObjectRequest);
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
        COSClient cosClient = getCOSClient();
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
        cosClient.putObject(putObjectRequest);
        return String.format("%s/%s", url, filePath);
    }

    /**
     * 获取腾讯云对象存储信息
     *
     * @return {@link COSClient}
     */
    private COSClient getCOSClient() {
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region qRegion = new Region(region);
        ClientConfig clientConfig = new ClientConfig(qRegion);
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }

}
