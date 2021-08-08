package com.scloudic.rabbitframework.security.cache.redisson;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RBinaryStream;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisManagerImpl implements RedisManager {
    private static final Logger logger = LoggerFactory.getLogger(RedisManagerImpl.class);
    private RedissonClient redissonClient;
    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public byte[] get(String key) {
        try {
            RBinaryStream binaryStream = redissonClient.getBinaryStream(key);
            return binaryStream.get();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] set(String key, byte[] value) {
        set(key, value, 0L);
        return value;
    }

    @Override
    public byte[] set(String key, byte[] value, long expire) {
        try {
            RBinaryStream binaryStream = redissonClient.getBinaryStream(key);
            if (expire != 0) {
                binaryStream.set(value, expire, TimeUnit.MILLISECONDS);
            } else {
                binaryStream.set(value);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return value;
    }

    /**
     * del
     *
     * @param key
     */
    @Override
    public void del(String key) {
        try {
            RBinaryStream binaryStream = redissonClient.getBinaryStream(key);
            boolean result = binaryStream.delete();
            logger.debug("del [" + key + "],result=" + result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void flushDB() {

    }

    /**
     * size
     */
    @Override
    public Long dbSize() {
        Long dbSize = 0L;
        return dbSize;
    }

    /**
     * keys
     *
     * @param pattern
     * @return
     */
    @Override
    public Set<String> keys(String pattern) {
        RKeys rKeys = redissonClient.getKeys();
        Iterable<String> iterable = rKeys.getKeysByPattern(pattern);
        Set<String> keys = new HashSet<String>();
        if (iterable == null) {
            return keys;
        }
        Iterator<String> iterator = iterable.iterator();
        if (iterator == null) {
            return keys;
        }
        while (iterator.hasNext()) {
            String string = (String) iterator.next();
            keys.add(string);
        }
        return keys;
    }

}
