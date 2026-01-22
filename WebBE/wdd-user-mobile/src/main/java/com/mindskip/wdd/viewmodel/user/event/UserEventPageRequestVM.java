package com.mindskip.wdd.viewmodel.user.event;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

/**
 * @version 4.4.0
 * @description: 用户动态
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/28 10:45
 */
@Data
public class UserEventPageRequestVM extends BasePage {
    /**
     * 用户id
     */
    private Integer userId;
}
