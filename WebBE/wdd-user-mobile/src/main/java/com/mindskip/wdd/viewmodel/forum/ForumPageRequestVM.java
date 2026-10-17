package com.mindskip.wdd.viewmodel.forum;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @version 1.9.0
 * @description: 文章分页查询
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/1 10:45
 */
@Data
public class ForumPageRequestVM extends BasePage {
    /**
     * 文章分类
     */
    private Integer forumArchiveId;

    private List<Integer> forumArchiveIdList;
}
