package com.mindskip.wdd.configuration.spring.mvc;

import com.mindskip.wdd.configuration.spring.interceptor.AuthHandlerInterceptor;
import com.mindskip.wdd.configuration.spring.interceptor.TokenHandlerInterceptor;
import com.mindskip.wdd.configuration.utility.YmlUtil;
import com.mindskip.wdd.service.SystemService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;


/**
 * @version 1.7.0
 * @description: 拦截器配置
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Configuration
@AllArgsConstructor
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    private final SystemService systemService;
    private final TokenHandlerInterceptor tokenHandlerInterceptor;
    private final AuthHandlerInterceptor authHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> ignoresList = YmlUtil.ymlArrayConvert(systemService.getSecurityIgnoreUrls());
        String[] ignores = new String[ignoresList.size()];
        registry.addInterceptor(authHandlerInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(ignoresList.toArray(ignores));
        registry.addInterceptor(tokenHandlerInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(ignoresList.toArray(ignores));
        super.addInterceptors(registry);
    }

}
