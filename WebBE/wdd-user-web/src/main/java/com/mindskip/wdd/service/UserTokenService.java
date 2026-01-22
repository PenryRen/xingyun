package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.UserToken;

/**
 * @version 1.7.0
 * @description: 用户token
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface UserTokenService extends IService<UserToken> {

    /**
     * 根据token获取UserToken，带缓存的
     *
     * @param token token
     * @return UserToken
     */
    UserToken getToken(String token);

    /**
     * 插入用户Token
     *
     * @param user user
     * @return UserToken
     */
    UserToken insertUserToken(User user);


    /**
     * 移除用户token
     *
     * @param userToken
     */
    void removeToken(UserToken userToken);


    /**
     * 保存验证码
     *
     * @param key
     * @param value
     */
    void saveKaptcha(String key, String value);

    /**
     * 获取验证码
     *
     * @param key
     * @return {@link String}
     */
    String getKaptcha(String key);

}
