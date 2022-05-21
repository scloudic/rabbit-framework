package com.scloudic.rabbitframework.redisson.spring;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import java.io.InputStream;

/**
 * redisson的spring bean工厂类
 *
 * @author justin.liang
 */
public class RedissonFactoryBean implements FactoryBean<RedissonClient>, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(RedissonFactoryBean.class);
    private RedissonClient redissonClient;
    private Resource configLocation;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.redissonClient = buildRedissonClient();
    }

    @Override
    public RedissonClient getObject() throws Exception {
        if (this.redissonClient == null) {
            afterPropertiesSet();
        }
        return this.redissonClient;
    }

    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
    }

    @Override
    public Class<? extends RedissonClient> getObjectType() {
        return this.redissonClient == null ? RedissonClient.class : this.redissonClient.getClass();
    }

    public RedissonClient buildRedissonClient() throws Exception {
        InputStream inputStream = null;
        try {
            inputStream = configLocation.getInputStream();
            return Redisson.create(Config.fromYAML(inputStream));
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {

                }
            }
        }
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void destroy() {
        if (redissonClient != null) {
            try {
                redissonClient.shutdown();
            } catch (Exception e) {
                // TODO 忽略
                logger.warn(e.getMessage(), e);
            }
        }
    }
}
