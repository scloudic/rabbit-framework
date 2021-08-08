package com.scloudic.rabbitframework.security.realm;

import com.scloudic.rabbitframework.core.exceptions.BizException;
import com.scloudic.rabbitframework.security.SecurityUtils;
import com.scloudic.rabbitframework.security.SecurityUser;
import com.scloudic.rabbitframework.security.web.mgt.SimpleWebSecurityManager;
import com.scloudic.rabbitframework.security.web.session.AbstractSecuritySessionDAO;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 授权realm
 *
 * @author: justin.liang
 */
public abstract class SecurityAuthorizingRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(SecurityAuthorizingRealm.class);
    private static final String DEFAULT_CACHE_KEY_PREFIX = "security_realm_key:";
    protected String cacheKeyPrefix = DEFAULT_CACHE_KEY_PREFIX;
    private String authz_key = "authz:";

    public SecurityAuthorizingRealm() {
        super();
        setName("securityRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Object userObject = getAvailablePrincipal(principals);
        if (userObject == null) {
            throw new BizException("not.login");
        }
        SecurityUser securityUser = (SecurityUser) userObject;
        return executeGetAuthorizationInfo(securityUser);
    }

    /**
     * 执行权限操作，获取权限信息,在配有缓存时只调用一次
     *
     * @param securityUser securityUser
     * @return AuthorizationInfo
     */
    protected abstract AuthorizationInfo executeGetAuthorizationInfo(SecurityUser securityUser);


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (token instanceof SecurityLoginToken) {
            SecurityLoginToken securityLoginToken = (SecurityLoginToken) token;
            return executeGetAuthenticationInfo(securityLoginToken);
        } else {
            logger.warn("token not instanceof SecurityLoginToken," + token);
            return null;
        }
    }

    /**
     * 执行登陆操作，获取登陆信息
     *
     * @param securityLoginToken securityLoginToken
     * @return AuthenticationInfo
     */
    protected abstract AuthenticationInfo executeGetAuthenticationInfo(SecurityLoginToken securityLoginToken);

    protected Object getCacheKey(String prefix, PrincipalCollection principals) {
        Object userObject = getAvailablePrincipal(principals);
        if (userObject == null) {
            return principals;
        }

        String userId = null;
        if (userObject instanceof SecurityUser) {
            SecurityUser shiroUser = (SecurityUser) userObject;
            userId = shiroUser.getUserId();
        }

        if (StringUtils.hasLength(userId)) {
            return prefix + userId;
        }

        return principals;
    }

    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        String prefix = getCacheKeyPrefix() + authz_key;
        return getCacheKey(prefix, principals);
    }

    public void cleanAuthorizationCache(String userId) {
        String prefix = getCacheKeyPrefix() + authz_key + userId;
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            cache.remove(prefix);
        }
    }

    public AuthorizationInfo getAuthorizationInfo(String userId) {
        String prefix = getCacheKeyPrefix() + authz_key + userId;
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        AuthorizationInfo authorizationInfo = cache.get(prefix);
        return authorizationInfo;
    }

    /**
     * 删除用户信息,仅针对redis的sessionDAO的实现
     *
     * @param userId    userId
     * @param keyPrefix 前缀
     */
    public void cleanSession(String userId, String keyPrefix) {
        SecurityManager securityManager = SecurityUtils.getSecurityManager();
        if (securityManager instanceof SimpleWebSecurityManager) {
            SimpleWebSecurityManager manager = (SimpleWebSecurityManager) securityManager;
            SessionManager sessionManager = manager.getSessionManager();
            if (securityManager instanceof DefaultSessionManager) {
                DefaultSessionManager defaultSessionManager = (DefaultSessionManager) sessionManager;
                SessionDAO sessionDAO = defaultSessionManager.getSessionDAO();
                if (sessionDAO instanceof AbstractSecuritySessionDAO) {
                    AbstractSecuritySessionDAO abstractSecuritySessionDAO = (AbstractSecuritySessionDAO) sessionDAO;
                    abstractSecuritySessionDAO.doDelete(userId, keyPrefix);
                }
            }

        }
    }

    public void setCacheKeyPrefix(String cacheKeyPrefix) {
        this.cacheKeyPrefix = cacheKeyPrefix;
    }

    public String getCacheKeyPrefix() {
        return cacheKeyPrefix;
    }
}
