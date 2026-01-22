package com.mindskip.wdd.base;

import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 数据权限基类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class BaseFilter {

    private Integer dataFilterUserId;
    private List<Integer> dataFilterDepartmentList;

}
