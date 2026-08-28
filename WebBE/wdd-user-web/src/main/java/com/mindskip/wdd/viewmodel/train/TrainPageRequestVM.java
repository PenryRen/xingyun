package com.mindskip.wdd.viewmodel.train;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训分页
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Data
public class TrainPageRequestVM extends BasePage {
    /**
     * 培训分类
     */
    private Integer trainArchiveId;

    /**
     * 培训分类列表
     */
    private List<Integer> trainArchiveIdList;

    /**
     * 部门id
     */
    private Integer departmentId;

    /**
     * 当前时间
     */
    private Date now;
}
