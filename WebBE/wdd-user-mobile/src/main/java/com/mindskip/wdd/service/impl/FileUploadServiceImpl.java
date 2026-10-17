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
import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.GetObjectRequest;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
    private final static String FOLDER_NAME_REGEX = "[A-Za-z0-9/-]{1,20}";

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

    private COSClient getCOSClient() {
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region qRegion = new Region(region);
        ClientConfig clientConfig = new ClientConfig(qRegion);
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }

    /**
     * 流式输出 COS 文件到响应，强制 Content-Disposition: inline，支持 Range 请求
     * 用于绕过 COS Bucket 强制下载开关，使视频/PDF 可在浏览器内联播放
     */
    @Override
    public void streamOssFile(String key, HttpServletResponse response, String rangeHeader) throws IOException {
        COSClient cosClient = getCOSClient();
        COSObject cosObject = null;
        try {
            ObjectMetadata metadata = cosClient.getObjectMetadata(bucket, key);
            long fileSize = metadata.getContentLength();
            String contentType = metadata.getContentType();

            response.setContentType(contentType != null ? contentType : "application/octet-stream");
            response.setHeader("Content-Disposition", "inline");
            response.setHeader("Accept-Ranges", "bytes");

            InputStream inputStream;
            long contentLength;

            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                String range = rangeHeader.substring("bytes=".length());
                String[] parts = range.split("-");
                long start = Long.parseLong(parts[0]);
                long end = parts.length > 1 && !parts[1].isEmpty() ? Long.parseLong(parts[1]) : fileSize - 1;

                if (start >= fileSize) {
                    response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                    response.setHeader("Content-Range", "bytes */" + fileSize);
                    return;
                }
                if (end >= fileSize) {
                    end = fileSize - 1;
                }

                GetObjectRequest getRequest = new GetObjectRequest(bucket, key);
                getRequest.setRange(start, end);
                cosObject = cosClient.getObject(getRequest);
                inputStream = cosObject.getObjectContent();
                contentLength = end - start + 1;

                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileSize);
                response.setContentLengthLong(contentLength);
            } else {
                cosObject = cosClient.getObject(bucket, key);
                inputStream = cosObject.getObjectContent();
                contentLength = fileSize;
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentLengthLong(contentLength);
            }

            byte[] buffer = new byte[8192];
            int bytesRead;
            try (OutputStream outputStream = response.getOutputStream()) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            } finally {
                inputStream.close();
            }
        } finally {
            if (cosObject != null) {
                cosObject.close();
            }
            cosClient.shutdown();
        }
    }
}
