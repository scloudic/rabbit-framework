package com.rabbitframework.security.cache.redisson;

import java.io.Serializable;
import java.util.Collection;

import com.rabbitframework.security.SecurityUser;
import com.tjzq.commons.utils.StringUtils;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.CacheManagerAware;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.CollectionUtils;
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
        String userId = null;
        PrincipalCollection principalCollection = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if (principalCollection == null || CollectionUtils.isEmpty(principalCollection)) {
            //忽略
        } else {
            SecurityUser securityUser = (SecurityUser) principalCollection.getPrimaryPrincipal();
            userId = securityUser.getUserId();
            logger.debug("打印用户信息主键：" + userId);
            cache.remove(getKey(userId));
        }
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
        //同时以用户主键保存session信息以便于后期删除
        PrincipalCollection principalCollection = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if (principalCollection == null || CollectionUtils.isEmpty(principalCollection)) {
            //忽略
        } else {
            SecurityUser securityUser = (SecurityUser) principalCollection.getPrimaryPrincipal();
            String userId = securityUser.getUserId();
            logger.debug("打印用户信息主键：" + userId);
            cache.put(getKey(userId), session);
        }
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

    private Session getSessionByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            logger.warn("get session by user id is null");
            return null;
        }
        RedisCache<String, Session> cache = getActiveSessionsCacheLazy();
        if (cache == null) {
            logger.warn("cache is null");
            return null;
        }
        return cache.get(userId);
    }

    /**
     * 删除用户所有session信息
     *
     * @param userId
     * @throws UnknownSessionException
     */
    @Override
    public void doDelete(String userId, String keyPrefix) throws UnknownSessionException {
        logger.debug("执行清除用户session操作");
        if (keyPrefix == null) {
            keyPrefix = "";
        }
        keyPrefix = keyPrefix + ":";
        if (StringUtils.isBlank(userId)) {
            logger.error("user id  is null");
            return;
        }
        RedisCache<String, Session> cache = getActiveSessionsCacheLazy();
        if (cache == null) {
            logger.warn("获取当前缓存为空!");
            return;
        }
        Session session = getSessionByUserId(keyPrefix + userId);
        if (session == null) {
            logger.warn("获取缓存session为空,该用户可能已清除");
            return;
        }
        cache.remove(keyPrefix + userId);
        cache.remove(keyPrefix + session.getId());
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
