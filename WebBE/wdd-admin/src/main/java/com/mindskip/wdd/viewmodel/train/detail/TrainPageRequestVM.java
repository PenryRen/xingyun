package com.mindskip.wdd.viewmodel.train.detail;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训详情分页
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/26 10:45
 */
@Data
public class TrainPageRequestVM extends BasePage {
    @NotNull
    private Integer trainId;
    /**
     * 培训状态
     */
    private Integer status;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 部门
     */
    private List<Integer> departmentIdList;
}
