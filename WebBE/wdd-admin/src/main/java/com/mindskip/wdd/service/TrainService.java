package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Train;
import com.mindskip.wdd.domain.TrainDepartment;
import com.mindskip.wdd.domain.TrainItem;
import com.mindskip.wdd.domain.TrainItemUser;
import com.mindskip.wdd.viewmodel.train.course.TrainCoursePageRequestVM;
import com.mindskip.wdd.viewmodel.train.course.TrainCourseWarePageResponseVM;
import com.mindskip.wdd.viewmodel.train.detail.TrainPageRequestVM;
import com.mindskip.wdd.viewmodel.train.detail.TrainPageResponseVM;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 课程
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface TrainService extends IService<Train> {
    /**
     * 课程分页
     *
     * @param requestVM
     * @return
     */
    PageInfo<Train> page(TrainCoursePageRequestVM requestVM);

    /**
     * 获取培训列表
     *
     * @param trainId
     * @return {@link List}<{@link TrainItem}>
     */
    List<TrainItem> getTrainItemList(Integer trainId);

    /**
     * 删除培训列表
     *
     * @param trainId
     */
    void clearTrainItem(Integer trainId);

    /**
     * 插入培训列表
     *
     * @param trainItem
     * @return int
     */
    int insertTrainItem(TrainItem trainItem);

    /**
     * 更新培训列表
     *
     * @param trainItem
     * @return int
     */
    int updateTrainItem(TrainItem trainItem);

    /**
     * 获取培训部门
     *
     * @param trainId
     * @return {@link List}<{@link TrainDepartment}>
     */
    List<TrainDepartment> getTrainDepartmentList(Integer trainId);

    /**
     * 删除培训部门
     *
     * @param trainId
     */
    void clearTrainDepartment(Integer trainId);

    /**
     * 插入培训部门
     *
     * @param trainDepartment
     * @return int
     */
    int insertTrainDepartment(TrainDepartment trainDepartment);

    /**
     * 获取用户培训数量
     *
     * @param trainId
     * @param status
     * @return int
     */
    int trainUserStatusCount(Integer trainId, Integer status);

    /**
     * 获取用户培训列表数量
     *
     * @param trainId
     * @param targetType
     * @param status
     * @return int
     */
    int trainItemUserStatusCount(Integer trainId, Integer targetType, Integer status);


    /**
     * 用户培训分页
     *
     * @param requestVM
     * @return {@link PageInfo}<{@link TrainPageResponseVM}>
     */
    PageInfo<TrainPageResponseVM> userPage(TrainPageRequestVM requestVM);

    /**
     * 用户课件分页
     *
     * @param requestVM
     * @return {@link PageInfo}<{@link TrainCourseWarePageResponseVM}>
     */
    PageInfo<TrainCourseWarePageResponseVM> userCourseWarePage(TrainPageRequestVM requestVM);


    /**
     * 获取用户培训列表
     *
     * @param id
     * @return {@link TrainItemUser}
     */
    TrainItemUser trainItemUserById(Long id);

    /**
     * 培训试卷批改完成，修改培训状态
     *
     * @param trainItemUser
     */
    void paperTrainComplete(TrainItemUser trainItemUser);
}
