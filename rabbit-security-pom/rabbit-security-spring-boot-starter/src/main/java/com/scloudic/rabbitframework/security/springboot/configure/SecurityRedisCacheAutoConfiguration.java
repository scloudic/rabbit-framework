package com.scloudic.rabbitframework.security.springboot.configure;

import com.scloudic.rabbitframework.security.cache.redisson.RedisCacheManager;
import com.scloudic.rabbitframework.security.cache.redisson.RedisManager;
import com.scloudic.rabbitframework.security.cache.redisson.RedisManagerImpl;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RabbitSecurityProperties.class)
@AutoConfigureAfter(name = "com.scloudic.rabbitframework.redisson.springboot.configure.RabbitRedissonAutoConfiguration")
public class SecurityRedisCacheAutoConfiguration {
    private final RabbitSecurityProperties rabbitSecurityProperties;
    @Autowired(required = false)
    private RedissonClient redissonClient;


    public SecurityRedisCacheAutoConfiguration(RabbitSecurityProperties rabbitSecurityProperties) {
        this.rabbitSecurityProperties = rabbitSecurityProperties;
    }

    /**
     * 缓存管理器
     *
     * @return
     */
    @Bean("cacheManager")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = RabbitSecurityProperties.RABBIT_SECURITY_PREFIX, name = "cache-type",
            havingValue = "redis", matchIfMissing = true)
    protected RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        long sessionExpire = rabbitSecurityProperties.getCacheSessionExpire().longValue();
        redisCacheManager.setSessionExpire(sessionExpire);
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
    @ConditionalOnProperty(prefix = RabbitSecurityProperties.RABBIT_SECURITY_PREFIX,
            name = "cache-type", havingValue = "redis", matchIfMissing = true)
    protected RedisManager redisManager() {
        RedisManagerImpl redisManager = new RedisManagerImpl();
        redisManager.setRedissonClient(redissonClient);
        return redisManager;
    }
}
