package com.mindskip.wdd.service;

import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.viewmodel.common.EncryptKV;

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
     * pair-two 对象加密
     *
     * @param object
     * @return
     */
    RestResponse pairTwoObjectEncrypt(Object object);

    /**
     * pair-one 对象解密
     *
     * @param encryptKV
     * @param valueType
     * @param <T>
     * @return
     */
    <T> T pairOneObjectDecrypt(EncryptKV encryptKV, Class<T> valueType);

    /**
     * Gets 获取系统名称.
     *
     * @return the name
     */
    String getName();

    /**
     * 获取试卷状态缓存时间
     *
     * @return
     */
    Duration getPaperStatusTimeToLive();

    /**
     * 获取权限验证忽略地址
     *
     * @return the security ignore urls
     */
    List<String> getSecurityIgnoreUrls();

    /**
     * 获取token到期时间
     *
     * @return the token time to live
     */
    Duration getTokenTimeToLive();

    /**
     * aes解密
     *
     * @param encryptV
     * @param valueType
     * @param <T>
     * @return
     */
    <T> T aesDecrypt(String encryptV, Class<T> valueType);

    /**
     * aes加密
     *
     * @param object
     * @return
     */
    String aesEncrypt(Object object);


    /**
     * 敏感词过滤
     *
     * @param str
     * @return {@link String}
     */
    String clearStopWord(String str);
}
