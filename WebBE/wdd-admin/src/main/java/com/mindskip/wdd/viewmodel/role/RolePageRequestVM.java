package com.mindskip.wdd.viewmodel.role;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

/**
 * @version 1.7.0
 * @description: 角色分页
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class RolePageRequestVM extends BasePage {
    /**
     * 角色名称
     */
    private String name;
}
