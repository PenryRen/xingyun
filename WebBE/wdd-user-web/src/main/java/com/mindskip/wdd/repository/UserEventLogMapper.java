package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.UserEventLog;
import com.mindskip.wdd.viewmodel.user.event.UserEventPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 用户动态
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface UserEventLogMapper extends BaseMapper<UserEventLog> {
    /**
     * 获取用户日志
     *
     * @param requestVM
     * @return
     */
    List<UserEventLog> page(UserEventPageRequestVM requestVM);
}
