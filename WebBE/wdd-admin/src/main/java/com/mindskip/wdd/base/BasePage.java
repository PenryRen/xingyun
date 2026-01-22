package com.mindskip.wdd.base;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 分页基类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class BasePage extends BaseFilter {
    private Integer pageIndex;

    private Integer pageSize;

}
