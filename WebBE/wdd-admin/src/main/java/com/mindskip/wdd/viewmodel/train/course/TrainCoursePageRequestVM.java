package com.mindskip.wdd.viewmodel.train.course;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 课程分页
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/26 10:45
 */

@Data
public class TrainCoursePageRequestVM extends BasePage {
    /**
     * 课程名称
     */
    private String name;

    /**
     * 课程分类
     */
    private List<Integer> trainArchiveIdList;
}
