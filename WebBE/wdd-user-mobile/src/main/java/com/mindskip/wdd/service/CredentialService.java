package com.mindskip.wdd.service;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.UserCredential;
import com.mindskip.wdd.viewmodel.credential.CredentialPageRequestVM;

import java.io.IOException;

/**
 * @version 1.7.0
 * @description: 合格证书
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface CredentialService {

    /**
     * 用户证书分页
     *
     * @param credentialPageRequestVM the credential page request vm
     * @return the page info
     */
    PageInfo<UserCredential> page(CredentialPageRequestVM credentialPageRequestVM);

    /**
     * 证书生成
     *
     * @param userCredential the user credential
     * @return the string
     * @throws IOException the io exception
     */
    String buildCredential(UserCredential userCredential) throws IOException;


    /**
     * 用户培训证书
     *
     * @param trainUserId
     * @return {@link UserCredential}
     */
    UserCredential trainCredential(Long trainUserId);

}
