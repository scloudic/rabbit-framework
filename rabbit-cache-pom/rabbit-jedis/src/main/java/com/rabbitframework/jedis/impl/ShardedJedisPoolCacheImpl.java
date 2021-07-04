package com.rabbitframework.jedis.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.jedis.RedisCache;
import com.rabbitframework.jedis.RedisException;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.SetParams;

import java.util.Set;

/**
 * redis缓存管理实现类 使用redis的切片连接池实现 {@link ShardedJedisPool}
 *
 * @author: justin.liang
 * @date: 2017/1/22 17:57
 */
public class ShardedJedisPoolCacheImpl implements RedisCache<ShardedJedis> {
    private static final Logger logger = LoggerFactory.getLogger(ShardedJedisPoolCacheImpl.class);
    private ShardedJedisPool shardedJedisPool;

    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        Set<Tuple> tuples = null;
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getJedis();
            tuples = shardedJedis.zrangeByScoreWithScores(key, min, max);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(shardedJedis);
        }
        return tuples;
    }

    /**
     * @param key
     * @return
     */
    public Set<Tuple> zrangeByScoreWithScores(String key) {
        Set<Tuple> tuples = null;
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getJedis();
            tuples = shardedJedis.zrangeByScoreWithScores(key, "-inf", "+inf");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(shardedJedis);
        }
        return tuples;
    }

    @Override
    public void set(String key, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getJedis();
            shardedJedis.set(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(shardedJedis);
        }
    }

    public void hset(String key, String field, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getJedis();
            shardedJedis.hset(key, field, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(shardedJedis);
        }
    }

    @Override
    public String hget(String key, String field) {
        ShardedJedis shardedJedis = null;
        String value = "";
        try {
            shardedJedis = getJedis();
            value = shardedJedis.hget(key, field);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(shardedJedis);
        }
        return value;
    }

    @Override
    public Long hdel(String key, String... field) {
        ShardedJedis shardedJedis = null;
        Long value = 0L;
        try {
            shardedJedis = getJedis();
            value = shardedJedis.hdel(key, field);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(shardedJedis);
        }
        return value;
    }

    public Long incr(String key) {
        ShardedJedis shardedJedis = null;
        Long value = 0L;
        try {
            shardedJedis = getJedis();
            value = shardedJedis.incr(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(shardedJedis);
        }
        return value;
    }

    public String get(String key) {
        ShardedJedis shardedJedis = null;
        String value = "";
        try {
            shardedJedis = getJedis();
            value = shardedJedis.get(key);
            if (null == value) {
                value = "";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(shardedJedis);
        }
        return value;
    }

    public void del(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getJedis();
            shardedJedis.del(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(shardedJedis);
        }
    }

    @Override
    public void set(String key, String value, int expire) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getJedis();
            shardedJedis.set(key, value);
            if (expire != 0) {
                shardedJedis.expire(key, expire);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(shardedJedis);
        }
    }

    @Override
    public Long setnx(String key, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getJedis();
            return shardedJedis.setnx(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(shardedJedis);
        }
    }

    @Override
    public String setnxex(String key, String value, int expire) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = getJedis();
            return shardedJedis.set(key, value, SetParams.setParams().nx().ex(expire));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        } finally {
            close(shardedJedis);
        }
    }

    @Override
    public void close(ShardedJedis shardedJedis) {
        try {
            if (null == shardedJedis) {
                return;
            }
            shardedJedis.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RedisException(e.getMessage(), e);
        }
    }

    public ShardedJedisPool getShardedJedisPool() {
        return shardedJedisPool;
    }

    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

    @Override
    public ShardedJedis getJedis() {
        return shardedJedisPool.getResource();
    }
}
