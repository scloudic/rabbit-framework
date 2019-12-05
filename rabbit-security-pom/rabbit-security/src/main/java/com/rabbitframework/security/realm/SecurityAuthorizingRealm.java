package com.rabbitframework.security.realm;

import com.rabbitframework.commons.exceptions.BizException;
import com.rabbitframework.security.SecurityUser;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 授权realm
 *
 * @author: justin.liang
 * @date: 16/5/20 下午1:52
 */
public abstract class SecurityAuthorizingRealm extends AuthorizingRealm {
    private static final Logger logger = LoggerFactory.getLogger(SecurityAuthorizingRealm.class);
    private static final String DEFAULT_CACHE_KEY_PREFIX = "security_realm_key:";
    protected String cacheKeyPrefix = DEFAULT_CACHE_KEY_PREFIX;
    private String authc_key = "authc:";
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
     * @param securityUser
     * @return
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
     * @param securityLoginToken
     * @return
     */
    protected abstract AuthenticationInfo executeGetAuthenticationInfo(SecurityLoginToken securityLoginToken);

    @Override
    protected Object getAuthenticationCacheKey(PrincipalCollection principals) {
        String prefix = cacheKeyPrefix + authc_key;
        return getCacheKey(prefix, principals);
    }

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
        String prefix = cacheKeyPrefix + authz_key;
        return getCacheKey(prefix, principals);
    }

    public void cleanAuthorizationCache(String userId) {
        String prefix = cacheKeyPrefix + authz_key + userId;
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            cache.remove(prefix);
        }
    }

    public AuthorizationInfo getAuthoriztionInfo(String userId) {
        String prefix = cacheKeyPrefix + authz_key + userId;
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        AuthorizationInfo authorizationInfo = cache.get(prefix);
        return authorizationInfo;
    }

    public void setCacheKeyPrefix(String cacheKeyPrefix) {
        this.cacheKeyPrefix = cacheKeyPrefix;
    }

    public String getCacheKeyPrefix() {
        return cacheKeyPrefix;
    }
}
