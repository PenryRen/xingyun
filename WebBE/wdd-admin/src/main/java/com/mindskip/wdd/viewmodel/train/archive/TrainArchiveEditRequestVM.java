package com.mindskip.wdd.viewmodel.train.archive;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @version 9.0.0
 * @description: 培训分类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/16 10:45
 */
@Data
public class TrainArchiveEditRequestVM {

    private Integer id;

    /**
     * 报名名称
     */
    @NotBlank(message = "名称不能为空")
    @Length(max = 255, message = "名称长度最大为255个字符")
    private String name;

    /**
     * 报名排序
     */
    private Integer itemOrder;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 父节点id
     */
    private Integer parentId;
}
