package com.mindskip.wdd.viewmodel.announcement;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @version 6.1.0
 * @description: 通知公告排序
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/12 13:50
 */
@Data
public class AnnouncementArchiveMoveRequestVM {

    /**
     * 拖动节点
     */
    @NotNull
    private Integer draggingNodeId;

    /**
     * 目标节点
     */
    @NotNull
    private Integer dropNodeId;

    /**
     * 拖动类型
     */
    @NotBlank
    private String dropType;
}
