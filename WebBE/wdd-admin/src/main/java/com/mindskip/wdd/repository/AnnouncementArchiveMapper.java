package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.AnnouncementArchive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
     * 获取第一级公告分类
     *
     * @return the root announcement archive
     */
    List<AnnouncementArchive> getRootAnnouncementArchive();

    /**
     * 根据父节点获取分类
     *
     * @param id the id
     * @return the announcement archive by parent id
     */
    List<AnnouncementArchive> getAnnouncementArchiveByParentId(Integer id);

    /**
     * 更新分类层级
     *
     * @param regexLevel  the regex level
     * @param targetLevel the target level
     * @return the int
     */
    int updateLevel(@Param("regexLevel") String regexLevel, @Param("targetLevel") String targetLevel);

    /**
     * 通过层级获取通知公告分类
     *
     * @param level the level
     * @return the by level
     */
    AnnouncementArchive getByLevel(String level);

    /**
     * 删除通知公告分类
     *
     * @param level the level
     * @return the int
     */
    int deleteByLevel(String level);
}