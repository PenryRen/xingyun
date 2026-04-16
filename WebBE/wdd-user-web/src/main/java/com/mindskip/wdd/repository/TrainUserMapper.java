package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.TrainUser;
import com.mindskip.wdd.viewmodel.user.train.UserTrainPageRequestVM;
import com.mindskip.wdd.viewmodel.user.train.UserTrainPageResponseVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 用户培训
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Mapper
public interface TrainUserMapper extends BaseMapper<TrainUser> {
    /**
     * 获取用户培训
     *
     * @param trainId
     * @param userId
     * @return {@link TrainUser}
     */
    TrainUser selectTrainUser(Integer trainId, Integer userId);

    /**
     * 用户培训分页
     *
     * @param userTrainPageRequestVM
     * @return {@link List}<{@link UserTrainPageResponseVM}>
     */
    List<UserTrainPageResponseVM> userTrainPage(UserTrainPageRequestVM userTrainPageRequestVM);
}