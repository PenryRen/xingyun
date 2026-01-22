package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.AnnouncementArchive;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 通知公告分类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
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
     * 根据父节点获取分类
     *
     * @param parentId the parent id
     * @return the by parent id
     */
    List<AnnouncementArchive> getByParentId(Integer parentId);
}