package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.UserCredential;
import com.mindskip.wdd.viewmodel.credential.CredentialPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 用户合格证书
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface UserCredentialMapper extends BaseMapper<UserCredential> {
    /**
     * 用户证书分页
     *
     * @param credentialPageRequestVM
     * @return
     */
    List<UserCredential> page(CredentialPageRequestVM credentialPageRequestVM);


    /**
     * 获取用户培训证书
     *
     * @param trainUserId
     * @return {@link UserCredential}
     */
    UserCredential trainCredential(Long trainUserId);
}