package com.mindskip.wdd.viewmodel.train.course;

import com.mindskip.wdd.viewmodel.course.ware.CourseWareQuestionVM;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训编辑
 * Copyright (C), 2025, 麟航团队
 * @date 2025/10/26 10:45
 */
@Data
public class TrainEditRequestVM {

    private Integer id;

    /**
     * 课程名称
     */
    @NotBlank(message = "课程名称不能为空")
    private String name;

    /**
     * 课程分类id
     */
    private Integer trainArchiveId;

    /**
     * 课程描述
     */
    @NotBlank(message = "课程描述不能为空")
    private String description;

    /**
     * 封面
     */
    @NotNull
    private String coverPath;

    /**
     * 发布部门
     */
    @NotNull(message = "部门不能为空")
    private List<Integer> departmentIdList;

    /**
     * 课程时间
     */
    @Size(min = 2, max = 2)
    private List<String> limitDateTime;

    /**
     * 课件列表
     */
    @Size(min = 1, message = "请添加课件")
    private List<TrainEditItem> courseWareItemList;

    /**
     * 培训试卷
     */
    private TrainEditItem examPaperItem;

    /**
     * 证书
     */
    private TrainEditItem credentialItem;

    /**
     * 排序
     */
    private Integer itemOrder;
}
