package com.rabbitframework.web.springboot.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = RabbitWebProperties.RABBIT_WEB_PREFIX)
public class RabbitWebProperties {
    public static final String RABBIT_WEB_PREFIX = "rabbit.web";
    private String jspPath = "/WEB-INF/jsp";
    private String freemarkerPath = "freemarker";
    private String restPackages = "";
    private Type type = Type.SERVLET;
    private Filter filter = new Filter();
    private Servlet servlet = new Servlet();
    /**
     * Init parameters to pass to Jersey through the servlet or filter.
     */
    private Map<String, String> initParams = new HashMap<String, String>();
    /**
     * Path that serves as the base URI for the application. If specified, overrides the
     * value of "@ApplicationPath".
     */
    private String applicationPath;

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

    public String getApplicationPath() {
        return applicationPath;
    }

    public void setApplicationPath(String applicationPath) {
        this.applicationPath = applicationPath;
    }

    public Map<String, String> getInitParams() {
        return initParams;
    }

    public void setInitParams(Map<String, String> initParams) {
        this.initParams = initParams;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Servlet getServlet() {
        return servlet;
    }

    public void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }

    public enum Type {
        SERVLET, FILTER
    }

    public static class Filter {
        /**
         * Jersey filter chain order.
         */
        private int order;

        public int getOrder() {
            return this.order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

    }

    public static class Servlet {
        /**
         * Load on startup priority of the Jersey servlet.
         */
        private int loadOnStartup = -1;

        public int getLoadOnStartup() {
            return this.loadOnStartup;
        }

        public void setLoadOnStartup(int loadOnStartup) {
            this.loadOnStartup = loadOnStartup;
        }

    }

}
