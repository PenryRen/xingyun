package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.TrainItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训列表
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/16 10:45
 */
@Mapper
public interface TrainItemMapper extends BaseMapper<TrainItem> {
    /**
     * 获取培训项目列表
     *
     * @param trainId
     * @return {@link List}<{@link TrainItem}>
     */
    List<TrainItem> getTrainItemList(Integer trainId);
}