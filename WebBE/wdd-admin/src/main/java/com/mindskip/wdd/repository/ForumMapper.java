package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.Forum;
import com.mindskip.wdd.viewmodel.forum.ForumPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.9.0
 * @description: 文章
 * Copyright (C), 2024, 麒技团队
 * @date 2024/7/1 10:45
 */
@Mapper
public interface ForumMapper extends BaseMapper<Forum> {
    /**
     * 文章分页
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<Forum> page(ForumPageRequestVM requestVM);
}