package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.TrainDepartment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训部门
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Mapper
public interface TrainDepartmentMapper extends BaseMapper<TrainDepartment> {

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
}