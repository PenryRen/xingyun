package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.UserEventLog;
import com.mindskip.wdd.viewmodel.userEventLog.UserEventLogPageRequestVM;
import com.mindskip.wdd.viewmodel.userEventLog.UserEventLogSelectRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 用户动态日志
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper
public interface UserEventLogMapper extends BaseMapper<UserEventLog> {

    /**
     * 日志分页
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<UserEventLog> page(UserEventLogPageRequestVM requestVM);

    /**
     * 根据id查询日志
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<UserEventLog> selectLogById(UserEventLogSelectRequestVM requestVM);
}
