package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ForumComment;
import com.mindskip.wdd.viewmodel.forum.ForumCommentPageRequestVM;
import com.mindskip.wdd.viewmodel.user.CommentPageRequestVM;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.9.0
 * @description: 文章评论
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/1 10:45
 */
@Mapper
public interface ForumCommentMapper extends BaseMapper<ForumComment> {

    /**
     * 根据id获取评论
     *
     * @param idList
     * @return
     */
    List<ForumComment> getCommentByIdList(List<Long> idList);

    /**
     * 文章评论分页
     *
     * @param requestVM
     * @return
     */
    List<ForumComment> page(ForumCommentPageRequestVM requestVM);


    /**
     * 用户发表的评论
     *
     * @param requestVM
     * @return
     */
    List<ForumComment> userCommentPage(CommentPageRequestVM requestVM);


    /**
     * 获取子评论
     *
     * @param forumId
     * @param idLevel
     * @return
     */
    List<ForumComment> getForumCommentByLevel(@Param("forumId") Integer forumId, @Param("idLevel") List<String> idLevel);
}