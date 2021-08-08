package com.scloudic.rabbitframework.web.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.Locale;

public class ServletContextHelper implements ServletContextAware, ApplicationContextAware {
    private static ApplicationContext applicationContext;
    private static ServletContext servletContext;
    @SuppressWarnings("unused")
    private String language;

    public void setServletContext(ServletContext servletContext) {
        ServletContextHelper.servletContext = servletContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ServletContextHelper.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static ServletContext getServletContext() {
        return servletContext;
    }

    public static Object getBean(String id) {
        return applicationContext.getBean(id);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static String getMessage(String key) {
        return getMessage(key, Locale.CHINA);
    }

    public static String getMessage(String key, Object... args) {
        return getMessage(key, args, key, Locale.CHINA);
    }

    public static String getMessage(String key, Locale p_locale) {
        return getMessage(key, null, key, p_locale);
    }

    public static String getMessage(String key, Object[] args, String defaultValue, Locale p_locale) {
        if (getApplicationContext() == null) {
            return key;
        }
        return getApplicationContext().getMessage(key, args, defaultValue, p_locale);
    }
}
