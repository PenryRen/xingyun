package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.UserEventLog;
import com.mindskip.wdd.repository.UserEventLogMapper;
import com.mindskip.wdd.service.UserEventLogService;
import com.mindskip.wdd.viewmodel.user.event.UserEventPageRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @version 1.7.0
 * @description: 用户动态
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@AllArgsConstructor
public class UserEventLogServiceImpl extends ServiceImpl<UserEventLogMapper, UserEventLog> implements UserEventLogService {

    private final UserEventLogMapper userEventLogMapper;


    @Override
    public PageInfo<UserEventLog> page(UserEventPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                userEventLogMapper.page(requestVM)
        );
    }
}
