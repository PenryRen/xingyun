package com.mindskip.wdd.utility;

/**
 * @version 1.7.0
 * @description: 错误提示格式化
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public class ErrorUtil {
    /**
     * Parameter error format string.
     *
     * @param field the field
     * @param msg   the msg
     * @return the string
     */
    public static String parameterErrorFormat(String field, String msg) {
        return "【" + field + " : " + msg + "】";
    }

    /**
     * Parameter error format string.
     *
     * @param msg the msg
     * @return the string
     */
    public static String parameterErrorFormat(String msg) {
        return "【" + msg + "】";
    }
}
