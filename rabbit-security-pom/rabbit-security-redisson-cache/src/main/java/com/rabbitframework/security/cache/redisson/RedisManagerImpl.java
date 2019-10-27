package com.rabbitframework.security.cache.redisson;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RBinaryStream;
import org.redisson.api.RedissonClient;

public class RedisManagerImpl implements RedisManager {
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
		set(key, value, 0);
		return value;
	}

	@Override
	public byte[] set(String key, byte[] value, int expire) {
		try {
			RBinaryStream binaryStream = redissonClient.getBinaryStream(key);
			if (expire != 0) {
				binaryStream.set(value, expire, TimeUnit.SECONDS);
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
			binaryStream.delete();
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
		return null;
		// RKeys rKeys = redissonClient.getKeys();
		// Iterable<String> iterable = rKeys.getKeysByPattern(pattern);
		// Set<String> keys = new HashSet<String>();
		// if (iterable == null) {
		// return keys;
		// }
		// Iterator<String> iterator = iterable.iterator();
		// if (iterator == null) {
		// return keys;
		// }
		// while (iterator.hasNext()) {
		// String string = (String) iterator.next();
		// keys.add(string);
		// }
		// return keys;
	}

}
