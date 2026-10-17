package com.mindskip.wdd.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.configuration.spring.cache.CacheConfig;
import com.mindskip.wdd.constant.CacheConstants;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.UserToken;
import com.mindskip.wdd.repository.UserTokenMapper;
import com.mindskip.wdd.service.SystemService;
import com.mindskip.wdd.service.UserTokenService;
import com.mindskip.wdd.utility.DateTimeUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

/**
 * @version 1.7.0
 * @description: 用户token
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@AllArgsConstructor
public class UserTokenServiceImpl extends ServiceImpl<UserTokenMapper, UserToken> implements UserTokenService {

    private final static String CACHE_NAME = "ueit:token";
    private final static String KAPTCHA_CACHE_NAME = "ueit:kaptcha";
    private final UserTokenMapper userTokenMapper;
    private final CacheConfig cacheConfig;
    private final RedisTemplate redisTemplate;
    private final SystemService systemService;


    @Override
    @Cacheable(value = CACHE_NAME, key = "#token", unless = "#result == null")
    public UserToken getToken(String token) {
        return userTokenMapper.getToken(token);
    }

    @Override
    @Transactional
    public UserToken insertUserToken(User user) {
        Date startTime = new Date();
        Date endTimeAccurate = DateUtil.offsetSecond(startTime, (int) systemService.getTokenTimeToLive().getSeconds());
        Date endTime = DateTimeUtil.parse(DateUtil.format(endTimeAccurate, "yyyy-MM-dd 03:00:00"));
        UserToken userToken = new UserToken();
        userToken.setToken(UUID.randomUUID().toString());
        userToken.setUserId(user.getId());
        userToken.setCreateDepartmentId(user.getDepartmentId());
        userToken.setCreateTime(startTime);
        userToken.setEndTime(endTime);
        userToken.setUserName(user.getUserName());
        userTokenMapper.insert(userToken);
        String key = cacheConfig.simpleKeyGenerator(CACHE_NAME, userToken.getToken());
        redisTemplate.opsForValue().set(key, userToken, Duration.ofSeconds(DateUtil.between(startTime, endTime, DateUnit.SECOND)));

        if (!systemService.getSoloLogin()) {
            // 缓存用户唯一标识，防止同一帐号，同时登录
            String userIdKey = getUserIdKey(user.getId());
            redisTemplate.opsForValue().set(userIdKey, key, Duration.ofSeconds(DateUtil.between(startTime, endTime, DateUnit.SECOND)));
        }
        return userToken;
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "#userToken.token")
    public void removeToken(UserToken userToken) {
        if (!systemService.getSoloLogin() && ObjectUtils.isEmpty(userToken.getUserId())) {
            String userIdKey = getUserIdKey(userToken.getUserId());
            redisTemplate.delete(userIdKey);
        }
        userTokenMapper.deleteById(userToken.getId());
    }

    @Override
    public void saveKaptcha(String key, String value) {
        String kaptchaKey = cacheConfig.simpleKeyGenerator(KAPTCHA_CACHE_NAME, key);
        redisTemplate.opsForValue().set(kaptchaKey, value, Duration.ofSeconds(900));
    }

    @Override
    public String getKaptcha(String key) {
        String kaptchaKey = cacheConfig.simpleKeyGenerator(KAPTCHA_CACHE_NAME, key);
        Object captchaKey = redisTemplate.opsForValue().get(kaptchaKey);
        return null == captchaKey ? null : captchaKey.toString();
    }

    private String getUserIdKey(Integer userId) {
        return cacheConfig.simpleKeyGenerator(CacheConstants.LOGIN_USERID_KEY, userId.toString());
    }
}
