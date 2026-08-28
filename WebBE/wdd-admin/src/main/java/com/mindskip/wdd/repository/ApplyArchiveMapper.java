package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ApplyArchive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 报名分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper
public interface ApplyArchiveMapper extends BaseMapper<ApplyArchive> {

    /**
     * 获取根节点报名分类
     *
     * @return the root apply archive
     */
    List<ApplyArchive> getRootApplyArchive();

    /**
     * 获取报名分类，根据父节点
     *
     * @param id the id
     * @return the apply archive by parent id
     */
    List<ApplyArchive> getApplyArchiveByParentId(Integer id);

    /**
     * 更新层级
     *
     * @param regexLevel  the regex level
     * @param targetLevel the target level
     * @return the int
     */
    int updateLevel(@Param("regexLevel") String regexLevel, @Param("targetLevel") String targetLevel);

    /**
     * 获取报名分类，根据层级
     *
     * @param level the level
     * @return the by level
     */
    ApplyArchive getByLevel(String level);

    /**
     * 删除分类，根据层级
     *
     * @param level the level
     * @return the int
     */
    int deleteByLevel(String level);
}