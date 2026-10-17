package com.mindskip.wdd.configuration.application;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * @version 1.7.0
 * @description: ApplicationContextProvider
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext context;

    private ApplicationContextProvider() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 获取bean
     *
     * @param <T>    the type parameter
     * @param aClass the a class
     * @return the bean
     */
    public static <T> T getBean(Class<T> aClass) {
        return context.getBean(aClass);
    }

    /**
     * Gets bean.
     *
     * @param <T>  the type parameter
     * @param name the name
     * @return the bean
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) context.getBean(name);
    }
}

