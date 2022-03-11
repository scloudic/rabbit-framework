package com.scloudic.rabbitframework.web.springboot.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = RabbitWebProperties.RABBIT_WEB_PREFIX)
public class RabbitWebProperties {
    public static final String RABBIT_WEB_PREFIX = "rabbit.web";
    private boolean requestLogEnable = true;
    private boolean xssFilterEnable = true;
    private boolean freemarkerEnable = false;
    private String freemarkerVariablePath = "";

    public boolean isRequestLogEnable() {
        return requestLogEnable;
    }

    public void setRequestLogEnable(boolean requestLogEnable) {
        this.requestLogEnable = requestLogEnable;
    }

    public boolean isXssFilterEnable() {
        return xssFilterEnable;
    }

    public void setXssFilterEnable(boolean xssFilterEnable) {
        this.xssFilterEnable = xssFilterEnable;
    }

    public boolean isFreemarkerEnable() {
        return freemarkerEnable;
    }

    public void setFreemarkerEnable(boolean freemarkerEnable) {
        this.freemarkerEnable = freemarkerEnable;
    }

    public String getFreemarkerVariablePath() {
        return freemarkerVariablePath;
    }

    public void setFreemarkerVariablePath(String freemarkerVariablePath) {
        this.freemarkerVariablePath = freemarkerVariablePath;
    }
}
