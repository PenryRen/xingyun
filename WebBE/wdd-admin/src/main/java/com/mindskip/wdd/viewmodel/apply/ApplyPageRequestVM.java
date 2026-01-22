package com.mindskip.wdd.viewmodel.apply;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 报名分页过滤
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class ApplyPageRequestVM extends BasePage {
    /**
     * 报名名称
     */
    private String name;
    /**
     * 报名分页
     */
    private List<Integer> applyArchiveIdList;
    /**
     * 报名id
     */
    private Integer selectId;
    /**
     * 当前时间
     */
    private Date now;
}
