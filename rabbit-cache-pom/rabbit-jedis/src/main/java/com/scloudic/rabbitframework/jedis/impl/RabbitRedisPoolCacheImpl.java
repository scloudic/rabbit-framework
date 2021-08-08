package com.scloudic.rabbitframework.jedis.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scloudic.rabbitframework.jedis.RabbitRedisPool;
import com.scloudic.rabbitframework.jedis.RedisCache;
import com.scloudic.rabbitframework.jedis.RedisException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.SetParams;

import java.util.Set;

/**
 * 非切片连接池方式
 *
 * @author: justin
 */
public class RabbitRedisPoolCacheImpl implements RedisCache<Jedis> {
    private static final Logger logger = LoggerFactory.getLogger(RabbitRedisPoolCacheImpl.class);
    private RabbitRedisPool rabbitRedisPool;

    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        Set<Tuple> tuples = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            tuples = jedis.zrangeByScoreWithScores(key, min, max);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
        return tuples;
    }


    public Set<Tuple> zrangeByScoreWithScores(String key) {
        Set<Tuple> tuples = null;
        Jedis jedis = null;
        try {
            jedis = getJedis();
            tuples = jedis.zrangeByScoreWithScores(key, "-inf", "+inf");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
        return tuples;
    }

    @Override
    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
    }

    public void hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.hset(key, field, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String hget(String key, String field) {
        Jedis jedis = null;
        String value = "";
        try {
            jedis = getJedis();
            value = jedis.hget(key, field);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
        return value;
    }

    @Override
    public Long hdel(String key, String... field) {
        Jedis jedis = null;
        Long value = 0L;
        try {
            jedis = getJedis();
            value = jedis.hdel(key, field);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
        return value;
    }

    public Long incr(String key) {
        Jedis jedis = null;
        Long value = 0L;
        try {
            jedis = getJedis();
            value = jedis.incr(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
        return value;
    }

    public String get(String key) {
        Jedis jedis = null;
        String value = "";
        try {
            jedis = getJedis();
            value = jedis.get(key);
            if (null == value) {
                value = "";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
        return value;
    }

    public void del(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.del(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
    }

    @Override
    public void set(String key, String value, int expire) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.set(key, value);
            if (expire != 0) {
                jedis.expire(key, expire);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
    }

    @Override
    public Long setnx(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.setnx(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
    }

    @Override
    public String setnxex(String key, String value, int expire) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.set(key, value, SetParams.setParams().nx().ex(expire));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(jedis);
        }
    }

    public void setRabbitRedisPool(RabbitRedisPool rabbitRedisPool) {
        this.rabbitRedisPool = rabbitRedisPool;
    }

    public Jedis getJedis() {
        return rabbitRedisPool.getResource();
    }

    @Override
    public void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

}
