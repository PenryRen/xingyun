package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.TrainUser;
import com.mindskip.wdd.viewmodel.train.detail.TrainPageRequestVM;
import com.mindskip.wdd.viewmodel.train.detail.TrainPageResponseVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 用户培训
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/26 10:45
 */
@Mapper
public interface TrainUserMapper extends BaseMapper<TrainUser> {

    /**
     * 获取培训数量
     *
     * @param trainId
     * @param status
     * @return int
     */
    int trainStatusCount(Integer trainId, Integer status);


    /**
     * 用户培训分页
     *
     * @param requestVM
     * @return {@link List}<{@link TrainPageResponseVM}>
     */
    List<TrainPageResponseVM> userPage(TrainPageRequestVM requestVM);

    /**
     * 获取用户培训
     *
     * @param trainId
     * @param userId
     * @return {@link TrainUser}
     */
    TrainUser selectTrainUser(Integer trainId, Integer userId);
}