package com.scloudic.rabbitframework.jedis.impl;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scloudic.rabbitframework.jedis.RedisCache;
import com.scloudic.rabbitframework.jedis.RedisException;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.SetParams;

public class JedisClusterCacheImpl implements RedisCache<JedisCluster> {
    private static final Logger logger = LoggerFactory.getLogger(JedisClusterCacheImpl.class);
    private JedisCluster jedisCluster;

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        Set<Tuple> tuples = null;
        try {
            tuples = jedisCluster.zrangeByScoreWithScores(key, min, max);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        }
        return tuples;
    }

    /**
     * @param key
     * @return
     */
    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key) {
        Set<Tuple> tuples = null;
        try {
            tuples = jedisCluster.zrangeByScoreWithScores(key, "-inf", "+inf");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        }
        return tuples;
    }

    @Override
    public void set(String key, String value) {
        try {
            jedisCluster.set(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        }
    }

    @Override
    public void hset(String key, String field, String value) {
        try {
            jedisCluster.hset(key, field, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        }
    }

    @Override
    public String hget(String key, String field) {
        String value = "";
        try {
            value = jedisCluster.hget(key, field);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        }
        return value;
    }

    @Override
    public Long hdel(String key, String... field) {
        Long value = 0L;
        try {
            value = jedisCluster.hdel(key, field);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        }
        return value;
    }

    public Long incr(String key) {
        Long value = 0L;
        try {
            value = jedisCluster.incr(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        }
        return value;
    }

    public String get(String key) {
        String value = "";
        try {
            value = jedisCluster.get(key);
            if (null == value) {
                value = "";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        }
        return value;
    }

    @Override
    public void del(String key) {
        try {
            jedisCluster.del(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        }
    }

    @Override
    public void set(String key, String value, int expire) {
        try {
            jedisCluster.set(key, value);
            if (expire > 0) {
                jedisCluster.expire(key, expire);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        }
    }

    @Override
    public Long setnx(String key, String value) {
        try {
            return jedisCluster.setnx(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        }
    }

    @Override
    public String setnxex(String key, String value, int expire) {
        try {
            return jedisCluster.set(key, value, SetParams.setParams().nx().ex(expire));
//			return jedisCluster.set(key, value, "NX", "EX", expire);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        }
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public JedisCluster getJedis() {
        return this.jedisCluster;
    }

    @Override
    public void close(JedisCluster jedis) {
        close();
    }

    public void close() {
        jedisCluster.close();
    }
}
