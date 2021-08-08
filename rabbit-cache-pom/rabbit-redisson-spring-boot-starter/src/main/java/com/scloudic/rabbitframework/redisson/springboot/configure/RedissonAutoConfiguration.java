package com.scloudic.rabbitframework.redisson.springboot.configure;

import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.scloudic.rabbitframework.redisson.RedisCache;
import com.scloudic.rabbitframework.redisson.spring.RedissonFactoryBean;

@Configuration
@ConditionalOnClass({RedissonClient.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RedissonAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(RedissonAutoConfiguration.class);

    @Bean(name = "redissonClient", destroyMethod = "shutdown")
    @ConditionalOnMissingBean(name = "redissonClient")
    public RedissonClient redissonClient() throws Exception {
        logger.debug("init RedissonClient");
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        Resource resource = resourcePatternResolver.getResource("redisson.yml");
        RedissonFactoryBean redissonFactoryBean = new RedissonFactoryBean();
        redissonFactoryBean.setConfigLocation(resource);
        return redissonFactoryBean.getObject();
    }

    @Bean(name = "redisCache")
    @DependsOn("redissonClient")
    @ConditionalOnMissingBean(name = "redisCache")
    public RedisCache redisCache(RedissonClient redissonClient) {
        logger.debug("init redisCache");
        RedisCache redisCache = new RedisCache();
        redisCache.setRedissonClient(redissonClient);
        return redisCache;
    }
}
