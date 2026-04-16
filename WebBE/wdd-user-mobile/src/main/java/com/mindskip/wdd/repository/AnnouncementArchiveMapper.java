package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.AnnouncementArchive;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 通知公告分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper
public interface AnnouncementArchiveMapper extends BaseMapper<AnnouncementArchive> {

    /**
     * 查询一级节点分页
     *
     * @return the list
     */
    List<AnnouncementArchive> selectRootTree();


    /**
     * 根据层级获取公告分页
     *
     * @param level
     * @return {@link List}<{@link AnnouncementArchive}>
     */
    List<AnnouncementArchive> getAnnouncementArchiveByLevel(String level);
}