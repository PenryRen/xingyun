package com.mindskip.wdd.viewmodel.exam.paper;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 试卷分页
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class ExamPaperPageRequestVM extends BasePage {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 部门id
     */
    private Integer departmentId;
    /**
     * 当前试卷
     */
    private Date now;

    /**
     * 试卷分类id
     */
    private Integer examPaperArchiveId;

    /**
     * 试卷分类id集合
     */
    private List<Integer> examPaperArchiveIdList;

    /**
     * 试卷类型
     */
    @NotNull
    private Integer paperType;
}
