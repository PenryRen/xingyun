package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.TrainItemUser;
import com.mindskip.wdd.viewmodel.train.course.TrainCourseWarePageResponseVM;
import com.mindskip.wdd.viewmodel.train.detail.TrainPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 用户培训列表
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/26 10:45
 */
@Mapper
public interface TrainItemUserMapper extends BaseMapper<TrainItemUser> {
    /**
     * 获取不同状态下的用户培训列表数量
     *
     * @param trainId
     * @param targetType
     * @param status
     * @return int
     */
    int trainItemStatusCount(Integer trainId, Integer targetType, Integer status);

    /**
     * 用户课件分页
     *
     * @param requestVM
     * @return {@link List}<{@link TrainCourseWarePageResponseVM}>
     */
    List<TrainCourseWarePageResponseVM> courseWarePage(TrainPageRequestVM requestVM);

    /**
     * 获取用户培训列表
     *
     * @param trainUserId
     * @return {@link List}<{@link TrainItemUser}>
     */
    List<TrainItemUser> getTrainItemUserList(Long trainUserId);
}