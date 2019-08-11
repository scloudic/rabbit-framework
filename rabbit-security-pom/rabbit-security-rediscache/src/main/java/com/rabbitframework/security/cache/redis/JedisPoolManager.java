package com.rabbitframework.security.cache.redis;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisPoolManager implements RedisManager {
	private JedisPool jedisPool = null;

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	@Override
	public byte[] get(byte[] key) {
		byte[] value = null;
		Jedis jedis = jedisPool.getResource();
		try {
			value = jedis.get(key);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			jedis.close();
		}
		return value;
	}

	@Override
	public byte[] set(byte[] key, byte[] value) {
		set(key, value, 0);
		return value;
	}

	@Override
	public byte[] set(byte[] key, byte[] value, int expire) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.set(key, value);
			if (expire != 0) {
				jedis.expire(key, expire);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			jedis.close();
		}
		return value;
	}

	@Override
	public void del(byte[] key) {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.del(key);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			jedis.close();
		}
	}

	@Override
	public void flushDB() {
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.flushDB();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			jedis.close();
		}
	}

	/**
	 * size
	 */
	@Override
	public Long dbSize() {
		Long dbSize = 0L;
		Jedis jedis = jedisPool.getResource();
		try {
			dbSize = jedis.dbSize();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			jedis.close();
		}
		return dbSize;
	}

	@Override
	public Set<byte[]> keys(String pattern) {
		Set<byte[]> keys = null;
		Jedis jedis = jedisPool.getResource();
		try {
			keys = jedis.keys(pattern.getBytes());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			jedis.close();
		}
		return keys;
	}
}
