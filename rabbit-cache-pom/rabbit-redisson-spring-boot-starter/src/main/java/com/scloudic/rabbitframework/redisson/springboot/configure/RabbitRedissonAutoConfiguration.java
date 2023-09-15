package com.scloudic.rabbitframework.redisson.springboot.configure;

import com.scloudic.rabbitframework.redisson.RedisCache;
import com.scloudic.rabbitframework.redisson.aop.RedissonLockInterceptor;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
@AutoConfigureAfter(value = RedissonAutoConfiguration.class)
public class RabbitRedissonAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(RabbitRedissonAutoConfiguration.class);
    @Autowired
    private RedissonClient redissonClient;
    @Bean(name = "redisCache")
    @ConditionalOnMissingBean(name = "redisCache")
    public RedisCache redisCache() {
        logger.debug("init redisCache");
        RedisCache redisCache = new RedisCache();
        redisCache.setRedissonClient(redissonClient);
        return redisCache;
    }

    @Bean
    @DependsOn("redisCache")
    @ConditionalOnMissingBean
    public RedissonLockInterceptor redissonLockInterceptor(RedisCache redisCache) {
        RedissonLockInterceptor redissonLockInterceptor = new RedissonLockInterceptor();
        redissonLockInterceptor.setRedisCache(redisCache);
        return redissonLockInterceptor;
    }
}
