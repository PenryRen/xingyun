package com.mindskip.wdd.viewmodel.word;

import com.mindskip.wdd.domain.enums.QuestionTypeEnum;
import lombok.Data;
import org.apache.poi.xwpf.usermodel.IBodyElement;

import java.util.List;

/**
 * @version 1.7.0
 * @description: word解析
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class QuestionWordElement {
    /**
     * 题目类型
     */
    private QuestionTypeEnum questionTypeEnum;
    /**
     * word节点
     */
    private List<IBodyElement> bodyElements;
    /**
     * 结果
     */
    private String result;
}
