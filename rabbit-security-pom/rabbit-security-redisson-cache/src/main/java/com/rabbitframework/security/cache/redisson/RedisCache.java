package com.rabbitframework.security.cache.redisson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * redis缓存实现
 *
 * @param <K>
 * @param <V>
 */
public class RedisCache<K, V> implements Cache<K, V> {
	private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);
	private RedisManager cache;
	private String keyPrefix = "security_redis_cache:";
	private int expire = 0;

	/**
	 * 通过一个JedisManager实例构造RedisCache
	 */
	public RedisCache(RedisManager cache) {
		if (cache == null) {
			throw new IllegalArgumentException("Cache argument cannot be null.");
		}
		this.cache = cache;
	}

	public RedisCache(RedisManager cache, int expire) {
		this(cache);
		this.expire = expire;
	}

	/**
	 * 获得byte[]型的key
	 *
	 * @param key
	 * @return
	 */
	private String getKey(K key) {
		if (key instanceof String) {
			String preKey = this.keyPrefix + key;
			return preKey;
		} else {
			return new String(SerializeUtils.serialize(key));
		}
	}

	@Override
	public V get(K key) throws CacheException {
		logger.debug("redis get [" + key + "]");
		try {
			if (key == null) {
				return null;
			} else {
				byte[] rawValue = cache.get(getKey(key));
				V value = (V) SerializeUtils.deserialize(rawValue);
				return value;
			}
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	@Override
	public V put(K key, V value) throws CacheException {
		logger.debug("redis put key [" + key + "]");
		try {
			cache.set(getKey(key), SerializeUtils.serialize(value), expire);
			return value;
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	@Override
	public V remove(K key) throws CacheException {
		logger.debug("redis del key [" + key + "]");
		try {
			V previous = get(key);
			cache.del(getKey(key));
			return previous;
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	@Override
	public void clear() throws CacheException {
		logger.debug("clear redis all data!");
		try {
			synchronized (this) {
				Set<String> keys = cache.keys(this.keyPrefix + "*");
				if (!CollectionUtils.isEmpty(keys)) {
					for (String key : keys) {
						cache.del(key);
					}
				}
			}
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	@Override
	public int size() {
		try {
			Long longSize = new Long(cache.dbSize());
			logger.debug("cacheSize:" + longSize);
			return longSize.intValue();
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	@Override
	public Set keys() {
		try {
			Set<String> keys = cache.keys(this.keyPrefix + "*");
			if (CollectionUtils.isEmpty(keys)) {
				return Collections.emptySet();
			} else {
				Set newKeys = new HashSet();
				for (String key : keys) {
					newKeys.add((K) key);
				}
				return newKeys;
			}
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	@Override
	public Collection<V> values() {
		try {
			Set<String> keys = cache.keys(this.keyPrefix + "*");
			if (!CollectionUtils.isEmpty(keys)) {
				List<V> values = new ArrayList<V>(keys.size());
				for (String key : keys) {
					V value = get((K) key);
					if (value != null) {
						values.add(value);
					}
				}
				return Collections.unmodifiableList(values);
			} else {
				return Collections.emptyList();
			}
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}
}
