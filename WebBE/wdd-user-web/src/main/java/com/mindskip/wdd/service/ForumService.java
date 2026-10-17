package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Forum;
import com.mindskip.wdd.domain.ForumArchive;
import com.mindskip.wdd.domain.ForumComment;
import com.mindskip.wdd.viewmodel.forum.ForumCommentPageRequestVM;
import com.mindskip.wdd.viewmodel.forum.ForumPageRequestVM;
import com.mindskip.wdd.viewmodel.user.CommentPageRequestVM;

import java.util.List;

/**
 * @version 1.9.0
 * @description: 文章
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/1 10:45
 */
public interface ForumService extends IService<Forum> {

    /**
     * 插入文章
     *
     * @param forum
     * @return
     */
    int insertForum(Forum forum);

    /**
     * 获取文章分类
     *
     * @param id
     * @return
     */
    ForumArchive getForumArchiveById(Integer id);

    /**
     * 批量获取文章分类
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
     * 获取文章列表，根据id
     *
     * @param idList
     * @return {@link List}<{@link Forum}>
     */
    List<Forum> getForumByIdList(List<Integer> idList);

    /**
     * 文章分页
     *
     * @param requestVM the request vm
     * @return the page info
     */
    PageInfo<Forum> page(ForumPageRequestVM requestVM);


    /**
     * 根据id获取所有评论
     *
     * @param idList
     * @return
     */
    List<ForumComment> getCommentByIdList(List<Long> idList);

    /**
     * 评论分页
     *
     * @param requestVM
     * @return
     */
    PageInfo<ForumComment> commentPage(ForumCommentPageRequestVM requestVM);


    /**
     * 用户发布的评论
     *
     * @param requestVM
     * @return
     */
    PageInfo<ForumComment> userCommentPage(CommentPageRequestVM requestVM);


    /**
     * 根据id获取文章评论
     *
     * @param id
     * @return
     */
    ForumComment getForumCommentById(Long id);

    /**
     * 获取子评论
     *
     * @param forumId
     * @param idLevel
     * @return
     */
    List<ForumComment> getForumCommentByLevel(Integer forumId, List<String> idLevel);

    /**
     * 插入评论
     *
     * @param forumComment
     * @return
     */
    int insertForumComment(ForumComment forumComment);

    /**
     * 更新评论
     *
     * @param forumComment
     * @return
     */
    int updateForumComment(ForumComment forumComment);
}
