package com.mindskip.wdd.viewmodel.apply;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 报名分页
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class ApplyPageRequestVM extends BasePage {
    /**
     * 部门
     */
    private Integer departmentId;
    /**
     * 当前试卷
     */
    private Date now;
    /**
     * 用户id
     */
    private Integer userId;

    private Integer applyArchiveId;

    private List<Integer> applyArchiveIdList;
}
