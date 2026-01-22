package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Train;
import com.mindskip.wdd.domain.TrainItem;
import com.mindskip.wdd.domain.TrainItemUser;
import com.mindskip.wdd.domain.TrainUser;
import com.mindskip.wdd.viewmodel.train.TrainPageRequestVM;
import com.mindskip.wdd.viewmodel.user.train.UserTrainPageRequestVM;
import com.mindskip.wdd.viewmodel.user.train.UserTrainPageResponseVM;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训中心
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/16 10:45
 */
public interface TrainService extends IService<Train> {

    /**
     * 培训分页
     *
     * @param requestVM
     * @return {@link PageInfo}<{@link Train}>
     */
    PageInfo<Train> page(TrainPageRequestVM requestVM);

    /**
     * 获取培训列表
     *
     * @param id
     * @return {@link List}<{@link TrainItem}>
     */
    List<TrainItem> getTrainItemList(Integer id);

    /**
     * 获取用户培训
     *
     * @param trainId
     * @param userId
     * @return {@link TrainUser}
     */
    TrainUser selectTrainUser(Integer trainId, Integer userId);

    /**
     * 获取用户培训列表
     *
     * @param trainUserId
     * @return {@link List}<{@link TrainItemUser}>
     */
    List<TrainItemUser> getTrainItemUserList(Long trainUserId);

    /**
     * 获取单个用户培训项
     *
     * @param trainUserId
     * @param userId
     * @param status
     * @return {@link TrainItemUser}
     */
    TrainItemUser getTrainItemUser(Long trainUserId, Integer userId, Integer status);

    /**
     * 获取正在进行的培训课件数量
     *
     * @param trainId
     * @param userId
     * @return int
     */
    int trainCourseWareGoingCount(Integer trainId, Integer userId);

    /**
     * 更新用户培训项
     *
     * @param trainItemUser
     * @return int
     */
    int updateTrainItemUser(TrainItemUser trainItemUser);

    /**
     * 插入用户培训
     *
     * @param trainUser
     * @return int
     */
    int insertTrainUser(TrainUser trainUser);

    /**
     * 批量插入用户培训列表
     *
     * @param trainItemUserList
     * @return int
     */
    int insertTrainItemUserList(List<TrainItemUser> trainItemUserList);


    /**
     * 批量更新，只更新itemOrder/deleted字段
     *
     * @param trainItemUserList
     * @return int
     */
    int updateTrainItemUserList(List<TrainItemUser> trainItemUserList);

    /**
     * 培训试卷完成更新
     *
     * @param trainItemUser
     */
    void paperTrainComplete(TrainItemUser trainItemUser);

    /**
     * 培训完成更新
     *
     * @param trainItemUser
     */
    void trainComplete(TrainItemUser trainItemUser);

    /**
     * 用户培训记录分页
     *
     * @param requestVM
     * @return {@link PageInfo}<{@link UserTrainPageResponseVM}>
     */
    PageInfo<UserTrainPageResponseVM> userTrainPage(UserTrainPageRequestVM requestVM);


    /**
     * 删除多余TrainItemUser
     *
     * @param trainUserId
     */
    void clearTrainItemUser(Long trainUserId);
}
