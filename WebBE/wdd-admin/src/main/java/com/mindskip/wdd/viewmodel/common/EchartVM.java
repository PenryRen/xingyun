package com.mindskip.wdd.viewmodel.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 图表
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class EchartVM {
    public EchartVM() {
        x = new ArrayList<>(32);
        y = new ArrayList<>(32);
    }

    /**
     * 图表标题
     */
    private String title;
    /**
     * x轴数组
     */
    private List<Integer> y;
    /**
     * y轴数组
     */
    private List<Integer> x;
}
