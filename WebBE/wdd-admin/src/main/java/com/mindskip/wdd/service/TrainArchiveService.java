package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.TrainArchive;
import com.mindskip.wdd.viewmodel.train.archive.TrainArchiveMoveRequestVM;

import java.util.List;


/**
 * @version 9.0.0
 * @description: 培训分类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/26 10:45
 */
public interface TrainArchiveService extends IService<TrainArchive> {

    /**
     * 获取一级节点分类分类
     *
     * @return
     */
    List<TrainArchive> getRootTrainArchive();

    /**
     * 根据父节点id,获取分类分类
     *
     * @param id
     * @return
     */
    List<TrainArchive> getTrainArchiveByParentId(Integer id);

    /**
     * 更新分类分类层级
     *
     * @param originalLevel
     * @param targetLevel
     * @return
     */
    int updateLevel(String originalLevel, String targetLevel);

    /**
     * 根据层级获取分类分类
     *
     * @param level
     * @return
     */
    TrainArchive getByLevel(String level);

    /**
     * 根据层级删除分类分类
     *
     * @param level
     * @return
     */
    int deleteByLevel(String level);

    /**
     * 培训分类位置移动
     *
     * @param courseWareArchiveMoveRequestVM
     * @return {@link RestResponse}
     */
    RestResponse move(TrainArchiveMoveRequestVM courseWareArchiveMoveRequestVM);
}
