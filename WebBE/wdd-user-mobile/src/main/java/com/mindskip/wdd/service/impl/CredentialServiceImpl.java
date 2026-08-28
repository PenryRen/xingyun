package com.mindskip.wdd.service.impl;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.CredentialTemplate;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.UserCredential;
import com.mindskip.wdd.domain.enums.CredentialItemFontEnum;
import com.mindskip.wdd.repository.CredentialTemplateMapper;
import com.mindskip.wdd.repository.UserCredentialMapper;
import com.mindskip.wdd.repository.UserMapper;
import com.mindskip.wdd.service.CredentialService;
import com.mindskip.wdd.service.FileUploadService;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.certificate.ImageUtil;
import com.mindskip.wdd.viewmodel.credential.CredentialPageRequestVM;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 合格证书
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@RequiredArgsConstructor
public class CredentialServiceImpl implements CredentialService {

    private final CredentialTemplateMapper credentialTemplateMapper;
    private final UserCredentialMapper userCredentialMapper;
    private final UserMapper userMapper;
    private final FileUploadService fileUploadService;
    @Value("${system.resource.file.location}")
    private String fileLocation;


    @Override
    public PageInfo<UserCredential> page(CredentialPageRequestVM credentialPageRequestVM) {
        return PageHelper.startPage(credentialPageRequestVM.getPageIndex(), credentialPageRequestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                userCredentialMapper.page(credentialPageRequestVM));
    }

    @Override
    public String buildCredential(UserCredential userCredential) throws IOException {
        User user = userMapper.selectById(userCredential.getUserId());
        CredentialTemplate credentialTemplate = credentialTemplateMapper.selectById(userCredential.getCredentialTemplateId());
        String templatePath = checkCredentialTemplateDownLoad(credentialTemplate.getTemplateImagePath());
        File templateFile = new File(templatePath);
        File credentialFile = File.createTempFile(UUID.randomUUID().toString(), ".png");
        if (null != userCredential.getExamPaperBuildId()) {
            userCredential.setCredentialNo(String.format("%s-%s", userCredential.getExamPaperBuildId(), userCredential.getId()));
        } else if (null != userCredential.getTrainId()) {
            userCredential.setCredentialNo(String.format("%s-%s", userCredential.getTrainId(), userCredential.getId()));
        }
        //证书生成
        List<ImageUtil.FontLocation> fontLocationList = credentialTemplate.getConfiguration().stream().map(item -> {
            ImageUtil.FontLocation fontLocation = new ImageUtil.FontLocation();
            fontLocation.setType(item.getType());
            fontLocation.setX(item.getElementX());
            fontLocation.setY(item.getElementY());
            fontLocation.setWeight(item.getFontWeight());
            fontLocation.setColor(item.getFontColor());
            fontLocation.setSize(item.getFontSize());
            CredentialItemFontEnum credentialItemFontEnum = CredentialItemFontEnum.fromCode(item.getType());
            switch (credentialItemFontEnum) {
                case PAPER_NAME:
                    if (null != userCredential.getExamPaperName()) {
                        fontLocation.setText(userCredential.getExamPaperName());
                    }
                    break;
                case COURSE:
                    if (null != userCredential.getTrainName()) {
                        fontLocation.setText(userCredential.getTrainName());
                    }
                    break;
                case REAL_NAME:
                    fontLocation.setText(user.getRealName());
                    break;
                case NO:
                    if (null != userCredential.getCredentialNo()) {
                        fontLocation.setText(userCredential.getCredentialNo());
                    }
                    break;
                case CREATE_TIME:
                    fontLocation.setText(String.format("%s", DateTimeUtil.dateChineseFormat(userCredential.getCredentialBuildTime())));
                    break;
                case VALIDITY:
                    fontLocation.setText(String.format("%s", ExamUtil.monToVM(item.getValidityMonth())));
                    break;
                case SEAL:
                    fontLocation.setText(credentialTemplate.getCompany());
                    break;
                case CUSTOMS:
                    fontLocation.setText(item.getText());
                    break;
            }
            return fontLocation;
        }).collect(Collectors.toList());

        ImageUtil.pressText(templateFile, credentialFile, fontLocationList);

        //证书上传
        String credentialUrl = fileUploadService.fileUpload(credentialFile.getAbsoluteFile(), credentialFile.getName(), "credential", true, false);
        userCredential.setCredentialImagePath(credentialUrl);
        userCredential.setUserRealName(user.getRealName());
        userCredentialMapper.updateById(userCredential);
        return credentialUrl;
    }

    @Override
    public UserCredential trainCredential(Long trainUserId) {
        return userCredentialMapper.trainCredential(trainUserId);
    }


    /**
     * 证书模板下载
     *
     * @param templateUrlPath
     * @return {@link String}
     */
    private String checkCredentialTemplateDownLoad(String templateUrlPath) {
        String credentialDirectory = String.format("%s/credential/template", fileLocation);
        String templatePath = String.format("%s/%s.%s", credentialDirectory, SecureUtil.md5(templateUrlPath), FileNameUtil.getSuffix(templateUrlPath));
        if (FileUtil.exist(templatePath)) {
            return templatePath;
        }
        File credentialDir = new File(credentialDirectory);
        if (!credentialDir.exists()) {
            credentialDir.mkdir();
        }
        HttpUtil.downloadFile(templateUrlPath, templatePath);
        return templatePath;
    }

}
