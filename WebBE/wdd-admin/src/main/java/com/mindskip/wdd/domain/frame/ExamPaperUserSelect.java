package com.mindskip.wdd.domain.frame;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 试卷发布 - 用户选择
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class ExamPaperUserSelect {

    /**
     * Instantiates a new Exam paper user select.
     */
    public ExamPaperUserSelect() {
    }

    /**
     * Instantiates a new Exam paper user select.
     *
     * @param id the id
     */
    public ExamPaperUserSelect(Integer id) {
        this.id = id;
    }

    /**
     * 用户id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 用户部门
     */
    private Integer departmentId;

}
