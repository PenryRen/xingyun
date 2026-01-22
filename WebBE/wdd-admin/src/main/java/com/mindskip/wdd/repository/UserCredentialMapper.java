package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.UserCredential;
import com.mindskip.wdd.viewmodel.answer.PaperCredentialPageRequestVM;
import com.mindskip.wdd.viewmodel.credential.CredentialUserPageRequestVM;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 用户证书
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface UserCredentialMapper extends BaseMapper<UserCredential> {

    /**
     * 用户证书分页
     *
     * @param credentialPageRequestVM the credential page request vm
     * @return the list
     */
    List<UserCredential> page(PaperCredentialPageRequestVM credentialPageRequestVM);

    /**
     * 用户证书统计
     *
     * @param paperBuildId the paper build id
     * @param alreadyBuild the already build
     * @return the integer
     */
    Integer userCredentialCount(@Param("paperBuildId") Long paperBuildId, @Param("alreadyBuild") Boolean alreadyBuild);


    /**
     * 获取等待生成证书数量
     *
     * @return {@link Integer}
     */
    Integer waitBuildCredentialCount();

    /**
     * 待生产证书分页
     *
     * @param pageIndex
     * @param pageSize
     * @return {@link List}<{@link UserCredential}>
     */
    List<UserCredential> waitBuildCredentialPage(Integer pageIndex, Integer pageSize);


    /**
     * 用户证书分页
     *
     * @param model
     * @return {@link List}<{@link UserCredential}>
     */
    List<UserCredential> userPage(CredentialUserPageRequestVM model);
}