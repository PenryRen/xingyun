package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.Feedback;
import com.mindskip.wdd.viewmodel.user.FeedbackPageRequestVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.5.0
 * @description: 意见反馈
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/10 10:45
 */
@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {

    /**
     * 意见反馈分页
     *
     * @param requestVM
     * @return {@link List}<{@link Feedback}>
     */
    List<Feedback> page(FeedbackPageRequestVM requestVM);

}
