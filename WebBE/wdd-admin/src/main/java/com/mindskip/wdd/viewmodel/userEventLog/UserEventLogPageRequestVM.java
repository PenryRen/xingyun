package com.mindskip.wdd.viewmodel.userEventLog;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 用户动态分页
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class UserEventLogPageRequestVM extends BasePage {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 部门
     */
    private List<Integer> departmentIdList;

}
