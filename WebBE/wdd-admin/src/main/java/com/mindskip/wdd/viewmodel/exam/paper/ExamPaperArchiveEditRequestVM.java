package com.mindskip.wdd.viewmodel.exam.paper;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @version 1.7.0
 * @description: 试卷分类编辑
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class ExamPaperArchiveEditRequestVM {

    private Integer id;

    /**
     * 分类名称
     */
    @NotBlank(message = "名称不能为空")
    @Length(max = 255, message = "名称长度最大为255个字符")
    private String name;

    /**
     * 分类排序
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
