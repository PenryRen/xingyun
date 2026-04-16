package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ApplyAudit;
import com.mindskip.wdd.viewmodel.apply.ApplyUserPageRequestVM;
import com.mindskip.wdd.viewmodel.apply.ApplyUserPageResponseVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 9.5.0
 * @description: 报名申请
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/28 10:45
 */
@Mapper
public interface ApplyAuditMapper extends BaseMapper<ApplyAudit> {

    /**
     * 报名申请人员列表
     *
     * @param applyUserPageRequestVM
     * @return {@link List}<{@link ApplyUserPageResponseVM}>
     */
    List<ApplyUserPageResponseVM> userAuditPage(ApplyUserPageRequestVM applyUserPageRequestVM);
}
