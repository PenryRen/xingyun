package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.TrainArchive;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Mapper
public interface TrainArchiveMapper extends BaseMapper<TrainArchive> {
    /**
     * 查询一级节点分页
     *
     * @return the list
     */
    List<TrainArchive> selectRootTree();

    /**
     * 根据父节点获取分类
     *
     * @param parentId the parent id
     * @return the by parent id
     */
    List<TrainArchive> getByParentId(Integer parentId);


    /**
     * 获取培训分类，根据层级
     *
     * @param level
     * @return {@link List}<{@link TrainArchive}>
     */
    List<TrainArchive> getTrainArchiveByLevel(String level);
}