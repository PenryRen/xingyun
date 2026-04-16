package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.UserApply;
import com.mindskip.wdd.viewmodel.apply.ApplyPageRequestVM;
import com.mindskip.wdd.viewmodel.apply.UserApplyPageResponseVM;
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
     * 用户报名分页
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<UserApplyPageResponseVM> page(ApplyPageRequestVM requestVM);
}