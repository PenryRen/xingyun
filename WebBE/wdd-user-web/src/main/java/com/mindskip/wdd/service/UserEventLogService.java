package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.UserEventLog;
import com.mindskip.wdd.viewmodel.user.event.UserEventPageRequestVM;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 用户动态
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface UserEventLogService extends IService<UserEventLog> {

    /**
     * 获取用户动态
     *
     * @return the user event log top
     */
    PageInfo<UserEventLog> page(UserEventPageRequestVM requestVM);

}
