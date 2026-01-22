package com.mindskip.wdd.service;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseFilter;
import com.mindskip.wdd.domain.CredentialTemplate;
import com.mindskip.wdd.domain.UserCredential;
import com.mindskip.wdd.domain.other.KeyValue;
import com.mindskip.wdd.viewmodel.answer.PaperCredentialPageRequestVM;
import com.mindskip.wdd.viewmodel.credential.CredentialPageRequestVM;
import com.mindskip.wdd.viewmodel.credential.CredentialUserPageRequestVM;

import java.io.IOException;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 证书模板
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface CredentialService {

    /**
     * 证书列表
     *
     * @param baseFilter
     * @return
     */
    List<KeyValue> list(BaseFilter baseFilter);

    /**
     * 证书模板分页查询
     *
     * @param requestVM
     * @return
     */
    PageInfo<CredentialTemplate> page(CredentialPageRequestVM requestVM);

    /**
     * 获取证书模板，根据id
     *
     * @param id
     * @return
     */
    CredentialTemplate getCredentialTemplateById(Integer id);

    /**
     * 插入证书模板
     *
     * @param credentialTemplate
     */
    void insertCredentialTemplate(CredentialTemplate credentialTemplate);

    /**
     * 根据证书模板
     *
     * @param credentialTemplate
     */
    void updateCredentialTemplate(CredentialTemplate credentialTemplate);

    /**
     * 用户证书分页查询
     *
     * @param paperCredentialPageRequestVM
     * @return
     */
    PageInfo<UserCredential> page(PaperCredentialPageRequestVM paperCredentialPageRequestVM);

    /**
     * 生成用户证书
     *
     * @param userCredential
     * @return
     * @throws IOException
     */
    String buildCredential(UserCredential userCredential) throws IOException;

    /**
     * 用户证书数量查询
     *
     * @param paperBuildId
     * @param alreadyBuild
     * @return
     */
    Integer userCredentialCount(Long paperBuildId, Boolean alreadyBuild);


    /**
     * 用户证书列表
     *
     * @param model 模型
     * @return {@link PageInfo}<{@link UserCredential}>
     */
    PageInfo<UserCredential> userPage(CredentialUserPageRequestVM model);
}
