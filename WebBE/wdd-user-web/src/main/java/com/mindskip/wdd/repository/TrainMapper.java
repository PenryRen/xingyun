package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.Train;
import com.mindskip.wdd.viewmodel.train.TrainPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Mapper
public interface TrainMapper extends BaseMapper<Train> {
    /**
     * 培训分页
     *
     * @param requestVM
     * @return {@link List}<{@link Train}>
     */
    List<Train> page(TrainPageRequestVM requestVM);
}