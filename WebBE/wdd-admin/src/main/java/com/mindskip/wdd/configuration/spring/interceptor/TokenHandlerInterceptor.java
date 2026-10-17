package com.mindskip.wdd.configuration.spring.interceptor;

import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.context.WebContext;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.UserToken;
import com.mindskip.wdd.service.RoleService;
import com.mindskip.wdd.service.SystemService;
import com.mindskip.wdd.service.UserService;
import com.mindskip.wdd.service.UserTokenService;
import com.mindskip.wdd.utility.JsonUtil;
import com.mindskip.wdd.utility.RestUtil;
import com.mindskip.wdd.viewmodel.login.WddToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * @version 1.7.0
 * @description: 权限验证拦截器
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Component
public class TokenHandlerInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(TokenHandlerInterceptor.class);
    private final UserTokenService userTokenService;
    private final UserService userService;
    private final WebContext webContext;
    private final SystemService systemService;
    private final RoleService roleService;

    /**
     * Instantiates a new Token handler interceptor.
     *
     * @param userTokenService the user token service
     * @param userService      the user service
     * @param webContext       the web context
     * @param systemService    the system service
     * @param roleService      the role service
     */
    @Autowired
    public TokenHandlerInterceptor(UserTokenService userTokenService, UserService userService, WebContext webContext, SystemService systemService, RoleService roleService) {
        this.userTokenService = userTokenService;
        this.userService = userService;
        this.webContext = webContext;
        this.systemService = systemService;
        this.roleService = roleService;
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
            String encodeToken = request.getHeader("token");
            if (StringUtils.isEmpty(encodeToken)) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals("ueit-admin-token")) {
                        encodeToken = cookie.getValue();
                        break;
                    }
                }
                if (StringUtils.isEmpty(encodeToken)) {
                    RestUtil.response(response, SystemCode.UNAUTHORIZED);
                    return false;
                }
            }


            Date now = new Date();
            String encodeTokenStr = systemService.pairOneDecode(encodeToken);
            WddToken wddToken = JsonUtil.toJsonObject(encodeTokenStr, WddToken.class);
            if (now.after(wddToken.getEndTime()))  //token 过期
            {
                RestUtil.response(response, SystemCode.AccessTokenError);
                return false;
            }

            UserToken userToken = userTokenService.getToken(wddToken.getToken());
            if (null == userToken) {
                RestUtil.response(response, SystemCode.UNAUTHORIZED);
                return false;
            }

            if (now.before(userToken.getEndTime())) {
                User user = userService.getUserByUserName(userToken.getUserName());
                if (null == user) {
                    RestUtil.response(response, SystemCode.UNAUTHORIZED);
                    return false;
                }

                //菜单角色权限校验
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Method method = handlerMethod.getMethod();
                PreAuthorize preAuthorize = method.getAnnotation(PreAuthorize.class);
                if (Objects.nonNull(preAuthorize)) {
                    Boolean havePermission = roleService.getRoleMenuPermission(user.getRoleId())
                            .stream()
                            .anyMatch(rm -> Arrays.stream(rm.getPermissions().split(","))
                                    .anyMatch(p -> Arrays.stream(preAuthorize.value()).anyMatch(pa -> pa.equals(p))));
                    if (havePermission) {
                        webContext.setContext(user, userToken);
                        return true;
                    } else {  //角色没有权限调用
                        RestUtil.response(response, SystemCode.AccessDenied);
                        return false;
                    }
                } else {
                    webContext.setContext(user, userToken);
                    return true;
                }

            } else {   //token 过期
                RestUtil.response(response, SystemCode.AccessTokenError);
                return false;
            }
        } catch (Exception e) {
            RestUtil.response(response, SystemCode.UNAUTHORIZED);
            return false;
        }
    }
}
