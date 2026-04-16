package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ApplyAudit;
import org.apache.ibatis.annotations.Mapper;

/**
 * @version 9.5.0
 * @description: 报名申请
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/28 10:45
 */
@Mapper
public interface ApplyAuditMapper extends BaseMapper<ApplyAudit> {
    /**
     * 获取用户报名申请
     *
     * @param applyId
     * @param userId
     * @return {@link ApplyAudit}
     */
    ApplyAudit getApplyAuditByUser(Integer applyId, Integer userId);
}
