package com.mindskip.wdd.viewmodel.apply;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 用户报名过滤
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ApplyUserPageRequestVM extends BasePage {
    private Integer id;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 部门id
     */
    private List<Integer> departmentIdList;
}
