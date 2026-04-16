package com.mindskip.wdd.configuration.spring.interceptor;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.configuration.ueit.VmWareConfigKey;
import com.mindskip.wdd.service.SysConfigService;
import com.mindskip.wdd.service.SystemService;
import com.mindskip.wdd.utility.RestUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 授权过期验证拦截器
 *
 * @author libl
 * @date 2025-04-16
 */
@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthHandlerInterceptor.class);
    private final SysConfigService sysConfigService;
    private final SystemService systemService;
    private final VmWareConfigKey vmWareConfigKey;

    /**
     * Instantiates a new Token handler interceptor.
     *
     * @param sysConfigService the config service
     * @param vmWareConfigKey  the vmWare config key
     */
    @Autowired
    public AuthHandlerInterceptor(SysConfigService sysConfigService, SystemService systemService, VmWareConfigKey vmWareConfigKey) {
        this.sysConfigService = sysConfigService;
        this.systemService = systemService;
        this.vmWareConfigKey = vmWareConfigKey;
    }

    /**
     * 权限验证
     *
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String expirationConfigKey = vmWareConfigKey.getExpirationConfigKey();
            if (StringUtils.isEmpty(expirationConfigKey)) {
                RestUtil.response(response, SystemCode.AuthExpiration);
                return false;
            }
            String encryptStr = sysConfigService.selectConfigByKey(expirationConfigKey);
            if (StringUtils.isEmpty(encryptStr)) {
                RestUtil.response(response, SystemCode.AuthExpiration);
                return false;
            }
            String decryptStr = systemService.pairOneDecode(encryptStr);
            DateTime expirationTime = DateUtil.parseDateTime(decryptStr);
            if (DateUtil.date().getTime() < expirationTime.getTime()) {
                return true;
            }
            RestUtil.response(response, SystemCode.AuthExpiration);
            return false;
        } catch (Exception e) {
            RestUtil.response(response, SystemCode.AuthExpiration);
            return false;
        }
    }

}
