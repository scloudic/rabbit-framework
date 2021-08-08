package com.scloudic.rabbitframework.jedis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisPool;

/**
 * jdeis缓存池
 *
 * @author: justin
 */
public class RabbitRedisPool extends JedisPool {

    public RabbitRedisPool(final GenericObjectPoolConfig poolConfig, final String host, int port,
                           int timeout, final String password) {
        super(poolConfig, host, port, timeout, password);
    }
}
