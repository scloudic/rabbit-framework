package com.rabbitframework.security.cache.redis;

import java.util.Set;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class ShardedJedisPoolManager implements RedisManager {
	private ShardedJedisPool shardedJedisPool;

	public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
		this.shardedJedisPool = shardedJedisPool;
	}

	@Override
	public byte[] get(byte[] key) {
		byte[] value = null;
		ShardedJedis shardedJedis = getShardedJedis();
		try {
			value = shardedJedis.get(key);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			shardedJedis.close();
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
		ShardedJedis shardedJedis = getShardedJedis();
		try {
			shardedJedis.set(key, value);
			if (expire != 0) {
				shardedJedis.expire(key, expire);
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			shardedJedis.close();
		}
		return value;
	}

	/**
	 * del
	 *
	 * @param key
	 */
	@Override
	public void del(byte[] key) {
		ShardedJedis shardedJedis = getShardedJedis();
		try {
			shardedJedis.del(key);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		} finally {
			shardedJedis.close();
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
	public Set<byte[]> keys(String pattern) {
		Set<byte[]> keys = null;
		return keys;
	}

	/**
	 * 获取分片{@link ShardedJedis}
	 *
	 * @return
	 */
	private ShardedJedis getShardedJedis() {
		ShardedJedis shardedJedis = shardedJedisPool.getResource();
		return shardedJedis;
	}
}
