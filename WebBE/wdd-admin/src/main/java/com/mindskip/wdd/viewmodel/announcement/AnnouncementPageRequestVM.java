package com.mindskip.wdd.viewmodel.announcement;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

/**
 * @version 1.7.0
 * @description: 公告过滤
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class AnnouncementPageRequestVM extends BasePage {
    /**
     * 公告标题
     */
    private String title;
}
