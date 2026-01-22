package com.mindskip.wdd.viewmodel.apply;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @version 6.1.0
 * @description: 报名排序
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/12 13:50
 */
@Data
public class ApplyArchiveMoveRequestVM {

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
