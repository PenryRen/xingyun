package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Forum;
import com.mindskip.wdd.domain.ForumArchive;
import com.mindskip.wdd.domain.ForumComment;
import com.mindskip.wdd.repository.ForumArchiveMapper;
import com.mindskip.wdd.repository.ForumCommentMapper;
import com.mindskip.wdd.repository.ForumMapper;
import com.mindskip.wdd.service.ForumService;
import com.mindskip.wdd.service.SystemService;
import com.mindskip.wdd.viewmodel.forum.ForumCommentPageRequestVM;
import com.mindskip.wdd.viewmodel.forum.ForumPageRequestVM;
import com.mindskip.wdd.viewmodel.user.CommentPageRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.9.0
 * @description: 文章
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/1 10:45
 */
@Service
@AllArgsConstructor
public class ForumServiceImpl extends ServiceImpl<ForumMapper, Forum> implements ForumService {


    private final ForumMapper forumMapper;
    private final ForumArchiveMapper forumArchiveMapper;
    private final ForumCommentMapper forumCommentMapper;
    private final SystemService systemService;


    @Override
    public int insertForum(Forum forum) {
        forum.setTitle(systemService.clearStopWord(forum.getTitle()));
        forum.setContent(systemService.clearStopWord(forum.getContent()));
        return forumMapper.insert(forum);
    }

    @Override
    public ForumArchive getForumArchiveById(Integer id) {
        return forumArchiveMapper.selectById(id);
    }

    @Override
    public List<ForumArchive> getForumArchiveByLevel(String level) {
        return forumArchiveMapper.getForumArchiveByLevel(level);
    }


    @Override
    public List<ForumArchive> selectRootTree() {
        return forumArchiveMapper.selectRootTree();
    }

    @Override
    public List<ForumArchive> getByParentId(Integer parentId) {
        return forumArchiveMapper.getByParentId(parentId);
    }

    @Override
    public List<Forum> getForumByIdList(List<Integer> idList) {
        return forumMapper.getForumByIdList(idList);
    }

    @Override
    public PageInfo<Forum> page(ForumPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                forumMapper.page(requestVM)
        );
    }

    @Override
    public List<ForumComment> getCommentByIdList(List<Long> idList) {
        return forumCommentMapper.getCommentByIdList(idList);
    }


    @Override
    public PageInfo<ForumComment> commentPage(ForumCommentPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                forumCommentMapper.page(requestVM)
        );
    }

    @Override
    public PageInfo<ForumComment> userCommentPage(CommentPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                forumCommentMapper.userCommentPage(requestVM)
        );
    }

    @Override
    public ForumComment getForumCommentById(Long id) {
        return forumCommentMapper.selectById(id);
    }

    @Override
    public List<ForumComment> getForumCommentByLevel(Integer forumId, List<String> idLevel) {
        return forumCommentMapper.getForumCommentByLevel(forumId, idLevel);
    }

    @Override
    public int insertForumComment(ForumComment forumComment) {
        forumComment.setContent(systemService.clearStopWord(forumComment.getContent()));
        return forumCommentMapper.insert(forumComment);
    }

    @Override
    public int updateForumComment(ForumComment forumComment) {
        return forumCommentMapper.updateById(forumComment);
    }


}
