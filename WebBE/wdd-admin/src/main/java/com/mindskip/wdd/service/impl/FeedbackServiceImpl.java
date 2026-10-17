package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Feedback;
import com.mindskip.wdd.repository.FeedbackMapper;
import com.mindskip.wdd.service.FeedbackService;
import com.mindskip.wdd.viewmodel.user.FeedbackPageRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @version 9.5.0
 * @description: 意见反馈
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/10 10:45
 */
@Service
@AllArgsConstructor
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

    private final FeedbackMapper feedbackMapper;

    @Override
    public PageInfo<Feedback> page(FeedbackPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                feedbackMapper.page(requestVM)
        );
    }

}
