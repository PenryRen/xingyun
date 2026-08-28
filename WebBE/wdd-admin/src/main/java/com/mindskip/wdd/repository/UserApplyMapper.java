package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.UserApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 用户报名
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper
public interface UserApplyMapper extends BaseMapper<UserApply> {

    /**
     * 查询用户是否报名
     *
     * @param userId  the user id
     * @param applyId the apply id
     * @return the user apply
     */
    UserApply getUserApply(@Param("userId") Integer userId, @Param("applyId") Integer applyId);

    /**
     * 根据报名id获取报名的用户
     *
     * @param id the id
     * @return the list
     */
    List<Integer> userIdListByApplyId(Integer id);
}