package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ApplyArchive;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 报名分类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/16 10:45
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
     * 根据层级获取报名分类
     *
     * @param level
     * @return {@link List}<{@link ApplyArchive}>
     */
    List<ApplyArchive> getApplyArchiveByLevel(String level);
}