package com.mindskip.wdd.configuration.mybaties;

import com.mindskip.wdd.domain.frame.CredentialItemFrame;
import com.mindskip.wdd.utility.JsonUtil;
import org.apache.ibatis.type.MappedTypes;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @version 7.1.0
 * @description: CredentialItemFrame对象转json
 * Copyright (C), 2024, 麒技团队
 * @date 2024/7/30 10:45
 */
@MappedTypes(Object.class)
public class CredentialItemFrameJsonTypeHandler extends BaseJsonTypeHandler {

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return JsonUtil.toJsonListObject(resultSet.getString(s), CredentialItemFrame.class);
    }

}
