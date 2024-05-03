package com.scloudic.rabbitframework.security.cache.redisson;

import com.scloudic.rabbitframework.core.utils.StringUtils;
import com.scloudic.rabbitframework.security.SecurityUser;
import com.scloudic.rabbitframework.security.web.session.AbstractSecuritySessionDAO;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.CacheManagerAware;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collection;

public class RedisSessionDAO extends AbstractSecuritySessionDAO implements CacheManagerAware {
    private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);
    public static final String ACTIVE_SESSION_CACHE_NAME = "security-activeSessionCache";
    private CacheManager cacheManager;
    private String keyPrefix = "session:";
    private boolean singleUser = false;
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
        if (isSingleUser()) {
            PrincipalCollection principalCollection = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (principalCollection != null && principalCollection.isEmpty()) {
                SecurityUser securityUser = (SecurityUser) principalCollection.getPrimaryPrincipal();
                String userId = securityUser.getUserId();
                cache.remove(getKey(userId));
            }
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
        session.setTimeout(cache.getExpireTime());
        cache.put(getKey(session.getId()), session);
        if (isSingleUser()) {
            PrincipalCollection principalCollection = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (principalCollection != null && !principalCollection.isEmpty()) {
                SecurityUser securityUser = (SecurityUser) principalCollection.getPrimaryPrincipal();
                String userId = securityUser.getUserId();
                Session userSession = getSessionByUserId(getKey(userId));
                if (userSession != null) {
                    delete(userSession);
                }
                cache.put(getKey(userId), session);
            }

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
        if (session == null || session.getId() == null) {
            logger.error("session or session id is null");
            return;
        }
        RedisCache<String, Session> cache = getActiveSessionsCacheLazy();
        if (cache == null) {
            logger.warn("cache is null");
            return;
        }
        session.setTimeout(cache.getExpireTime());
        cache.put(getKey(session.getId()), session);
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
    public void doDelete(String userId) throws UnknownSessionException {
        if (!isSingleUser()) {
            logger.warn("没有多用户存储数据");
        }
        logger.debug("执行清除用户session操作");
        if (StringUtils.isBlank(userId)) {
            logger.error("user id  is null");
            return;
        }
        RedisCache<String, Session> cache = getActiveSessionsCacheLazy();
        if (cache == null) {
            logger.warn("获取当前缓存为空!");
            return;
        }
        Session session = getSessionByUserId(getKey(userId));
        if (session == null) {
            logger.warn("获取缓存session为空,该用户可能已清除");
            return;
        }
        cache.remove(getKey(userId));
        cache.remove(getKey(session.getId()));
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

    public boolean isSingleUser() {
        return singleUser;
    }

    public void setSingleUser(boolean singleUser) {
        this.singleUser = singleUser;
    }

    @Override
    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }
}
