package com.mindskip.wdd.viewmodel.train.exam.answer;

import com.mindskip.wdd.base.BaseFilter;
import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @version 9.0.0
 * @description: 下一张待批改培训试卷
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/26 10:45
 */
@Data
public class NextAnswerRequestVM extends BaseFilter {
    private Long id;
    private Integer trainId;
}
