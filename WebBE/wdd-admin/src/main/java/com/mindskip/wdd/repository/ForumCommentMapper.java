package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ForumComment;
import com.mindskip.wdd.viewmodel.forum.comment.ForumCommentPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

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
     * 评论分页
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<ForumComment> page(ForumCommentPageRequestVM requestVM);
}