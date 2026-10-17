package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.UserEventLog;
import com.mindskip.wdd.viewmodel.userEventLog.UserEventLogPageRequestVM;
import com.mindskip.wdd.viewmodel.userEventLog.UserEventLogSelectRequestVM;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 用户动态日志
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public interface UserEventLogService extends IService<UserEventLog> {

    /**
     * 用户动态日志分页
     *
     * @param requestVM the request vm
     * @return the page info
     */
    PageInfo<UserEventLog> page(UserEventLogPageRequestVM requestVM);

    /**
     * 查询用户动态日志
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<UserEventLog> selectLogById(UserEventLogSelectRequestVM requestVM);

}
