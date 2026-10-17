package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mindskip.wdd.domain.TrainArchive;

import java.util.List;


/**
 * @version 9.0.0
 * @description: 培训分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
public interface TrainArchiveService extends IService<TrainArchive> {

    /**
     * 获取最上级培训分类
     *
     * @return {@link List}<{@link TrainArchive}>
     */
    List<TrainArchive> selectRootTree();

    /**
     * 根据id获取培训分类
     *
     * @param id
     * @return {@link TrainArchive}
     */
    TrainArchive getTrainArchiveById(Integer id);

    /**
     * 根据层级获取培训分类
     *
     * @param level
     * @return {@link List}<{@link TrainArchive}>
     */
    List<TrainArchive> getTrainArchiveByLevel(String level);

}
