package com.mindskip.wdd.utility;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 时间工具类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public class DateTimeUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateTimeUtil.class);

    /**
     * Date time full format string.
     *
     * @param date the date
     * @return the string
     */
    public static String dateTimeFullFormat(Date date) {
        if (null == date) {
            return "";
        }
        DateTime time = new DateTime(date);
        return time.toString(DatePattern.NORM_DATETIME_FORMAT);
    }

    public static String dateTimeNumberFormat(Date date) {
        if (null == date) {
            return "";
        }
        DateTime time = new DateTime(date);
        return time.toString("yyyy/MM/dd");
    }


    /**
     * Date format string.
     *
     * @param date the date
     * @return the string
     */
    public static String dateFormat(Date date) {
        if (null == date) {
            return "";
        }
        DateTime time = new DateTime(date);
        return time.toString(DatePattern.NORM_DATE_FORMAT);
    }


    /**
     * Date chinese format string.
     *
     * @param date the date
     * @return the string
     */
    public static String dateChineseFormat(Date date) {
        if (null == date) {
            return "";
        }
        DateTime time = new DateTime(date);
        return time.toString(DatePattern.CHINESE_DATE_PATTERN);
    }


    /**
     * Time format string.
     *
     * @param date the date
     * @return the string
     */
    public static String timeFormat(Date date) {
        if (null == date) {
            return "";
        }
        DateTime time = new DateTime(date);
        return time.toString("HH:mm:ss");
    }


    /**
     * To date date.
     *
     * @param dateStr the date str
     * @return the date
     */
    public static Date toDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        return DateUtil.parse(dateStr, DatePattern.NORM_DATE_FORMAT);
    }


    /**
     * To time date.
     *
     * @param dateStr the date str
     * @return the date
     */
    public static Date toTime(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        return DateUtil.parse(dateStr, DatePattern.NORM_TIME_PATTERN);
    }

    /**
     * Parse date.
     *
     * @param dateStr the date str
     * @return the date
     */
    public static Date parse(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        return DateUtil.parse(dateStr, DatePattern.NORM_DATETIME_FORMAT);
    }


    /**
     * To local date time date.
     *
     * @param dateStr the date str
     * @return the date
     */
    public static Date toLocalDateTime(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        DateTime dateTime = DateUtil.parse(dateStr, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return DateUtil.offsetHour(dateTime, +8);
    }



    public static String secondToChineseTime(Integer second) {
        Integer hour = second / 60 / 60;
        Integer minutes = second / 60 % 60;
        Integer remainingSeconds = second % 60;
        if (!hour.equals(0)) {
            return hour + "时" + minutes + "分" + remainingSeconds + "秒";
        } else if (!minutes.equals(0)) {
            return minutes + "分" + remainingSeconds + "秒";
        } else {
            return remainingSeconds + "秒";
        }
    }

}
