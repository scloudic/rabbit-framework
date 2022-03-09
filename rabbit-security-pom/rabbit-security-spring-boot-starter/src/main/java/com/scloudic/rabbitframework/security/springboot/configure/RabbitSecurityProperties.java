package com.scloudic.rabbitframework.security.springboot.configure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = RabbitSecurityProperties.RABBIT_SECURITY_PREFIX)
public class RabbitSecurityProperties {
    public static final String RABBIT_SECURITY_PREFIX = "rabbit.security";
    private String filterUrls;
    private Map<String, String> filterChainDefinitions;
    //如果使用servlet此配置无效
    private CookieProperties cookie = null;
    private boolean tokenEnabled = true;
    private boolean sessionIdCookieEnabled = true;
    private String tokenName = "Authorization";
    private String sessionDaoKeyPrefix = "rabbit_session";
    private SessionType sessionType = SessionType.servlet;
    private Long cacheSessionExpire = 604800L * 1000;
    private Long otherCacheExpire = 600L * 1000;
    private CacheType cacheType = CacheType.memory;
    private boolean singleUser = false;
    //是否开启session验证定时任务
    private boolean sessionValidationSchedulerEnabled = false;
    private Map<String, FilterProperties> filters = new HashMap<String, FilterProperties>();
    private List<String> realmBeanNames = new ArrayList<String>();

    public List<String> getRealmBeanNames() {
        return realmBeanNames;
    }

    public void setRealmBeanNames(List<String> realmBeanNames) {
        this.realmBeanNames = realmBeanNames;
    }

    public Long getCacheSessionExpire() {
        return cacheSessionExpire;
    }

    public void setCacheSessionExpire(Long cacheSessionExpire) {
        this.cacheSessionExpire = cacheSessionExpire;
    }

    public String getSessionDaoKeyPrefix() {
        return sessionDaoKeyPrefix;
    }

    public void setSessionDaoKeyPrefix(String sessionDaoKeyPrefix) {
        this.sessionDaoKeyPrefix = sessionDaoKeyPrefix;
    }

    public String getFilterUrls() {
        return filterUrls;
    }

    public void setFilterUrls(String filterUrls) {
        this.filterUrls = filterUrls;
    }

    public void setOtherCacheExpire(Long otherCacheExpire) {
        this.otherCacheExpire = otherCacheExpire;
    }

    public Long getOtherCacheExpire() {
        return otherCacheExpire;
    }

    public Map<String, String> getFilterChainDefinitions() {
        return filterChainDefinitions;
    }

    public void setFilterChainDefinitions(Map<String, String> filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

    public CookieProperties getCookie() {
        return cookie;
    }

    public void setCookie(CookieProperties cookie) {
        this.cookie = cookie;
    }

    public void setTokenEnabled(boolean tokenEnabled) {
        this.tokenEnabled = tokenEnabled;
    }

    public boolean isTokenEnabled() {
        return tokenEnabled;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public void setSessionIdCookieEnabled(boolean sessionIdCookieEnabled) {
        this.sessionIdCookieEnabled = sessionIdCookieEnabled;
    }

    public boolean isSessionIdCookieEnabled() {
        return sessionIdCookieEnabled;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public CacheType getCacheType() {
        return cacheType;
    }

    public void setCacheType(CacheType cacheType) {
        this.cacheType = cacheType;
    }


    public boolean isSessionValidationSchedulerEnabled() {
        return sessionValidationSchedulerEnabled;
    }

    public void setSessionValidationSchedulerEnabled(boolean sessionValidationSchedulerEnabled) {
        this.sessionValidationSchedulerEnabled = sessionValidationSchedulerEnabled;
    }

    public Map<String, FilterProperties> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, FilterProperties> filters) {
        this.filters = filters;
    }

    public boolean isSingleUser() {
        return singleUser;
    }

    public void setSingleUser(boolean singleUser) {
        this.singleUser = singleUser;
    }

    /**
     * session管理类型
     * local:本地默认session
     * servlet: 容器session
     *
     * @since 3.3.1
     */
    public enum SessionType {
        local, servlet
    }

    public enum CacheType {
        memory, redis
    }
}
