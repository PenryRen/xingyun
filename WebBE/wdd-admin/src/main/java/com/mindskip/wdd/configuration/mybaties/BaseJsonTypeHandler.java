package com.mindskip.wdd.configuration.mybaties;

import com.mindskip.wdd.utility.JsonUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @version 1.7.0
 * @description: mybaties Json对象转换
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public abstract class BaseJsonTypeHandler extends BaseTypeHandler<Object> {


    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        preparedStatement.setObject(i, JsonUtil.toJsonStr(o));
    }


    @Override
    public Object getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return resultSet.getString(i);
    }

    @Override
    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return callableStatement.getString(i);
    }
}
