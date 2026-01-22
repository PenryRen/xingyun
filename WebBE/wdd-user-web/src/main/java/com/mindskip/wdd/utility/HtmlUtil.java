package com.mindskip.wdd.utility;

import com.mindskip.wdd.utility.xss.HTMLFilter;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version 3.2.0
 * @description: html工具类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
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
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>";
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>";
        String regEx_html = "<[^>]+>";
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");
        return htmlStr.trim();
    }


    public static String StringSub(String str, String keyWord, Integer length) {
        int position = str.indexOf(keyWord);
        int fromIndexInclude = position - length;
        int toIndexExclude = position + length;
        int len = str.length();

        if (fromIndexInclude < 0) {
            fromIndexInclude = 0;
        } else if (fromIndexInclude > len) {
            fromIndexInclude = len;
        }

        if (toIndexExclude < 0) {
            toIndexExclude = len + toIndexExclude;
            if (toIndexExclude < 0) {
                toIndexExclude = len;
            }
        } else if (toIndexExclude > len) {
            toIndexExclude = len;
        }

        if (toIndexExclude < fromIndexInclude) {
            int tmp = fromIndexInclude;
            fromIndexInclude = toIndexExclude;
            toIndexExclude = tmp;
        }

        if (fromIndexInclude == toIndexExclude) {
            return "";
        }

        return str.substring(fromIndexInclude, toIndexExclude);
    }


    public static String clearAnswerHtml(String str) {
        str = cn.hutool.http.HtmlUtil.cleanHtmlTag(str);
        str = str.replace("&nbsp;", "");
        str = str.trim();
        return str;
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
