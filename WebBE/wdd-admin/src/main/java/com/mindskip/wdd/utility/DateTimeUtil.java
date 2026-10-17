package com.mindskip.wdd.utility;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 时间工具类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
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


    public static String dateTimeFullNumberFormat(Date date) {
        if (null == date) {
            return "";
        }
        DateTime time = new DateTime(date);
        return time.toString(DatePattern.PURE_DATETIME_PATTERN);
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
        return time.toString(DatePattern.NORM_TIME_FORMAT);
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
     * Parse date.
     *
     * @param dateStr the date str
     * @param timeStr the time str
     * @return the date
     */
    public static Date parse(String dateStr, String timeStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        if (StringUtils.isBlank(timeStr)) {
            return null;
        }
        return DateUtil.parse(String.format("%s %s", dateStr, timeStr), DatePattern.NORM_DATETIME_FORMAT);
    }


    /**
     * Between second int.
     *
     * @param startDateTime the start date time
     * @param endDateTime   the end date time
     * @return the int
     */
    public static int betweenSecond(String startDateTime, String endDateTime) {
        return (int) DateUtil.between(parse(startDateTime), parse(endDateTime), DateUnit.SECOND, true);
    }


    /**
     * Between second int.
     *
     * @param startDateTime the start date time
     * @param endDateTime   the end date time
     * @return the int
     */
    public static int betweenSecond(Date startDateTime, Date endDateTime) {
        return (int) DateUtil.between(startDateTime, endDateTime, DateUnit.SECOND, true);
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
     * 时间转化成秒(00:00:00)
     *
     * @param time
     * @return {@link Integer}
     */
    public static Integer timeToSecond(String time) {
        if (StringUtils.isNotEmpty(time)) {
            String[] timeSplit = time.split(":");
            if (timeSplit.length == 1) {
                return Integer.parseInt(timeSplit[0]);
            } else if (timeSplit.length == 2) {
                return Integer.parseInt(timeSplit[0]) * 60 + Integer.parseInt(timeSplit[1]);
            } else if (timeSplit.length == 3) {
                return Integer.parseInt(timeSplit[0]) * 60 * 60 + Integer.parseInt(timeSplit[1]) * 60 + Integer.parseInt(timeSplit[2]);
            }
        }
        return null;
    }

    /**
     * 秒转化成时间(00:00:00)
     *
     * @param second
     * @return {@link String}
     */
    public static String secondToTime(Integer second) {
        Integer hour = second / 60 / 60;
        Integer minutes = second / 60 % 60;
        Integer remainingSeconds = second % 60;
        if (!hour.equals(0)) {
            return hour + ":" + minutes + ":" + remainingSeconds;
        } else if (!minutes.equals(0)) {
            return minutes + ":" + remainingSeconds;
        } else {
            return remainingSeconds.toString();
        }
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
