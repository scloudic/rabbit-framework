package com.rabbitframework.security.cache.redisson;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.CacheManagerAware;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.security.web.session.mgt.AbstractRabbitSessionDAO;

public class RedisSessionDAO extends AbstractRabbitSessionDAO implements CacheManagerAware {
	private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);
	public static final String ACTIVE_SESSION_CACHE_NAME = "security-activeSessionCache";
	private CacheManager cacheManager;
	private String keyPrefix = "session:";
	private RedisCache<String, Session> activeSessions = null;

	@Override
	public void delete(Session session) {
		if (session == null || session.getId() == null) {
			logger.error("session or session id is null");
			return;
		}
		RedisCache<String, Session> cache = getActiveSessionsCacheLazy();
		if (cache == null) {
			logger.warn("cache is null");
			return;
		}
		cache.remove(getKey(session.getId()));
	}

	@Override
	public Collection<Session> getActiveSessions() {
		RedisCache<String, Session> cache = getActiveSessionsCacheLazy();
		if (cache == null) {
			logger.warn("cache is null");
			return null;
		}
		return cache.values();
	}

	@Override
	public void doSave(Session session) {
		if (session == null || session.getId() == null) {
			logger.error("session or session id is null");
			return;
		}
		RedisCache<String, Session> cache = getActiveSessionsCacheLazy();
		if (cache == null) {
			logger.warn("cache is null");
			return;
		}
		session.setTimeout(cache.getExpire() * 1000);
		cache.put(getKey(session.getId()), session);
	}

	@Override
	protected Session doReadSession(Serializable sessionId) {
		if (sessionId == null) {
			logger.error("session id is null");
			return null;
		}
		RedisCache<String, Session> cache = getActiveSessionsCacheLazy();
		if (cache == null) {
			logger.warn("cache is null");
			return null;
		}
		return cache.get(getKey(sessionId));
	}

	@Override
	public void doUpdate(Session session) throws UnknownSessionException {
		doSave(session);
	}

	private RedisCache<String, Session> getActiveSessionsCacheLazy() {
		if (this.activeSessions == null) {
			this.activeSessions = createActiveSessionsCache();
		}
		return activeSessions;
	}

	private String getKey(Serializable key) {
		return keyPrefix + key;
	}

	protected RedisCache<String, Session> createActiveSessionsCache() {
		RedisCache<String, Session> cache = null;
		CacheManager mgr = getCacheManager();
		if (mgr != null) {
			cache = (RedisCache) mgr.getCache(ACTIVE_SESSION_CACHE_NAME);
		}
		return cache;
	}

	public String getKeyPrefix() {
		return keyPrefix;
	}

	public void setKeyPrefix(String keyPrefix) {
		this.keyPrefix = keyPrefix;
	}

	@Override
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}
}
