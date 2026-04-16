package com.mindskip.wdd.viewmodel.role;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 角色列表返回
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class RoleListResponseVM {
    private Integer id;
    /**
     * 角色名称
     */
    private String name;
}
