package com.mindskip.wdd.utility;

import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @version 1.7.0
 * @description: http请求工具类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public class RestUtil {
    private static final Logger logger = LoggerFactory.getLogger(com.mindskip.wdd.utility.RestUtil.class);


    /**
     * Response.
     *
     * @param response   the response
     * @param systemCode the system code
     */
    public static void response(HttpServletResponse response, SystemCode systemCode) {
        response(response, systemCode.getCode(), systemCode.getMessage());
    }

    /**
     * Response.
     *
     * @param response   the response
     * @param systemCode the system code
     * @param msg        the msg
     */
    public static void response(HttpServletResponse response, int systemCode, String msg) {
        response(response, systemCode, msg, null);
    }


    /**
     * http返回写入json
     *
     * @param response   the response
     * @param systemCode the system code
     * @param msg        the msg
     * @param content    the content
     */
    public static void response(HttpServletResponse response, int systemCode, String msg, Object content) {
        try {
            RestResponse res = new RestResponse<>(systemCode, msg, content);
            String resStr = JsonUtil.toJsonStr(res);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.write(resStr);
            printWriter.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * http post 请求
     *
     * @param url
     * @param
     * @return {@link String}
     */
    public static String httpClientPost(String url, String parameter) {
        HttpPost httpPost = new HttpPost(url);
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            BasicResponseHandler handler = new BasicResponseHandler();
            StringEntity entity = new StringEntity(parameter, "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            return httpClient.execute(httpPost, handler);
        } catch (Exception e) {
            return null;
        }
    }
}
