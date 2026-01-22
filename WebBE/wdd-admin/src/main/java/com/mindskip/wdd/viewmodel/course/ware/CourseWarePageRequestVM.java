package com.mindskip.wdd.viewmodel.course.ware;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 课件分页
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class CourseWarePageRequestVM extends BasePage {
    /**
     * 课件名称
     */
    private String name;
    /**
     * 课件类型
     */
    private Integer fileType;

    /**
     * 课件分类
     */
    private List<Integer> courseWareArchiveIdList;

    /**
     * 已选中Id
     */
    private List<Integer> selectIdList;

    /**
     * 实训环境
     */
    private String vmType;
}
