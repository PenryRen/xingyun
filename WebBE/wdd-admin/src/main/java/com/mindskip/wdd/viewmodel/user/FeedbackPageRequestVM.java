package com.mindskip.wdd.viewmodel.user;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @version 9.5.0
 * @description: 意见反馈分页请求
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/10 10:45
 */
@Data
public class FeedbackPageRequestVM extends BasePage {
    /**
     * 部门
     */
    private List<Integer> departmentIdList;

}
