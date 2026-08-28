package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ApplyArchive;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 报名分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Mapper
public interface ApplyArchiveMapper extends BaseMapper<ApplyArchive> {

    /**
     * 查询一级节点分页
     *
     * @return the list
     */
    List<ApplyArchive> selectRootTree();

    /**
     * 根据父节点获取分类
     *
     * @param parentId the parent id
     * @return the by parent id
     */
    List<ApplyArchive> getByParentId(Integer parentId);

    /**
     * 根据层级获取报名分类
     *
     * @param level
     * @return {@link List}<{@link ApplyArchive}>
     */
    List<ApplyArchive> getApplyArchiveByLevel(String level);
}