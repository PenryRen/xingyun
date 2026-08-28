package com.mindskip.wdd.viewmodel.monitor;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @version 6.5.0
 * @description: 答卷监考分页
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/25 10:45
 */
@Data
public class PaperMonitorPageRequestVM extends BasePage {
    /**
     * 试卷id
     */
    private Long id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 部门
     */
    private List<Integer> departmentIdList;
}
