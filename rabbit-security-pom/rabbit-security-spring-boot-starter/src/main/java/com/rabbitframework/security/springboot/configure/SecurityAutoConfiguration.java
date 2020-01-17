package com.rabbitframework.security.springboot.configure;

import com.rabbitframework.security.cache.redisson.RedisCacheManager;
import com.rabbitframework.security.cache.redisson.RedisManager;
import com.rabbitframework.security.cache.redisson.RedisManagerImpl;
import com.rabbitframework.security.cache.redisson.RedisSessionDAO;
import com.rabbitframework.security.realm.EmptyRealm;
import com.rabbitframework.security.realm.SecurityAuthorizingRealm;
import com.rabbitframework.security.spring.interceptor.SecurityAuthorizationAttributeSourceAdvisor;
import com.rabbitframework.security.web.mgt.SimpleWebSecurityManager;
import com.rabbitframework.security.web.session.mgt.SimpleWebSessionManager;
import com.tjzq.redisson.springboot.configure.RedissonAutoConfiguration;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.ArrayList;
import java.util.List;

/**
 * spring boot自动初始化加载
 *
 * @author justin.liang
 */
@Configuration
@AutoConfigureAfter({RedissonAutoConfiguration.class, LifecycleAutoConfiguration.class})
@EnableConfigurationProperties(RabbitSecurityProperties.class)
public class SecurityAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SecurityAutoConfiguration.class);
    private final RabbitSecurityProperties rabbitSecurityProperties;
    @Autowired
    private RedissonClient redissonClient;
    private ApplicationContext applicationContext;

    public SecurityAutoConfiguration(ApplicationContext applicationContext,
                                     RabbitSecurityProperties rabbitSecurityProperties) {
        this.applicationContext = applicationContext;
        this.rabbitSecurityProperties = rabbitSecurityProperties;
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
        simpleWebSecurityManager.setSessionManager(securityWebSessionManager());
        simpleWebSecurityManager.setCacheManager(rabbitRedisCacheManager());
        simpleWebSecurityManager.setRealms(realms);
        return simpleWebSecurityManager;
    }

    @Bean("securityWebSessionManager")
    @ConditionalOnMissingBean
    @DependsOn(value = {"redisSessionDAO"})
    protected SimpleWebSessionManager securityWebSessionManager() {
        SimpleWebSessionManager simpleWebSessionManager = new SimpleWebSessionManager();
        simpleWebSessionManager.setCacheManager(rabbitRedisCacheManager());
        simpleWebSessionManager.setSessionDAO(redisSessionDAO());
        return simpleWebSessionManager;
    }

    /**
     * 缓存管理器
     *
     * @return
     */
    @Bean("rabbitRedisCacheManager")
    @ConditionalOnMissingBean
    protected RedisCacheManager rabbitRedisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        Integer sessionExpire = rabbitSecurityProperties.getCacheSessionExpire();
        redisCacheManager.setSessionExpire(sessionExpire.intValue());
        redisCacheManager.setOtherExpire(rabbitSecurityProperties.getOtherCacheExpire().intValue());
        return redisCacheManager;
    }

    /**
     * redis管理实现类
     *
     * @return
     */
    @Bean("redisManager")
    @ConditionalOnMissingBean
    protected RedisManager redisManager() {
        RedisManagerImpl redisManager = new RedisManagerImpl();
        redisManager.setRedissonClient(redissonClient);
        return redisManager;
    }

    /**
     * sessionDao初始化实例
     *
     * @return
     */
    @Bean("redisSessionDAO")
    @ConditionalOnMissingBean
    protected RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        String keyPrefix = rabbitSecurityProperties.getSessionDaoKeyPrefix();
        redisSessionDAO.setKeyPrefix(keyPrefix + ":");
        redisSessionDAO.setCacheManager(rabbitRedisCacheManager());
        return redisSessionDAO;
    }

    @Bean
    @ConditionalOnMissingBean
    protected SecurityAuthorizationAttributeSourceAdvisor securityAuthorizationAttributeSourceAdvisor() {
        SecurityAuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor =
                new SecurityAuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }
}
