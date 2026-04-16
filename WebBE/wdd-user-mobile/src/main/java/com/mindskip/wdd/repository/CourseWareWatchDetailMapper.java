package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.CourseWareWatchDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;

/**
 * @version 5.9.0
 * @description: 课件观看详情
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/3 11:45
 */
@Mapper
public interface CourseWareWatchDetailMapper extends BaseMapper<CourseWareWatchDetail> {
    /**
     * 用户观看时长统计
     *
     * @param courseWareId
     * @param userId
     * @param startTime
     * @return {@link Integer}
     */
    Integer watchUserSum(Integer courseWareId, Integer userId, Date startTime);

    /**
     * 获取最新的课件观看详情
     *
     * @param userId
     * @param courseWareId
     * @return {@link CourseWareWatchDetail}
     */
    CourseWareWatchDetail getLastItem(Integer userId, Integer courseWareId);
}