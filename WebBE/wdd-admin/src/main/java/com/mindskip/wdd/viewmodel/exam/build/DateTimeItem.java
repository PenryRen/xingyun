package com.mindskip.wdd.viewmodel.exam.build;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @version 1.7.0
 * @description: 时间
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
@AllArgsConstructor
public class DateTimeItem {
    public DateTimeItem() {
    }

    /**
     * 开始时间
     */
    private String limitStartTime;
    /**
     * 结束时间
     */
    private String limitEndTime;
}
