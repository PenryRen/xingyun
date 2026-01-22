package com.mindskip.wdd.viewmodel.user;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 用户分页
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class UserPageRequestVM extends BasePage {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 部门列表
     */
    private List<Integer> departmentIdList;
    /**
     * 排除部门
     */
    private List<Integer> excludeDepartmentIdList;
    /**
     * 已选部门
     */
    private List<Integer> selectIdList;
    /**
     * 状态
     */
    private Integer status;
}
