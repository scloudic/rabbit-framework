package com.rabbitframework.security.realm;

import com.rabbitframework.security.SecurityUser;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.StringUtils;

/**
 * 授权realm
 *
 * @author: justin.liang
 * @date: 16/5/20 下午1:52
 */
public abstract class SecurityAuthorizingRealm extends AuthorizingRealm {
    private static final String DEFAULT_CACHE_KEY_PREFIX = "security_realm_key:";
    protected String cacheKeyPrefix = DEFAULT_CACHE_KEY_PREFIX;
    private String authc_key = "authc:";
    private String authz_key = "authz:";

    public SecurityAuthorizingRealm() {
        super();
        setName("securityRealm");
    }

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
