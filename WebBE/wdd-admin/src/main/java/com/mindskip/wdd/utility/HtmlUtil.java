package com.mindskip.wdd.utility;

import com.mindskip.wdd.utility.xss.HTMLFilter;
import org.apache.commons.lang3.StringUtils;

/**
 * @version 1.7.0
 * @description: html工具类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public class HtmlUtil {

    private static HTMLFilter htmlFilter = new HTMLFilter();

    /**
     * 清理html标签
     *
     * @param htmlStr the html str
     * @return the string
     */
    public static String clear(String htmlStr) {
        return cn.hutool.http.HtmlUtil.cleanHtmlTag(htmlStr);
    }

    /**
     * html标签转码
     *
     * @param htmlStr the html str
     * @return the string
     */
    public static String escape(String htmlStr) {
        return cn.hutool.http.HtmlUtil.escape(htmlStr);
    }


    /**
     * xss标签清理
     *
     * @param html
     * @return {@link String}
     */
    public static String xssClear(String html) {
        if (StringUtils.isNoneEmpty(html)) {
            return htmlFilter.filter(html);
        } else {
            return html;
        }
    }
}
