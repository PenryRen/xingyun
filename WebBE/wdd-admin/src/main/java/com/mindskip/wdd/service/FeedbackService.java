package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Feedback;
import com.mindskip.wdd.viewmodel.user.FeedbackPageRequestVM;

/**
 * @version 9.5.0
 * @description: 意见反馈
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/10 10:45
 */
public interface FeedbackService extends IService<Feedback> {

    /**
     * 意见反馈分页
     *
     * @param requestVM
     * @return {@link PageInfo}<{@link Feedback}>
     */
    PageInfo<Feedback> page(FeedbackPageRequestVM requestVM);

}
