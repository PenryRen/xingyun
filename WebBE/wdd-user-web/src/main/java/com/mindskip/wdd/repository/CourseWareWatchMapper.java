package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.CourseWareWatch;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version 1.7.0
 * @description: 课件观看记录
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface CourseWareWatchMapper extends BaseMapper<CourseWareWatch> {
    /**
     * 获取课件观看
     *
     * @param userId
     * @param courseWareId
     * @return {@link CourseWareWatch}
     */
    CourseWareWatch getCourseWareWatch(Integer userId, Integer courseWareId);
}