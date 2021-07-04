package com.rabbitframework.web.springboot.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = RabbitWebProperties.RABBIT_WEB_PREFIX)
public class RabbitWebProperties {
    public static final String RABBIT_WEB_PREFIX = "rabbit.web";
    private String jspPath = "/WEB-INF/jsp";
    private String freemarkerPath = "freemarker";
    private String templateVariablePath = "";
    private String restPackages = "";
    private boolean requestLog = false;
    private Integer filterOrder = 0;
    /**
     * 静态资源过滤正则
     */
    private String staticContentRegex = "/(((images|css|js|static|jsp|WEB-INF/jsp)/.*)|(favicon.ico))";
    /**
     * Init parameters to pass to Jersey through the servlet or filter.
     */
    private Map<String, String> initParams = new HashMap<String, String>();

    public void setJspPath(String jspPath) {
        this.jspPath = jspPath;
    }

    public String getJspPath() {
        return jspPath;
    }

    public String getFreemarkerPath() {
        return freemarkerPath;
    }

    public void setFreemarkerPath(String freemarkerPath) {
        this.freemarkerPath = freemarkerPath;
    }

    public String getRestPackages() {
        return restPackages;
    }

    public void setRestPackages(String restPackages) {
        this.restPackages = restPackages;
    }

    public Map<String, String> getInitParams() {
        return initParams;
    }

    public void setInitParams(Map<String, String> initParams) {
        this.initParams = initParams;
    }

    public boolean isRequestLog() {
        return requestLog;
    }

    public void setRequestLog(boolean requestLog) {
        this.requestLog = requestLog;
    }

    public String getTemplateVariablePath() {
        return templateVariablePath;
    }

    public void setTemplateVariablePath(String templateVariablePath) {
        this.templateVariablePath = templateVariablePath;
    }

    public Integer getFilterOrder() {
        return filterOrder;
    }

    public void setFilterOrder(Integer filterOrder) {
        this.filterOrder = filterOrder;
    }

    public String getStaticContentRegex() {
        return staticContentRegex;
    }

    public void setStaticContentRegex(String staticContentRegex) {
        this.staticContentRegex = staticContentRegex;
    }
}
