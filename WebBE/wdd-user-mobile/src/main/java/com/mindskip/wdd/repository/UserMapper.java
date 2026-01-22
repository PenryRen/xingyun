package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 用户
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据用户名查询用户
     *
     * @param username the username
     * @return the user by user
     */
    User getUserByUserName(String username);

    /**
     * 获取用户根据id列表
     *
     * @param idList the id list
     * @return the user by id list
     */
    List<User> getUserByIdList(@Param("idList") List<Integer> idList);
}
