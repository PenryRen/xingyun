package com.mindskip.wdd.viewmodel.announcement;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 公告编辑
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class AnnouncementEditRequestVM {

    private Integer id;

    /**
     * 公告标题
     */
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 公告封面
     */
    @NotBlank(message = "封面不能为空")
    private String imageSrc;

    /**
     * 公告详细内容
     */
    @NotBlank(message = "详细内容不能为空")
    private String content;

    /**
     * 公告发布部门
     */
    @NotNull(message = "部门不能为空")
    private List<Integer> departmentIdList;

    /**
     * 公告分类
     */
    private Integer announcementArchiveId;

    /**
     * 是否顶置
     */
    @NotNull(message = "是否顶置")
    private Boolean overhead;

    /**
     * 是否重要
     */
    @NotNull(message = "是否重要")
    private Boolean importanted;
}
