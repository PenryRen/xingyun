package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.AnnouncementDepartment;
import com.mindskip.wdd.domain.other.KeyValue;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 通知公告发布部门
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface AnnouncementDepartmentMapper extends BaseMapper<AnnouncementDepartment> {

    /**
     * 删除公告部门
     *
     * @param announcementId the announcement id
     * @return the int
     */
    int updateDeleteByAnnouncementId(Integer announcementId);

    /**
     * 获取通知公告部门
     *
     * @param announcementId the announcement id
     * @return the department by announcement id
     */
    List<KeyValue> getDepartmentByAnnouncementId(Integer announcementId);
}