package com.mindskip.wdd.viewmodel.user.train;

import lombok.Data;

import java.util.Date;

/**
 * @version 9.0.0
 * @description: 培训记录返回
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Data
public class UserTrainPageResponseVM {
    private Long id;
    /**
     * 培训id
     */
    private Integer trainId;
    /**
     * 培训名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 封面
     */
    private String coverPath;
    /**
     * 创建时间
     */
    private Date createTime;
    private String createTimeStr;
    /**
     * 创建者
     */
    private Integer createUser;
    /**
     * 状态
     */
    private Integer status;
    private String statusStr;
}
