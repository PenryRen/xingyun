package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.TrainItemUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 用户培训列表
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Mapper
public interface TrainItemUserMapper extends BaseMapper<TrainItemUser> {
    /**
     * 获取用户培训列表
     *
     * @param trainUserId
     * @return {@link List}<{@link TrainItemUser}>
     */
    List<TrainItemUser> getTrainItemUserList(Long trainUserId);

    /**
     * 获取用户培训项
     *
     * @param trainUserId
     * @param userId
     * @param status
     * @return {@link TrainItemUser}
     */
    TrainItemUser getTrainItemUser(Long trainUserId, Integer userId, Integer status);

    /**
     * 批量插入用户培训列表
     *
     * @param trainItemUserList
     * @return int
     */
    int insertList(List<TrainItemUser> trainItemUserList);


    /**
     * 批量更新，只更新itemOrder/deleted字段
     *
     * @param trainItemUserList
     * @return int
     */
    int updateList(List<TrainItemUser> trainItemUserList);

    /**
     * 获取课件正在进行的课件数量
     *
     * @param trainId
     * @param userId
     * @return int
     */
    int trainCourseWareGoingCount(Integer trainId, Integer userId);


    /**
     * 删除多余TrainItemUser
     *
     * @param trainUserId
     */
    void clearTrainItemUser(Long trainUserId);

}