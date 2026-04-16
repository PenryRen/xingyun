package com.mindskip.wdd.utility;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 工具类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public class ExamUtil {

    /**
     * 分数除以10
     *
     * @param score the score
     * @return the string
     */
    public static String scoreToVM(Integer score) {
        if (null == score) {
            return null;
        }

        if (score % 10 == 0) {
            return String.valueOf(score / 10);
        } else {
            return String.format("%.1f", score / 10.0);
        }
    }

    /**
     * 分数乘以10
     *
     * @param score the score
     * @return the integer
     */
    public static Integer scoreFromVM(String score) {
        if (score == null) {
            return null;
        } else {
            return (int) (Float.parseFloat(score) * 10);
        }
    }

    /**
     * 秒转为时间段
     *
     * @param secondNumber the second
     * @return the string
     */
    public static String secondToVM(Integer secondNumber) {
        final StringBuilder sb = new StringBuilder();
        if (null != secondNumber) {
            long betweenMs = secondNumber * 1000;
            if (betweenMs > 0) {
                long day = betweenMs / DateUnit.DAY.getMillis();
                long hour = betweenMs / DateUnit.HOUR.getMillis() - day * 24;
                long minute = betweenMs / DateUnit.MINUTE.getMillis() - day * 24 * 60 - hour * 60;
                final long BetweenOfSecond = ((day * 24 + hour) * 60 + minute) * 60;
                long second = betweenMs / DateUnit.SECOND.getMillis() - BetweenOfSecond;
                if (0 != day) {
                    sb.append(day).append("天");
                }
                if (0 != hour) {
                    sb.append(hour).append("小时");
                }
                if (0 != minute) {
                    sb.append(minute).append("分钟");
                }
                if (0 != second) {
                    sb.append(second).append("秒");
                }
            }
        }
        if (StrUtil.isEmpty(sb)) {
            sb.append(0).append("秒");
        }
        return sb.toString();
    }

    /**
     * 分钟转为时间段
     *
     * @param min the min
     * @return the string
     */
    public static String minToVM(Integer min) {
        return secondToVM(min * 60);
    }

    public static String monToVM(Integer mon) {
        Integer split = 12;
        if (mon >= split) {
            if (mon % split == 0) {
                return String.format("%d年", mon / split);
            } else {
                return String.format("%d年%d月", mon / split, mon % split);
            }
        } else {
            return String.format("%d月", mon);
        }
    }

    private static final String ANSWER_SPLIT = ",";


    /**
     * 字符串数组排序
     *
     * @param contentArray the content array
     * @return the string
     */
    public static String contentToString(List<String> contentArray) {
        return contentArray.stream().sorted().collect(Collectors.joining(ANSWER_SPLIT));
    }


    /**
     * 字符串转为数组
     *
     * @param contentArray the content array
     * @return the list
     */
    public static List<String> contentToArray(String contentArray) {
        return Arrays.asList(contentArray.split(ANSWER_SPLIT));
    }

    private static final String FORM_ANSWER_SPLIT = "_";

    /**
     * 获取最后数字+1
     *
     * @param str the str
     * @return the integer
     */
    public static Integer lastNum(String str) {
        Integer start = str.lastIndexOf(FORM_ANSWER_SPLIT);
        return Integer.parseInt(str.substring(start + 1));
    }

    /**
     * 数字转化为百分比
     *
     * @param value the value
     * @return the string
     */
    public static String percentFormat(Double value) {
        if (null == value)
            return "";
        else {
            return NumberUtil.decimalFormat("#.##%", value.doubleValue());
        }
    }
}
