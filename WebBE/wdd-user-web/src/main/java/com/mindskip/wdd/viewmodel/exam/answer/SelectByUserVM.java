package com.mindskip.wdd.viewmodel.exam.answer;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 用户查询
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class SelectByUserVM {
    /**
     * 试卷id
     */
    private Long paperId;
    /**
     * 用户id
     */
    private Integer userId;
}
