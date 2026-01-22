package com.mindskip.wdd.viewmodel.common;

import lombok.Data;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 图表轴配置
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/26 10:45
 */
@Data
public class SeriesItem {
    /**
     * 轴值
     */
    private Object value;
    /**
     * 轴样式配置
     */
    private ItemStyle itemStyle;

    public SeriesItem(Object value, String color) {
        this.value = value;
        this.itemStyle = new ItemStyle();
        this.itemStyle.setColor(color);
    }
}
