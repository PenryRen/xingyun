package com.mindskip.wdd.domain.other;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.7.0
 * @description: 试卷缓存信息
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class PaperSession implements Serializable {


    private static final long serialVersionUID = 1505157767651525925L;

    /**
     * 考试总时长
     */
    private Integer totalTime;

    /**
     * 剩余时长
     */
    private Integer remainTime;

    /**
     * 作弊次数
     */
    private Integer cheatCount;

    /**
     * 考试开始时间
     */
    private Date startTime;
}
