package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.PracticeBuildDepartment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version 9.5.0
 * @description: 模拟练习发布部门
 * Copyright (C), 2024, 麒技团队
 * @date 2024/09/11 10:45
 */
@Mapper
public interface PracticeBuildDepartmentMapper extends BaseMapper<PracticeBuildDepartment> {

    /**
     * 根据练习构建id，清除练习部门
     *
     * @param practiceBuildId
     */
    void clearDepartment(Long practiceBuildId);
}
