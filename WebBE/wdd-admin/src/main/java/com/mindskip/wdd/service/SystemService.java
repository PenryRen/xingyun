package com.mindskip.wdd.service;

import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.SystemStatus;
import com.mindskip.wdd.domain.User;

import java.time.Duration;
import java.util.List;


/**
 * @version 1.7.0
 * @description: 系统配置
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface SystemService {

    /**
     * 验证用户密码是否正确
     *
     * @param user     the user
     * @param username the username
     * @param password the password
     * @return the boolean
     */
    boolean authUser(User user, String username, String password);

    /**
     * 密码解密
     *
     * @param encodePwd the encode pwd
     * @return the string
     */
    String pwdDecode(String encodePwd);

    /**
     * 密码加密
     *
     * @param password the password
     * @return the string
     */
    String pwdEncode(String password);

    /**
     * pair-one 加密
     *
     * @param encodeStr the encode str
     * @return the string
     */
    String pairOneEncode(String encodeStr);

    /**
     * pair-one 解密
     *
     * @param decodeStr the decode str
     * @return the string
     */
    String pairOneDecode(String decodeStr);

    /**
     * 试卷加密
     *
     * @param object the object
     * @return the rest response
     */
    RestResponse paperEncrypt(Object object);

    /**
     * 获取权限验证忽略地址
     *
     * @return the security ignore urls
     */
    List<String> getSecurityIgnoreUrls();

    /**
     * Gets 获取系统名称.
     *
     * @return the name
     */
    String getName();

    /**
     * 获得mysql版本
     *
     * @return {@link String}
     */
    String getMysqlVersion();

    /**
     * 获取mysql系统状态
     *
     * @param key the key
     * @return the status
     */
    List<SystemStatus> getStatus(String key);


    /**
     * 获取token到期时间
     *
     * @return the token time to live
     */
    Duration getTokenTimeToLive();
}
