package com.mindskip.wdd.viewmodel.dashboard;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 首页数据统计
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class DashboardVM {

    /**
     * 试卷月提交量
     */
    private EchartVM paperEchartVM;

    /**
     * 用户月注册量
     */
    private EchartVM userEchartVM;
}
