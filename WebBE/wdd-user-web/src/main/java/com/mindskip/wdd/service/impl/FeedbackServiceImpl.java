package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.domain.Feedback;
import com.mindskip.wdd.repository.FeedbackMapper;
import com.mindskip.wdd.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @version 1.7.0
 * @description: 用户反馈
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Service
@AllArgsConstructor
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

}
