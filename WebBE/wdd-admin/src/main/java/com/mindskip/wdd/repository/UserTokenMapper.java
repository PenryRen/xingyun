package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.UserToken;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version 1.7.0
 * @description: 用户token
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper
public interface UserTokenMapper extends BaseMapper<UserToken> {

    /**
     * 获取用户token
     *
     * @param token the token
     * @return the token
     */
    UserToken getToken(String token);
}
