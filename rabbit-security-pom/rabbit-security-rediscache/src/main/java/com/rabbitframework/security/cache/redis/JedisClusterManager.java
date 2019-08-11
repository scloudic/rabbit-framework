package com.rabbitframework.security.cache.redis;

import java.util.Set;
import redis.clients.jedis.JedisCluster;

public class JedisClusterManager implements RedisManager {
	private JedisCluster jedisCluster = null;

	public void setJedisCluster(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}

	@Override
	public byte[] get(byte[] key) {
		byte[] value = null;
		try {
			value = jedisCluster.get(key);
		} catch (Throwable e) {
			throw new RuntimeException(e);
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

		try {
			jedisCluster.set(key, value);
			if (expire != 0) {
				jedisCluster.expire(key, expire);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		return value;
	}

	@Override
	public void del(byte[] key) {
		try {
			jedisCluster.del(key);
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

	@Override
	public Set<byte[]> keys(String pattern) {
		Set<byte[]> keys = null;
		return keys;
	}
}
