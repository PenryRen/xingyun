package com.mindskip.wdd.viewmodel.menu;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

/**
 * @version 1.7.0
 * @description: 菜单分页查询
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class MenuPageRequestVM extends BasePage {
    /**
     * 菜单名称
     */
    private String name;
}
