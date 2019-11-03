package com.rabbitframework.jade.cache.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.jade.cache.Cache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class EhcacheCache implements Cache {
	private static final Logger logger = LoggerFactory.getLogger(EhcacheCache.class);
	/**
	 * The cache manager reference.
	 */
	private static final CacheManager CACHE_MANAGER = CacheManager.create();

	/**
	 * The cache id (namespace)
	 */
	private final String id;

	/**
	 * The cache instance
	 */
	private final Ehcache cache;

	/**
	 * @param id
	 */
	public EhcacheCache(final String id) {
		if (id == null) {
			throw new IllegalArgumentException("Cache instances require an ID");
		}
		if (!CACHE_MANAGER.cacheExists(id)) {
			CACHE_MANAGER.addCache(id);
		}
		this.cache = CACHE_MANAGER.getCache(id);
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	public void clear() {
		cache.removeAll();
	}

	/**
	 * {@inheritDoc}
	 */
	public String getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getObject(Object key) {
		try {
			Element cachedElement = cache.get(key);
			cache.flush();
			if (cachedElement == null) {
				return null;
			}
			Object result = cachedElement.getObjectValue();
			if (logger.isDebugEnabled() && result != null) {
				logger.debug("read data from cache");
			}
			return result;
		} catch (Exception e) {

		}
		return null;
	}

	// /**
	// * {@inheritDoc}
	// */
	// public ReadWriteLock getReadWriteLock() {
	// return readWriteLock;
	// }

	/**
	 * {@inheritDoc}
	 */
	public int getSize() {
		return cache.getSize();
	}

	/**
	 * {@inheritDoc}
	 */
	public void putObject(Object key, Object value) {
		try {
			cache.put(new Element(key, value));
			cache.flush();
		} catch (Exception e) {

		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Object removeObject(Object key) {
		try {
			Object obj = getObject(key);
			cache.remove(key);
			cache.flush();
			return obj;
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Cache)) {
			return false;
		}

		Cache otherCache = (Cache) obj;
		return id.equals(otherCache.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "EHCache {" + id + "}";
	}

	// private static class DummyReadWriteLock implements ReadWriteLock {
	//
	// private Lock lock = new DummyLock();
	//
	// public Lock readLock() {
	// return lock;
	// }
	//
	// public Lock writeLock() {
	// return lock;
	// }
	//
	// private static class DummyLock implements Lock {
	//
	// public void lock() {
	// }
	//
	// public void lockInterruptibly() throws InterruptedException {
	// }
	//
	// public boolean tryLock() {
	// return true;
	// }
	//
	// public boolean tryLock(long paramLong, TimeUnit paramTimeUnit)
	// throws InterruptedException {
	// return true;
	// }
	//
	// public void unlock() {
	// }
	//
	// public Condition newCondition() {
	// return null;
	// }
	// }
	//
	// }
}
