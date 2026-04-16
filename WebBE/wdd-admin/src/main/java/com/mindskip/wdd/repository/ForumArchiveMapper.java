package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ForumArchive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.9.0
 * @description: 文章分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/1 10:45
 */
@Mapper
public interface ForumArchiveMapper extends BaseMapper<ForumArchive> {

    /**
     * 获取第一级文章分类
     *
     * @return the root forum archive
     */
    List<ForumArchive> getRootForumArchive();

    /**
     * 根据父节点获取分类
     *
     * @param id the id
     * @return the forum archive by parent id
     */
    List<ForumArchive> getForumArchiveByParentId(Integer id);

    /**
     * 更新分类层级
     *
     * @param regexLevel  the regex level
     * @param targetLevel the target level
     * @return the int
     */
    int updateLevel(@Param("regexLevel") String regexLevel, @Param("targetLevel") String targetLevel);

    /**
     * 通过层级获取通知文章分类
     *
     * @param level the level
     * @return the by level
     */
    ForumArchive getByLevel(String level);

    /**
     * 删除通知文章分类
     *
     * @param level the level
     * @return the int
     */
    int deleteByLevel(String level);
}