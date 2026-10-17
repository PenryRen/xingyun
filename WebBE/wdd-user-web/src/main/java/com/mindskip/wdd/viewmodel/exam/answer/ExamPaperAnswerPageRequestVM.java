package com.mindskip.wdd.viewmodel.exam.answer;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 答卷分页查询
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ExamPaperAnswerPageRequestVM extends BasePage {

    /**
     * 答卷人
     */
    private Integer createUser;

    /**
     * 是否通过
     */
    private Boolean passed;

    /**
     * 试卷分类Id
     */
    private Integer examPaperArchiveId;

    /**
     * 试卷分类id列表
     */
    private List<Integer> examPaperArchiveIdList;
}
