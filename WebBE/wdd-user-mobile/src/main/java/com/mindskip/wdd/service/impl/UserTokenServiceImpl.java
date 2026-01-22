package com.mindskip.wdd.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.configuration.spring.cache.CacheConfig;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.UserToken;
import com.mindskip.wdd.repository.UserTokenMapper;
import com.mindskip.wdd.service.SystemService;
import com.mindskip.wdd.service.UserTokenService;
import com.mindskip.wdd.utility.DateTimeUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Service
@AllArgsConstructor
public class UserTokenServiceImpl extends ServiceImpl<UserTokenMapper, UserToken> implements UserTokenService {

    private final static String CACHE_NAME = "ueit:token";
    private final static String USER_ID_CACHE_NAME = "ueit:login_userid";
    private final UserTokenMapper userTokenMapper;
    private final CacheConfig cacheConfig;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SystemService systemService;

    // 是否允许账户多终端同时登录（true允许 false不允许）
    @Value("${token.soloLogin}")
    private boolean soloLogin;



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
        return userToken;
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "#userToken.token")
    public void removeToken(UserToken userToken) {
        userTokenMapper.deleteById(userToken.getId());
    }


}
