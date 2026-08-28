package com.mindskip.wdd.utility;

import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @version 1.7.0
 * @description: http请求工具类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public class RestUtil {
    private static final Logger logger = LoggerFactory.getLogger(RestUtil.class);


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
}
