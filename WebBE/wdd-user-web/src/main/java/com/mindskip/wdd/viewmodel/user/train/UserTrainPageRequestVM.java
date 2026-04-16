package com.mindskip.wdd.viewmodel.user.train;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

/**
 * @version 9.0.0
 * @description: 培训记录分页
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Data
public class UserTrainPageRequestVM extends BasePage {
    /**
     * 用户id
     */
    private Integer userId;
}
