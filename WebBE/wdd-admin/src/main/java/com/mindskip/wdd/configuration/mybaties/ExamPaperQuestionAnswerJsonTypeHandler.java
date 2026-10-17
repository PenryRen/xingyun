package com.mindskip.wdd.configuration.mybaties;

import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import com.mindskip.wdd.utility.JsonUtil;
import org.apache.ibatis.type.MappedTypes;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @version 1.7.0
 * @description: QuestionAnswerFrame对象转json
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@MappedTypes(Object.class)
public class ExamPaperQuestionAnswerJsonTypeHandler extends BaseJsonTypeHandler {

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return JsonUtil.toJsonObject(resultSet.getString(s), QuestionAnswerFrame.class);
    }

}
