package com.scloudic.rabbitframework.web.springboot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jersey.JerseyAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * web 启动加载类
 * 排除{@link JerseyAutoConfiguration}
 */
@SpringBootApplication(exclude = {JerseyAutoConfiguration.class})
public abstract class RabbitWebApplication extends SpringBootServletInitializer {
    public RabbitWebApplication() {
        super();
        setRegisterErrorPageFilter(false);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(getClass());
    }
}
