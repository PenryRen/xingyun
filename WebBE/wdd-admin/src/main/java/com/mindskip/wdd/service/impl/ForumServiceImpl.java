package com.mindskip.wdd.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Forum;
import com.mindskip.wdd.domain.ForumComment;
import com.mindskip.wdd.repository.ForumCommentMapper;
import com.mindskip.wdd.repository.ForumMapper;
import com.mindskip.wdd.service.ForumService;
import com.mindskip.wdd.viewmodel.forum.ForumPageRequestVM;
import com.mindskip.wdd.viewmodel.forum.comment.ForumCommentPageRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @version 1.9.0
 * @description: 文章
 * Copyright (C), 2024, 麒技团队
 * @date 2024/7/1 10:45
 */
@Service
@AllArgsConstructor
public class ForumServiceImpl implements ForumService {

    private final ForumMapper forumMapper;
    private final ForumCommentMapper forumCommentMapper;


    @Override
    public PageInfo<Forum> forumPage(ForumPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                forumMapper.page(requestVM)
        );
    }

    @Override
    public Forum selectForum(Integer id) {
        return forumMapper.selectById(id);
    }

    @Override
    public int insertForum(Forum forum) {
        return forumMapper.insert(forum);
    }

    @Override
    public int updateForum(Forum forum) {
        return forumMapper.updateById(forum);
    }

    @Override
    public PageInfo<ForumComment> forumCommentPage(ForumCommentPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                forumCommentMapper.page(requestVM)
        );
    }

    @Override
    public ForumComment selectForumComment(Long id) {
        return forumCommentMapper.selectById(id);
    }

    @Override
    public int updateForumComment(ForumComment forumComment) {
        return forumCommentMapper.updateById(forumComment);
    }


}
