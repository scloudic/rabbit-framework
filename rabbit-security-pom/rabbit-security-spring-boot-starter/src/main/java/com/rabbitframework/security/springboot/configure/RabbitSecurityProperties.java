package com.rabbitframework.security.springboot.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = RabbitSecurityProperties.RABBIT_WEB_PREFIX)
public class RabbitSecurityProperties {
    public static final String RABBIT_WEB_PREFIX = "rabbit.security";
    private String loginUrl = "/user/toLogin";
    private String unauthorizedUrl = "/user/unauthorized";
    private String filterUrls = "/images/**,/lib/**,/res/**,/static/**";
    private Map<String, String> filterChainDefinitions;
    private String sessionDaoKeyPrefix = "rabbit_session";
    private Integer cacheSessionExpire = 604800;
    private Integer otherCacheExpire = 600;
    private List<String> realmBeanNames = new ArrayList<String>();

    public List<String> getRealmBeanNames() {
        return realmBeanNames;
    }

    public void setRealmBeanNames(List<String> realmBeanNames) {
        this.realmBeanNames = realmBeanNames;
    }

    public Integer getCacheSessionExpire() {
        return cacheSessionExpire;
    }

    public void setCacheSessionExpire(Integer cacheSessionExpire) {
        this.cacheSessionExpire = cacheSessionExpire;
    }

    public String getSessionDaoKeyPrefix() {
        return sessionDaoKeyPrefix;
    }

    public void setSessionDaoKeyPrefix(String sessionDaoKeyPrefix) {
        this.sessionDaoKeyPrefix = sessionDaoKeyPrefix;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public String getFilterUrls() {
        return filterUrls;
    }

    public void setFilterUrls(String filterUrls) {
        this.filterUrls = filterUrls;
    }

    public void setOtherCacheExpire(Integer otherCacheExpire) {
        this.otherCacheExpire = otherCacheExpire;
    }

    public Integer getOtherCacheExpire() {
        return otherCacheExpire;
    }

    public Map<String, String> getFilterChainDefinitions() {
        return filterChainDefinitions;
    }

    public void setFilterChainDefinitions(Map<String, String> filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }
}
