package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ForumArchive;
import org.apache.ibatis.annotations.Mapper;

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
     * 批量获取分类
     *
     * @param idList
     * @return
     */
    List<ForumArchive> getForumArchiveList(List<Integer> idList);


    /**
     * 查询一级节点分页
     *
     * @return the list
     */
    List<ForumArchive> selectRootTree();

    /**
     * 根据父节点获取文章分类
     *
     * @param parentId the parent id
     * @return the by parent id
     */
    List<ForumArchive> getByParentId(Integer parentId);


    /**
     * 根据层级获取文章分类
     *
     * @param level
     * @return {@link List}<{@link ForumArchive}>
     */
    List<ForumArchive> getForumArchiveByLevel(String level);
}