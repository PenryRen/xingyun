package com.mindskip.wdd.viewmodel.announcement;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @version 1.7.0
 * @description: 公告分类编辑
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class AnnouncementArchiveEditRequestVM {

    private Integer id;

    /**
     * 公告分类名称
     */
    @NotBlank(message = "名称不能为空")
    @Length(max = 255, message = "名称长度最大为255个字符")
    private String name;

    /**
     * 公告分类排序
     */
    private Integer itemOrder;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 上级节点id
     */
    private Integer parentId;
}
