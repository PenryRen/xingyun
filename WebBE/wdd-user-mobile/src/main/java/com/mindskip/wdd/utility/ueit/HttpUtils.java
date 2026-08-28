package com.mindskip.wdd.utility.ueit;

import cn.hutool.http.*;
import com.mindskip.wdd.configuration.ueit.VmWareConfigKey;
import com.mindskip.wdd.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Http请求工具类
 *
 * @author libl
 * @date 2025-04-15
 */
@Component
public class HttpUtils {

    @Autowired
    private VmWareConfigKey vmWareConfigKeyAuto;

    @Autowired
    private SysConfigService sysConfigServiceAuto;

    private static VmWareConfigKey vmWareConfigKey;

    private static SysConfigService sysConfigService;

    @PostConstruct
    public void init() {
        vmWareConfigKey = vmWareConfigKeyAuto;
        sysConfigService = sysConfigServiceAuto;
    }

    /**
     * 获取授权
     *
     * @return Authorization
     */
    private static String getAuthorization() {
        String authConfigKey = vmWareConfigKey.getAuthConfigKey();
        return sysConfigService.selectConfigByKey(authConfigKey);
    }

    /**
     * Get请求
     *
     * @return 结果
     */
    public static HttpResponse sendGet(String url, Map<String, Object> formMap) {
        HttpRequest request = HttpUtil.createGet(url);
        request.header(Header.CONTENT_TYPE, ContentType.JSON.getValue());
        request.header(Header.AUTHORIZATION, getAuthorization());
        request.form(formMap);
        return request.execute();
    }

    /**
     * Post请求
     *
     * @return 结果
     */
    public static HttpResponse sendPost(String url, String body) {
        HttpRequest request = HttpUtil.createPost(url);
        request.header(Header.CONTENT_TYPE, ContentType.JSON.getValue());
        request.header(Header.AUTHORIZATION, getAuthorization());
        request.body(body);
        return request.execute();
    }
}