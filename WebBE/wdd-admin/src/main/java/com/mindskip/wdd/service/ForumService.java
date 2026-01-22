package com.mindskip.wdd.service;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Forum;
import com.mindskip.wdd.domain.ForumComment;
import com.mindskip.wdd.viewmodel.forum.ForumPageRequestVM;
import com.mindskip.wdd.viewmodel.forum.comment.ForumCommentPageRequestVM;


/**
 * @version 1.9.0
 * @description: 文章
 * Copyright (C), 2024, 麒技团队
 * @date 2024/7/1 10:45
 */
public interface ForumService {

    /**
     * 文章部门分页查询
     *
     * @param requestVM
     * @return
     */
    PageInfo<Forum> forumPage(ForumPageRequestVM requestVM);

    /**
     * 查询文章
     *
     * @param id
     * @return
     */
    Forum selectForum(Integer id);

    /**
     * 插入文章
     *
     * @param forum
     * @return
     */
    int insertForum(Forum forum);

    /**
     * 更新文章
     *
     * @param forum
     * @return
     */
    int updateForum(Forum forum);


    /**
     * 文章评论分页
     *
     * @param requestVM
     * @return
     */
    PageInfo<ForumComment> forumCommentPage(ForumCommentPageRequestVM requestVM);

    /**
     * 查询文章评论
     *
     * @param id
     * @return
     */
    ForumComment selectForumComment(Long id);

    /**
     * 更新文章评论
     *
     * @param forumComment
     * @return
     */
    int updateForumComment(ForumComment forumComment);

}
