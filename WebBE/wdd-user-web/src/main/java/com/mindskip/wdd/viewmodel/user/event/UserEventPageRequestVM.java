package com.mindskip.wdd.viewmodel.user.event;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

/**
 * @version 4.5.0
 * @description: 用户动态
 * Copyright (C), 2025, 麟航团队
 * @date 2025/11/10 10:45
 */
@Data
public class UserEventPageRequestVM extends BasePage {
    /**
     * 用户id
     */
    private Integer userId;
}
