package com.scloudic.rabbitframework.security.springboot.configure;

import com.scloudic.rabbitframework.security.cache.redisson.RedisSessionDAO;
import com.scloudic.rabbitframework.security.mgt.SubjectDAOImpl;
import com.scloudic.rabbitframework.security.realm.EmptyRealm;
import com.scloudic.rabbitframework.security.realm.SecurityAuthorizingRealm;
import com.scloudic.rabbitframework.security.web.mgt.SimpleWebSecurityManager;
import com.scloudic.rabbitframework.security.web.servlet.SecurityWebCookie;
import com.scloudic.rabbitframework.security.web.session.SecurityWebSessionManager;
import com.scloudic.rabbitframework.core.utils.StringUtils;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.event.EventBus;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.web.session.mgt.ServletContainerSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限管理web自动加载初始化
 *
 * @since 3.3.1
 */
@Configuration
@AutoConfigureAfter({SecurityBeanAutoConfiguration.class})
@EnableConfigurationProperties(RabbitSecurityProperties.class)
public class SecurityWebAutoConfiguration {
    @Autowired(required = false)
    private CacheManager cacheManager;
    private final RabbitSecurityProperties rabbitSecurityProperties;
    private ApplicationContext applicationContext;
    @Autowired
    private EventBus eventBus;

    public SecurityWebAutoConfiguration(ApplicationContext applicationContext,
                                        RabbitSecurityProperties rabbitSecurityProperties) {
        this.applicationContext = applicationContext;
        this.rabbitSecurityProperties = rabbitSecurityProperties;
    }

    /**
     * sessionDao初始化实例
     *
     * @return
     */
    @Bean("sessionDAO")
    @ConditionalOnMissingBean
    protected SessionDAO sessionDAO() {
        SessionDAO sessionDAO = null;
        RabbitSecurityProperties.CacheType cacheType = rabbitSecurityProperties.getCacheType();
        switch (cacheType) {
            case redis:
                sessionDAO = new RedisSessionDAO();
                String keyPrefix = rabbitSecurityProperties.getSessionDaoKeyPrefix();
                ((RedisSessionDAO) sessionDAO).setSingleUser(rabbitSecurityProperties.isSingleUser());
                ((RedisSessionDAO) sessionDAO).setKeyPrefix(keyPrefix + ":");
                ((RedisSessionDAO) sessionDAO).setCacheManager(cacheManager);
                break;
            case memory:
                sessionDAO = new MemorySessionDAO();
                break;
        }
        return sessionDAO;
    }

    @Bean("securityWebCookie")
    @ConditionalOnMissingBean
    protected SecurityWebCookie securityWebCookie() {
        SecurityWebCookie securityWebCookie = new SecurityWebCookie();
        CookieProperties cookieProperties = rabbitSecurityProperties.getCookie();
        if (cookieProperties != null) {
            setCookie(cookieProperties, securityWebCookie);
        }
        return securityWebCookie;
    }

    private void setCookie(CookieProperties cookieProperties, SecurityWebCookie securityWebCookie) {
        securityWebCookie.setHttpOnly(cookieProperties.isHttpOnly());
        securityWebCookie.setMaxAge(cookieProperties.getMaxAge());
        securityWebCookie.setSecure(cookieProperties.isSecure());
        securityWebCookie.setVersion(cookieProperties.getVersion());
        securityWebCookie.setName(cookieProperties.getName());
        if (StringUtils.isNotBlank(cookieProperties.getValue())) {
            securityWebCookie.setValue(cookieProperties.getValue());
        }
        if (StringUtils.isNotBlank(cookieProperties.getComment())) {
            securityWebCookie.setComment(cookieProperties.getComment());
        }
        if (StringUtils.isNotBlank(cookieProperties.getDomain())) {
            securityWebCookie.setDomain(cookieProperties.getDomain());
        }
        if (StringUtils.isNotBlank(cookieProperties.getPath())) {
            securityWebCookie.setPath(cookieProperties.getPath());
        }
    }

    @Bean("sessionManager")
    @ConditionalOnMissingBean
    protected SessionManager sessionManager() {
        if (rabbitSecurityProperties.getSessionType() == RabbitSecurityProperties.SessionType.local) {
            SecurityWebSessionManager simpleWebSessionManager = new SecurityWebSessionManager();
            simpleWebSessionManager.setSessionIdCookie(securityWebCookie());
            simpleWebSessionManager.setTokenEnabled(rabbitSecurityProperties.isTokenEnabled());
            simpleWebSessionManager.setTokenName(rabbitSecurityProperties.getTokenName());
            simpleWebSessionManager.setSessionIdCookieEnabled(rabbitSecurityProperties.isSessionIdCookieEnabled());
            simpleWebSessionManager.setSessionValidationSchedulerEnabled(rabbitSecurityProperties.isSessionValidationSchedulerEnabled());
            simpleWebSessionManager.setGlobalSessionTimeout(rabbitSecurityProperties.getCacheSessionExpire().longValue());
            simpleWebSessionManager.setSessionValidationInterval(rabbitSecurityProperties.getCacheSessionExpire().longValue());
            simpleWebSessionManager.setSessionDAO(sessionDAO());
            return simpleWebSessionManager;
        }
        return new ServletContainerSessionManager();
    }

    @Bean("securityManager")
    @ConditionalOnMissingBean
    protected SecurityManager securityManager() {
        List<String> realmsList = rabbitSecurityProperties.getRealmBeanNames();
        List<Realm> realms = new ArrayList<Realm>();
        if (realmsList.size() == 0) {
            realms.add(new EmptyRealm());
        } else {
            realmsList.forEach((name) -> {
                SecurityAuthorizingRealm securityAuthorizingRealm = (SecurityAuthorizingRealm) applicationContext
                        .getBean(name);
                realms.add(securityAuthorizingRealm);
            });
        }
        SimpleWebSecurityManager simpleWebSecurityManager = new SimpleWebSecurityManager();
        simpleWebSecurityManager.setRealms(realms);
        simpleWebSecurityManager.setEventBus(eventBus);
        if (cacheManager != null) {
            simpleWebSecurityManager.setCacheManager(cacheManager);
        }
        simpleWebSecurityManager.setSessionManager(sessionManager());
        simpleWebSecurityManager.setSubjectDAO(new SubjectDAOImpl());
        return simpleWebSecurityManager;
    }


}
