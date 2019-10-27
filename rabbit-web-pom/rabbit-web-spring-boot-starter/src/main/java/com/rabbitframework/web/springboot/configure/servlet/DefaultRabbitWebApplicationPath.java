package com.rabbitframework.web.springboot.configure.servlet;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import javax.ws.rs.ApplicationPath;

public class DefaultRabbitWebApplicationPath implements RabbitWebApplicationPath {
    private final String applicationPath;

    private final ResourceConfig config;

    public DefaultRabbitWebApplicationPath(String applicationPath, ResourceConfig config) {
        this.applicationPath = applicationPath;
        this.config = config;
    }

    @Override
    public String getPath() {
        return resolveApplicationPath();
    }

    private String resolveApplicationPath() {
        if (StringUtils.hasLength(this.applicationPath)) {
            return this.applicationPath;
        }
        return findApplicationPath(
                AnnotationUtils.findAnnotation(this.config.getApplication().getClass(), ApplicationPath.class));
    }

    private static String findApplicationPath(ApplicationPath annotation) {
        // Jersey doesn't like to be the default servlet, so map to /* as a fallback
        if (annotation == null) {
            return "/*";
        }
        return annotation.value();
    }
}
