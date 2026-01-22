package com.mindskip.wdd.viewmodel.train.archive;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @version 9.0.0
 * @description: 培训分类排序
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/16 10:45
 */
@Data
public class TrainArchiveMoveRequestVM {

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
