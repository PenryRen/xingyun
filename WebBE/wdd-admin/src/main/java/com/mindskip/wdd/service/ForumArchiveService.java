package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.ForumArchive;
import com.mindskip.wdd.viewmodel.forum.ForumArchiveMoveRequestVM;

import java.util.List;


/**
 * @version 1.9.0
 * @description: 文章分类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/7/1 10:45
 */
public interface ForumArchiveService extends IService<ForumArchive> {

    /**
     * 获取文章分类根节点
     *
     * @return
     */
    List<ForumArchive> getRootForumArchive();

    /**
     * 通过id 获取文章分类
     *
     * @param id
     * @return
     */
    List<ForumArchive> getForumArchiveByParentId(Integer id);

    /**
     * 更新文章分类层级
     *
     * @param originalLevel 原始层级
     * @param targetLevel   目标层级
     * @return
     */
    int updateLevel(String originalLevel, String targetLevel);

    /**
     * 根据层级获取文章
     *
     * @param level
     * @return
     */
    ForumArchive getByLevel(String level);

    /**
     * 根据层级删除文章
     *
     * @param level
     * @return
     */
    int deleteByLevel(String level);

    /**
     * 文章分类移动
     *
     * @param forumArchiveMoveRequestVM
     * @return {@link RestResponse}
     */
    RestResponse move(ForumArchiveMoveRequestVM forumArchiveMoveRequestVM);
}
